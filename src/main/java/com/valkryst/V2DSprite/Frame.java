package com.valkryst.V2DSprite;

import lombok.NonNull;

import java.util.Arrays;
import java.util.zip.DataFormatException;

/**
 * Represents a single frame of an {@link Animation}.
 *
 * @param x
 * 		X-axis position of the top-left pixel of the frame within a
 * 		{@link SpriteSheet}
 *
 * @param y
 * 		Y-axis position of the top-left pixel of the frame within a
 * 		{@link SpriteSheet}.
 *
 * @param width Width of the frame within a {@link SpriteSheet}.
 *
 * @param height Height of the frame within a {@link SpriteSheet}.
 *
 * @param duration The duration of the frame in milliseconds.
 */
public record Frame(int x, int y, int width, int height, int duration) {
	/**
	 * Parses a {@code Frame} from a delimited {@link String}.
	 *
	 * @param delimitedString The delimited string to parse.
	 * @param delimiter The delimiter to use.
	 *
	 * @return The parsed {@code Frame}.
	 *
	 * @throws DataFormatException If the string is incorrectly formatted.
	 */
	public static Frame from(final @NonNull String delimitedString, final int delimiter) throws DataFormatException {
		final var split = delimitedString.split(
			Arrays.toString(Character.toChars(delimiter))
		);

		if (split.length != 5) {
			throw new DataFormatException(
				String.format(
					"The string \"%s\" does not have the requisite number of values.",
					delimitedString
				)
			);
		}

		return new Frame(
			Integer.parseInt(split[0]),
			Integer.parseInt(split[1]),
			Integer.parseInt(split[2]),
			Integer.parseInt(split[3]),
			Integer.parseInt(split[4])
		);
	}
}
