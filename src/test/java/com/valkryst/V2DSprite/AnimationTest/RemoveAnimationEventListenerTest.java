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

public class RemoveAnimationEventListenerTest {
	@Test
	public void testRemovedListenerDoesNotReceiveEvents() {
		final var animation = new Animation("valid", "idle");
		final List<AnimationEventType> events = new ArrayList<>();
		final var listener = new AnimationEventListener() {
			@Override public void onFirstFrame(@NonNull AnimationEvent e) {}
			@Override public void onNewFrame(@NonNull AnimationEvent e) { events.add(e.getType()); }
			@Override public void onLastFrame(@NonNull AnimationEvent e) {}
			@Override public void onPause(@NonNull AnimationEvent e) {}
			@Override public void onResume(@NonNull AnimationEvent e) {}
		};

		animation.addAnimationEventListener(listener);
		animation.removeAnimationEventListener(listener);
		animation.update(1.0);
		Assertions.assertTrue(events.isEmpty());
	}
}
