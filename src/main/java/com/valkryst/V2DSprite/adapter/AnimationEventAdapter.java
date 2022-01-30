package com.valkryst.V2DSprite.adapter;

import com.valkryst.V2DSprite.event.AnimationEvent;
import com.valkryst.V2DSprite.listener.AnimationEventListener;
import lombok.NonNull;

/**
 * An abstract adapter class for receiving {@link AnimationEvent}s. The methods
 * in this class are empty. This class exists as convenience for creating
 * listener objects.
 */
public abstract class AnimationEventAdapter implements AnimationEventListener {
	@Override
	public void onFirstFrame(final @NonNull AnimationEvent event) {}

	@Override
	public void onNewFrame(final @NonNull AnimationEvent event) {}

	@Override
	public void onLastFrame(final @NonNull AnimationEvent event) {}

	@Override
	public void onPause(final @NonNull AnimationEvent event) {}

	@Override
	public void onResume(final @NonNull AnimationEvent event) {}
}
