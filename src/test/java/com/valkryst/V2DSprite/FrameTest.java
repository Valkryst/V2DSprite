package com.valkryst.V2DSprite;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.zip.DataFormatException;

public class FrameTest {
	@Test
	public void testFrom() throws DataFormatException {
		final var frame = Frame.from("1,2,3,4,5", ',');
		Assertions.assertEquals(1, frame.x());
		Assertions.assertEquals(2, frame.y());
		Assertions.assertEquals(3, frame.width());
		Assertions.assertEquals(4, frame.height());
		Assertions.assertEquals(5, frame.duration());
	}

	@Test
	public void testFromWithNullDelimitedString() {
		Assertions.assertThrows(NullPointerException.class, () -> {
			Frame.from(null, 0);
		});
	}

	@Test
	public void testFromWithTooFewValues() {
		Assertions.assertThrows(DataFormatException.class, () -> {
			Frame.from("1,2,3,4", ',');
		});
	}

	@Test
	public void testFromWithTooManyValues() {
		Assertions.assertThrows(DataFormatException.class, () -> {
			Frame.from("1,2,3,4,5,6", ',');
		});
	}

	@Test
	public void testFromWithNonIntegerValues() {
		Assertions.assertThrows(NumberFormatException.class, () -> {
			Frame.from("1,2,3,4,a", ',');
		});
	}
}
