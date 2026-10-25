package com.valkryst.V2DSprite;

import com.valkryst.V2DSprite.adapter.AnimationEventAdapter;
import com.valkryst.V2DSprite.event.AnimationEvent;
import lombok.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class AnimationTest {
	/**
	 * Verifies that {@code onNewFrame} is called exactly once per listener when {@code FIRST_FRAME} fires,
	 * regardless of how many listeners are registered.
	 *
	 * <p>
	 *     The previous implementation called {@code sendEvent(NEW_FRAME)} inside the per-listener loop, so
	 *     with N listeners it triggered N recursive {@code sendEvent(NEW_FRAME)} calls, each iterating all N
	 *     listeners — resulting in N² {@code onNewFrame} invocations instead of N.
	 * </p>
	 */
	@Test
	public void testSendEventNewFrameCalledOncePerListenerOnFirstFrame() {
		final var animation = new Animation("valid", "idle");
		final var newFrameCount = new AtomicInteger(0);

		final var listener = new AnimationEventAdapter() {
			@Override
			public void onNewFrame(final @NonNull AnimationEvent event) {
				newFrameCount.incrementAndGet();
			}
		};

		animation.addAnimationEventListener(listener);
		animation.addAnimationEventListener(listener);

		// reset() sends FIRST_FRAME which should trigger NEW_FRAME exactly once,
		// meaning onNewFrame should be called once per registered listener (2 total).
		animation.reset();

		Assertions.assertEquals(2, newFrameCount.get());
	}

	/**
	 * Verifies that {@code onNewFrame} is called exactly once per listener when {@code LAST_FRAME} fires.
	 */
	@Test
	public void testSendEventNewFrameCalledOncePerListenerOnLastFrame() {
		final var animation = new Animation("valid", "idle");
		animation.setReversed(true);

		final var newFrameCount = new AtomicInteger(0);

		final var listener = new AnimationEventAdapter() {
			@Override
			public void onNewFrame(final @NonNull AnimationEvent event) {
				newFrameCount.incrementAndGet();
			}
		};

		animation.addAnimationEventListener(listener);
		animation.addAnimationEventListener(listener);

		// reset() in reversed mode sends LAST_FRAME which should trigger NEW_FRAME exactly once.
		animation.reset();

		Assertions.assertEquals(2, newFrameCount.get());
	}
}
