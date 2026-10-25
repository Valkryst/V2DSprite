package com.valkryst.V2DSprite.AnimationTest;

import com.valkryst.V2DSprite.Animation;
import com.valkryst.V2DSprite.Frame;
import com.valkryst.V2DSprite.event.AnimationEvent;
import com.valkryst.V2DSprite.listener.AnimationEventListener;
import com.valkryst.V2DSprite.type.AnimationEventType;
import lombok.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class UpdateTest {
	/** The {@code valid} sprite has 6 frames, each 1000ms. update(1.0) advances exactly one frame. */
	private static Animation newAnimation() {
		return new Animation("valid", "idle");
	}

	@Test
	public void testAdvancesOneFrame() {
		final var animation = newAnimation();
		animation.update(1.0);
		Assertions.assertEquals(new Frame(32, 0, 32, 32, 1000), animation.getCurrentFrame());
	}

	@Test
	public void testDoesNotAdvanceWhenPaused() {
		final var animation = newAnimation();
		animation.setPaused(true);
		animation.update(1.0);
		Assertions.assertEquals(new Frame(0, 0, 32, 32, 1000), animation.getCurrentFrame());
	}

	@Test
	public void testFiresLastFrameEvent() {
		final var animation = newAnimation();
		final List<AnimationEventType> events = new ArrayList<>();
		animation.addAnimationEventListener(new AnimationEventListener() {
			@Override public void onFirstFrame(@NonNull AnimationEvent e) { events.add(e.getType()); }
			@Override public void onNewFrame(@NonNull AnimationEvent e) { events.add(e.getType()); }
			@Override public void onLastFrame(@NonNull AnimationEvent e) { events.add(e.getType()); }
			@Override public void onPause(@NonNull AnimationEvent e) {}
			@Override public void onResume(@NonNull AnimationEvent e) {}
		});

		for (int i = 0; i < 5; i++) {
			animation.update(1.0);
		}

		Assertions.assertTrue(events.contains(AnimationEventType.LAST_FRAME));
	}

	@Test
	public void testWrapsToFirstFrameAndFiresEvent() {
		final var animation = newAnimation();
		final List<AnimationEventType> events = new ArrayList<>();
		animation.addAnimationEventListener(new AnimationEventListener() {
			@Override public void onFirstFrame(@NonNull AnimationEvent e) { events.add(e.getType()); }
			@Override public void onNewFrame(@NonNull AnimationEvent e) { events.add(e.getType()); }
			@Override public void onLastFrame(@NonNull AnimationEvent e) { events.add(e.getType()); }
			@Override public void onPause(@NonNull AnimationEvent e) {}
			@Override public void onResume(@NonNull AnimationEvent e) {}
		});

		for (int i = 0; i < 6; i++) {
			animation.update(1.0);
		}

		Assertions.assertEquals(new Frame(0, 0, 32, 32, 1000), animation.getCurrentFrame());
		Assertions.assertTrue(events.contains(AnimationEventType.FIRST_FRAME));
	}

	@Test
	public void testSpeedMultiplier() {
		final var animation = newAnimation();
		animation.setSpeed(2.0);
		animation.update(1.0);
		Assertions.assertEquals(new Frame(64, 0, 32, 32, 1000), animation.getCurrentFrame());
	}

	@Test
	public void testReversedAdvancesBackward() {
		final var animation = newAnimation();
		animation.setReversed(true);
		animation.reset();
		animation.update(1.0);
		Assertions.assertEquals(new Frame(32, 32, 32, 32, 1000), animation.getCurrentFrame());
	}
}
