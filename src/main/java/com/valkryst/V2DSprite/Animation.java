package com.valkryst.V2DSprite;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.valkryst.V2DSprite.event.AnimationEvent;
import com.valkryst.V2DSprite.listener.AnimationEventListener;
import com.valkryst.V2DSprite.type.AnimationEventType;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.DataFormatException;

public class Animation {
	private final static Map<Class, Cache<Integer, Object>> DATA_CACHES = new HashMap<>();

	private final List<AnimationEventListener> listeners = new ArrayList<>(0);

	private final String spriteName;

	// todo document
	private final CollisionBox[] collisionBoxes;

	private final Frame[] frames;

	/** Index of the current frame. */
	private int frameIndex = 0;

	/**
	 * The time elapsed, in milliseconds, since the current frame was first
	 * displayed.
	 */
	private double frameTime = 0;

	// todo document
	private final Hitbox[] hitboxes;

	/** Whether to ignore calls to {@link #update(double)}. */
	@Getter private boolean paused = false;

	/** Whether to play the animation in reverse. */
	@Getter @Setter private boolean reversed = false;

	/** The speed at which the animation should play. */
	@Getter @Setter private double speed = 1.0;

	/**
	 * Constructs a new {@code Animation}.
	 *
	 * @param spriteName
	 * 			<p>The name of the sprite.</p>
	 *
	 *			<p>
	 *			    This corresponds to the name of the folder containing the
	 *				sprite sheet and animation data. If {@code spriteName} is
	 *				{@code "hero"}, then the sprite sheet and animation data
	 *				would be located in the {@code "/sprites/hero"} directory.
	 *			</p>
	 *
	 * @param animationName
	 * 			<p>The name of the animation.</p>
	 *
	 * 			<p>
	 * 			    This corresponds to the name of the file containing the
	 * 				animation data. If {@code spriteName} is {@code "hero"} and
	 * 				{@code animationName} is {@code "walk"}, then the file would
	 * 				be {@code "walk.ini"} in the {@code "/sprites/hero"}
	 * 				directory.
	 * 			</p>
	 */
	@SneakyThrows
	public Animation(final @NonNull String spriteName, final @NonNull String animationName) {
		this.spriteName = spriteName;
		this.frames = (Frame[]) loadData(Frame.class, spriteName, animationName);

		CollisionBox[] collisionBoxes;
		try {
			collisionBoxes = (CollisionBox[]) loadData(CollisionBox.class, spriteName, animationName);

			if (collisionBoxes.length != frames.length) {
				throw new IllegalStateException("There must be an equal number of collision boxes and frames. There are currently " + collisionBoxes.length + " collision boxes and " + frames.length + " frames.");
			}
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
			collisionBoxes = new CollisionBox[0];
		}
		this.collisionBoxes = collisionBoxes;


		Hitbox[] hitboxes;
		try {
			hitboxes = (Hitbox[]) loadData(Hitbox.class, spriteName, animationName);

			if (hitboxes.length != frames.length) {
				throw new IllegalStateException("There must be an equal number of hitboxes and frames. There are currently " + hitboxes.length + " hitboxes and " + frames.length + " frames.");
			}
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
			hitboxes = new Hitbox[0];
		}
		this.hitboxes = hitboxes;
	}

	private Object loadData(final Class klass, final String spriteName, final String animationName) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		/*
		 * Attempt to load data from one of the caches, or create a new cache
		 * if required.
		 */
		final int cacheKey = Objects.hash(spriteName, animationName);
		Object data;

		if (DATA_CACHES.containsKey(klass)) {
			data = DATA_CACHES.get(klass).getIfPresent(cacheKey);

			if (data != null) {
				return data;
			}
		} else {
			DATA_CACHES.put(
				klass,
				Caffeine.newBuilder()
						.initialCapacity(0)
						.expireAfterAccess(5, TimeUnit.MINUTES)
						.build()
			);
		}

		// Attempt to load data from the JAR.
		final var path = String.format(
			"/sprites/%s/animations/%s_%s.tsv",
			spriteName,
			animationName,
			klass.getSimpleName().toLowerCase(Locale.ROOT)
		);

		final var inputStream = Animation.class.getResourceAsStream(path);
		if (inputStream == null) {
			throw new FileNotFoundException("Could not load data from '" + path + "'.");
		}

		final var bufferedReader = new BufferedReader(
			new InputStreamReader(
				Objects.requireNonNull(Animation.class.getResourceAsStream(path))
			)
		);

		final var lines = bufferedReader.lines().toList();
		bufferedReader.close();

		// Attempt to construct objects from the loaded data.
		final var fromMethod = klass.getMethod("from", String.class, int.class);

		data = Array.newInstance(klass, lines.size());
		for (int i = 0 ; i < lines.size() ; i++) {
			Array.set(data, i, fromMethod.invoke(null, lines.get(i), '\t'));
		}
		DATA_CACHES.get(klass).put(cacheKey, data);

