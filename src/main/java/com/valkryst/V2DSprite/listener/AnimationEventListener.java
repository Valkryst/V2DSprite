package com.valkryst.V2DSprite.listener;

import com.valkryst.V2DSprite.Animation;
import com.valkryst.V2DSprite.Frame;
import com.valkryst.V2DSprite.event.AnimationEvent;
import lombok.NonNull;

import java.util.EventListener;

public interface AnimationEventListener extends EventListener {
	/**
	 * This occurs when the first {@link Frame} is reached.
	 *
	 * @param event The event.
	 */
	void onFirstFrame(final @NonNull AnimationEvent event);

	/**
	 * This occurs when a new {@link Frame} is reached. This occurs for all
	 * frames, including the first and last frames.
	 *
	 * @param event The event.
	 */
	void onNewFrame(final @NonNull AnimationEvent event);

	/**
	 * This occurs when the last {@link Frame} is reached.
	 *
	 * @param event The event.
	 */
	void onLastFrame(final @NonNull AnimationEvent event);

	/**
	 * This occurs when the {@link Animation} is paused.
	 *
	 * @param event The event.
	 */
	void onPause(final @NonNull AnimationEvent event);

	/**
	 * This occurs when the {@link Animation} is resumed.
	 *
	 * @param event The event.
	 */
	void onResume(final @NonNull AnimationEvent event);
}
