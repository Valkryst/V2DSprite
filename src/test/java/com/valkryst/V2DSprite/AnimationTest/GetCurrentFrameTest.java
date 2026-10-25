package com.valkryst.V2DSprite.AnimationTest;

import com.valkryst.V2DSprite.Animation;
import com.valkryst.V2DSprite.Frame;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GetCurrentFrameTest {
	@Test
	public void testReturnsFirstFrameInitially() {
		final var animation = new Animation("valid", "idle");
		Assertions.assertEquals(new Frame(0, 0, 32, 32, 1000), animation.getCurrentFrame());
	}

	@Test
	public void testReturnsCorrectFrameAfterUpdate() {
		final var animation = new Animation("valid", "idle");
		animation.update(1.0);
		Assertions.assertEquals(new Frame(32, 0, 32, 32, 1000), animation.getCurrentFrame());
	}
}
