package com.valkryst.V2DSprite;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class SpriteSheet {
	/** A cache of all recently loaded {@code SpriteSheet}s. */
	private final static Cache<String, SpriteSheet> SPRITE_SHEETS = Caffeine.newBuilder()
																			.initialCapacity(0)
																			.expireAfterAccess(150, TimeUnit.SECONDS)
																			.build();

	/** All image formats supported by {@link javax.imageio}. */
	private final static String[] SUPPORTED_FORMATS = new String[] {
		"bmp", "gif", "jpeg", "jpg", "png", "tiff", "tif", "wbmp"
	};

	@Getter private final BufferedImage image;

	/**
	 * Constructs a new {@code SpriteSheet}.
	 *
	 * @param spriteName
	 * 			<p>The name of the sprite.</p>
	 *
	 *			<p>
	 *			    This corresponds to the name of the folder containing the
	 *				sprite sheet and animation data. If {@code spriteName} is
	 *				{@code "hero"}, then the sprite sheet and animation data
	 *				would be located in the {@code "/sprites/hero"} directory.
	 *			</p>
	 *
	 * @throws FileNotFoundException
	 * 			If the image file cannot be found within the JAR.
	 *
	 * @throws IOException If an error occurs when loading the image.
	 */
	private SpriteSheet(final @NonNull String spriteName) throws IOException {
		for (final String format : SUPPORTED_FORMATS) {
			final var inputStream = SpriteSheet.class.getResourceAsStream(
				String.format(
					"/sprites/%s/image.%s",
					spriteName,
					format
				)
			);

			if (inputStream != null) {
				image = ImageIO.read(inputStream);
				inputStream.close();
				return;
			}
		}

		throw new FileNotFoundException(
			String.format("""
				Could not find the image file for the sprite sheet named "%s",
				using any of the following formats: %s
			""", spriteName, Arrays.toString(SUPPORTED_FORMATS))
		);
	}

	/**
	 * Loads a {@code SpriteSheet} and returns it.
	 *
	 * @param spriteName
	 * 			<p>The name of the sprite.</p>
	 *
	 *			<p>
	 *			    This corresponds to the name of the folder containing the
	 *				sprite sheet and animation data. If {@code spriteName} is
	 *				{@code "hero"}, then the sprite sheet and animation data
	 *				would be located in the {@code "/sprites/hero"} directory.
	 *			</p>
	 *
	 * @return The {@code SpriteSheet}.
	 */
	@SneakyThrows
	public static SpriteSheet load(final @NonNull String spriteName) {
		var sheet = SPRITE_SHEETS.getIfPresent(spriteName);

		if (sheet == null) {
			sheet = new SpriteSheet(spriteName);
			SPRITE_SHEETS.put(spriteName, sheet);
		}

		return sheet;
	}
}
