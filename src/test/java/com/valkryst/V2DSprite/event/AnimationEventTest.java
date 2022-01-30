package com.valkryst.V2DSprite.event;

import com.valkryst.V2DSprite.Animation;
import com.valkryst.V2DSprite.Frame;
import com.valkryst.V2DSprite.type.AnimationEventType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AnimationEventTest {
	private final static Animation animation = new Animation("valid", "idle");
	private final static Frame frame = new Frame(0, 0, 0, 0, 0);

	@Test
	public void testConstructor() {
		final var event = new AnimationEvent(animation, AnimationEventType.NEW_FRAME, frame, 0);
		Assertions.assertEquals(animation, event.getSource());
		Assertions.assertEquals(AnimationEventType.NEW_FRAME, event.getType());
		Assertions.assertEquals(frame, event.getFrame());
		Assertions.assertEquals(0, event.getIndex());
	}

	@Test
	public void testConstructorWithNullSource() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new AnimationEvent(null, AnimationEventType.NEW_FRAME, frame, 0);
		});
	}

	@Test
	public void testConstructorWithNullType() {
		Assertions.assertThrows(NullPointerException.class, () -> {

			new AnimationEvent(animation, null, frame, 0);
		});
	}

	@Test
	public void testConstructorWithNullFrame() {
		Assertions.assertThrows(NullPointerException.class, () -> {

			new AnimationEvent(animation, AnimationEventType.NEW_FRAME, null, 0);
		});
	}
}
