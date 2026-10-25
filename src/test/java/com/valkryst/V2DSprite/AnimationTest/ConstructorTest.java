package com.valkryst.V2DSprite.AnimationTest;

import com.valkryst.V2DSprite.Animation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class ConstructorTest {
	@Test
	public void testValid() {
		Assertions.assertDoesNotThrow(() -> new Animation("valid", "idle"));
	}

	@Test
	public void testValidWithFramesOnly() {
		Assertions.assertDoesNotThrow(() -> new Animation("valid_frames_only", "idle"));
	}

	@Test
	public void testNullSpriteName() {
		Assertions.assertThrows(NullPointerException.class, () -> new Animation(null, "idle"));
	}

	@Test
	public void testNullAnimationName() {
		Assertions.assertThrows(NullPointerException.class, () -> new Animation("valid", null));
	}

	@Test
	public void testInvalidSpriteName() {
		Assertions.assertThrows(FileNotFoundException.class, () -> new Animation("nonexistent", "idle"));
	}
}
