package com.valkryst.V2DSprite;

import lombok.NonNull;

import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

/**
 * Represents the hitbox of a single {@link Frame} of an {@link Animation}.
 *
 * @param x X-axis position of the top-left pixel of the box within a {@link Frame}
 * @param y Y-axis position of the top-left pixel of the box within a {@link Frame}.
 * @param width Width of the box within a {@link Frame}.
 * @param height Height of the box within a {@link Frame}.
 */
public record Hitbox(int x, int y, int width, int height) {
	/**
	 * Parses a {@code Hitbox} from a delimited {@link String}.
	 *
	 * @param delimitedString Delimited string to parse.
	 * @param delimiter Delimiter to use.
	 *
	 * @return Parsed {@code Hitbox}.
	 */
	public static Hitbox from(final @NonNull String delimitedString, final int delimiter) throws DataFormatException {
		final var split = delimitedString.split(
			Pattern.quote(String.valueOf((char) delimiter))
		);

		if (split.length != 4) {
			throw new DataFormatException(
				String.format(
					"The string \"%s\" does not have the requisite number of values.",
					delimitedString
				)
			);
		}

		return new Hitbox(
			Integer.parseInt(split[0]),
			Integer.parseInt(split[1]),
			Integer.parseInt(split[2]),
			Integer.parseInt(split[3])
		);
	}
}
