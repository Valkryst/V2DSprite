package com.valkryst.V2DSprite.AnimationTest;

import com.valkryst.V2DSprite.Animation;
import com.valkryst.V2DSprite.CollisionBox;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GetCurrentCollisionBoxTest {
	@Test
	public void testReturnsCollisionBox() {
		final var animation = new Animation("valid", "idle");
		Assertions.assertEquals(new CollisionBox(0, 0, 32, 32), animation.getCurrentCollisionBox());
	}

	@Test
	public void testReturnsNullWhenNoCollisionBoxes() {
		final var animation = new Animation("valid_frames_only", "idle");
		Assertions.assertNull(animation.getCurrentCollisionBox());
	}
}
