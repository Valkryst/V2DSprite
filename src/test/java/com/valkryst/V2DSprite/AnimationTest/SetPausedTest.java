package com.valkryst.V2DSprite.AnimationTest;

import com.valkryst.V2DSprite.Animation;
import com.valkryst.V2DSprite.event.AnimationEvent;
import com.valkryst.V2DSprite.listener.AnimationEventListener;
import com.valkryst.V2DSprite.type.AnimationEventType;
import lombok.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class SetPausedTest {
	@Test
	public void testPause() {
		final var animation = new Animation("valid", "idle");
		animation.setPaused(true);
		Assertions.assertTrue(animation.isPaused());
	}

	@Test
	public void testResume() {
		final var animation = new Animation("valid", "idle");
		animation.setPaused(true);
		animation.setPaused(false);
		Assertions.assertFalse(animation.isPaused());
	}

	@Test
	public void testFiresPauseEvent() {
		final var animation = new Animation("valid", "idle");
		final List<AnimationEventType> events = new ArrayList<>();
		animation.addAnimationEventListener(new AnimationEventListener() {
			@Override public void onFirstFrame(@NonNull AnimationEvent e) {}
			@Override public void onNewFrame(@NonNull AnimationEvent e) {}
			@Override public void onLastFrame(@NonNull AnimationEvent e) {}
			@Override public void onPause(@NonNull AnimationEvent e) { events.add(e.getType()); }
			@Override public void onResume(@NonNull AnimationEvent e) {}
		});

		animation.setPaused(true);
		Assertions.assertTrue(events.contains(AnimationEventType.PAUSE));
	}

	@Test
	public void testFiresResumeEvent() {
		final var animation = new Animation("valid", "idle");
		final List<AnimationEventType> events = new ArrayList<>();
		animation.addAnimationEventListener(new AnimationEventListener() {
			@Override public void onFirstFrame(@NonNull AnimationEvent e) {}
			@Override public void onNewFrame(@NonNull AnimationEvent e) {}
			@Override public void onLastFrame(@NonNull AnimationEvent e) {}
			@Override public void onPause(@NonNull AnimationEvent e) {}
			@Override public void onResume(@NonNull AnimationEvent e) { events.add(e.getType()); }
		});

		animation.setPaused(true);
		animation.setPaused(false);
		Assertions.assertTrue(events.contains(AnimationEventType.RESUME));
	}
}
