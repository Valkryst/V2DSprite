package com.valkryst.V2DSprite;

import lombok.NonNull;

import java.util.Arrays;
import java.util.zip.DataFormatException;

/**
 * Represents the collision box of a single {@link Frame} of an
 * {@link Animation}.
 *
 * @param x
 * 		X-axis position of the top-left pixel of the box within a {@link Frame}
 *
 * @param y
 * 		Y-axis position of the top-left pixel of the box within a {@link Frame}.
 *
 * @param width Width of the box within a {@link Frame}.
 *
 * @param height Height of the box within a {@link Frame}.
 */
public record CollisionBox(int x, int y, int width, int height) {

	/**
	 * Parses a {@code CollisionBox} from a delimited {@link String}.
	 *
	 * @param delimitedString The delimited string to parse.
	 * @param delimiter The delimiter to use.
	 *
	 * @return The parsed {@code CollisionBox}.
	 */
	public static CollisionBox from(final @NonNull String delimitedString, final int delimiter) throws DataFormatException {
		final var split = delimitedString.split(
			Arrays.toString(Character.toChars(delimiter))
		);

		if (split.length != 4) {
			throw new DataFormatException(
				String.format(
					"The string \"%s\" does not have the requisite number of values.",
					delimitedString
				)
			);
		}

		return new CollisionBox(
			Integer.parseInt(split[0]),
			Integer.parseInt(split[1]),
			Integer.parseInt(split[2]),
			Integer.parseInt(split[3])
		);
	}
}
