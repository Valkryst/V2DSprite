package com.valkryst.V2DSprite.AnimationTest;

import com.valkryst.V2DSprite.Animation;
import com.valkryst.V2DSprite.Frame;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ResetTest {
	@Test
	public void testResetsToFirstFrame() {
		final var animation = new Animation("valid", "idle");
		animation.update(3.0);
		animation.reset();
		Assertions.assertEquals(new Frame(0, 0, 32, 32, 1000), animation.getCurrentFrame());
	}

	@Test
	public void testResetsToLastFrameWhenReversed() {
		final var animation = new Animation("valid", "idle");
		animation.setReversed(true);
		animation.reset();
		Assertions.assertEquals(new Frame(64, 32, 32, 32, 1000), animation.getCurrentFrame());
	}
}
