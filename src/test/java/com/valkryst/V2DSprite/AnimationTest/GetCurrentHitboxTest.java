package com.valkryst.V2DSprite.AnimationTest;

import com.valkryst.V2DSprite.Animation;
import com.valkryst.V2DSprite.Hitbox;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GetCurrentHitboxTest {
	@Test
	public void testReturnsHitbox() {
		final var animation = new Animation("valid", "idle");
		Assertions.assertEquals(new Hitbox(0, 0, 32, 32), animation.getCurrentHitbox());
	}

	@Test
	public void testReturnsNullWhenNoHitboxes() {
		final var animation = new Animation("valid_frames_only", "idle");
		Assertions.assertNull(animation.getCurrentHitbox());
	}
}
