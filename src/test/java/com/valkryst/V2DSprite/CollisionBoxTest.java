package com.valkryst.V2DSprite;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.zip.DataFormatException;

public class CollisionBoxTest {
	@Test
	public void testFrom() throws DataFormatException {
		final var box = CollisionBox.from("1,2,3,4", ',');
		Assertions.assertEquals(1, box.x());
		Assertions.assertEquals(2, box.y());
		Assertions.assertEquals(3, box.width());
		Assertions.assertEquals(4, box.height());
	}

	@Test
	public void testFromWithNullDelimitedString() {
		Assertions.assertThrows(NullPointerException.class, () -> CollisionBox.from(null, 0));
	}

	@Test
	public void testFromWithTooFewValues() {
		Assertions.assertThrows(DataFormatException.class, () -> CollisionBox.from("1,2,3", ','));
	}

	@Test
	public void testFromWithTooManyValues() {
		Assertions.assertThrows(DataFormatException.class, () -> CollisionBox.from("1,2,3,4,5", ','));
	}

	@Test
	public void testFromWithNonIntegerValues() {
		Assertions.assertThrows(NumberFormatException.class, () -> CollisionBox.from("1,2,3,a", ','));
	}
}