		return data;
	}

	/**
	 * Updates the animation.
	 *
	 * @param deltaTime
	 * 			 <p>
	 * 			     The time elapsed, in milliseconds, since the last update.
	 * 			 </p>
	 *
	 * 			 <p>
	 * 			    It is assumed that this value is in fractions of a second.
	 * 			 	i.e. 16ms is represented as 0.0016.
	 * 			 </p>
	 */
	public void update(final double deltaTime) {
		if (paused) {
			return;
		}

		/*
		 * The frame time is expressed in milliseconds, so we need to multiply
		 * by 1000 to ensure the added value is also in milliseconds.
		 */
		frameTime += deltaTime * speed * 1000;

		while (frameTime >= frames[frameIndex].duration()) {
			frameTime -= frames[frameIndex].duration();

			if (reversed) {
				if (frameIndex == 0) {
					frameIndex = frames.length - 1;
					sendEvent(AnimationEventType.FIRST_FRAME);
					continue;
				}

				if (frameIndex == 1) {
					frameIndex--;
					sendEvent(AnimationEventType.LAST_FRAME);
					continue;
				}
			} else {
				if (frameIndex == frames.length - 1) {
					frameIndex = 0;
					sendEvent(AnimationEventType.FIRST_FRAME);
					continue;
				}

				if (frameIndex == frames.length - 2) {
					frameIndex++;
					sendEvent(AnimationEventType.LAST_FRAME);
					continue;
				}
			}

			frameIndex += reversed ? -1 : 1;
			sendEvent(AnimationEventType.NEW_FRAME);
		}
	}

	/** Resets the animation to its initial state. */
	public void reset() {
		if (reversed) {
			frameIndex = frames.length - 1;
			sendEvent(AnimationEventType.LAST_FRAME);
		} else {
			frameIndex = 0;
			sendEvent(AnimationEventType.FIRST_FRAME);
		}

		frameTime = 0;
	}

	/**
	 * <p>Draws the current #{@link Frame} on a given graphics context.</p>
	 *
	 * <p>
	 *     Assumes that the graphics context's clipping area has been set
	 *     prior to calling this method. This will ensure that the frame is
	 *     drawn at the correct location.
	 * </p>
	 *
	 * <pre>{@code
	 * var frame = animation.getCurrentFrame();
	 * gc.setClip(50, 75, frame.width(), frame.height());
	 * }</pre>
	 *
	 * @param gc The graphics context to draw on.
	 */
	public void draw(final @NonNull Graphics gc) {
		final var frame = frames[frameIndex];
		final var clipBounds = gc.getClipBounds();

		gc.drawImage(
			SpriteSheet.load(spriteName).getImage(),
			clipBounds.x,
			clipBounds.y,
			clipBounds.x + frame.width(),
			clipBounds.y + frame.height(),
			frame.x(),
			frame.y(),
			frame.x() + frame.width(),
			frame.y() + frame.height(),
			null
		);
	}

	/**
	 * todo Document better.
	 * <p>
	 *     Performs the same operation as {@link #draw(Graphics)}, but with an
	 *     {@link AffineTransform} applied to the graphics context.
	 * </p>
	 *
	 * <p>
	 *     The original {@link AffineTransform} of the graphics context is
	 *     restored after drawing the frame.
	 * </p>
	 *
	 * @param gc The graphics context to draw on.
	 * @param transform The transform to apply to the frame.
	 */
	public void draw(final @NonNull Graphics2D gc, final AffineTransform transform) {
		final var oldTransform = gc.getTransform();
		final var oldClip = gc.getClip();

		gc.setTransform(transform);
		gc.setClip(transform.createTransformedShape(oldClip));

		draw(gc);

		gc.setTransform(oldTransform);
		gc.setClip(oldClip);
	}

	/**
	 * Adds an {@link AnimationEventListener} to this {@code Animation}.
	 *
	 * @param listener The listener to add.
	 */
	public void addAnimationEventListener(final @NonNull AnimationEventListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes an {@link AnimationEventListener} from this {@code Animation}.
	 *
	 * @param listener The listener to remove.
	 */
	public void removeAnimationEventListener(final @NonNull AnimationEventListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Sends an {@link AnimationEvent} to all {@link AnimationEventListener}s.
	 *
	 * @param type The type of event to send.
	 */
	private void sendEvent(final @NonNull AnimationEventType type) {
		if (listeners.isEmpty()) {
			return;
		}

		final var event = new AnimationEvent(
			this,
			type,
			frames[frameIndex],
			frameIndex
		);

		for (final var listener : listeners) {
			switch (type) {
				case FIRST_FRAME -> {
					listener.onFirstFrame(event);
					sendEvent(AnimationEventType.NEW_FRAME);
				}
				case NEW_FRAME -> listener.onNewFrame(event);
				case LAST_FRAME -> {
					listener.onLastFrame(event);
					sendEvent(AnimationEventType.NEW_FRAME);
				}
				case PAUSE -> listener.onPause(event);
				case RESUME -> listener.onResume(event);
			}
		}
	}

	/**
	 * Retrieves the current {@link CollisionBox}.
	 *
	 * @return
	 * 		The current collision box, or null if the animation has no collision
	 * 		boxes.
	 */
	public CollisionBox getCurrentCollisionBox() {
		if (collisionBoxes.length > 0) {
			return collisionBoxes[frameIndex];
		} else {
			return null;
		}
	}

	/**
	 * Retrieves the current {@link Frame}.
	 *
	 * @return The current frame.
	 */
	public Frame getCurrentFrame() {
		return frames[frameIndex];
	}

	/**
	 * Retrieves the current {@link Hitbox}.
	 *
	 * @return
	 * 		The current hit box, or null if the animation has no hit
	 * 		boxes.
	 */
	public Hitbox getCurrentHitbox() {
		if (hitboxes.length > 0) {
			return hitboxes[frameIndex];
		} else {
			return null;
		}
	}

	/**
	 * Sets the pause state of the animation.
	 *
	 * @param paused The new pause state.
	 */
	public void setPaused(final boolean paused) {
		this.paused = paused;
		sendEvent(paused ? AnimationEventType.PAUSE : AnimationEventType.RESUME);
	}
}
