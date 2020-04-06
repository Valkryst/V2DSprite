package com.valkryst.V2DSprite;

import lombok.Getter;
import lombok.NonNull;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.MissingFormatArgumentException;
import java.util.Optional;

public class SpriteSheet {
    /** The original image, loaded from disk. */
    @Getter private final BufferedImage bufferedImage;
    /** The volatile image, created from the original image. */
    private VolatileImage volatileImage;

    /** The sprites. */
    private HashMap<String, Sprite> sprites = new HashMap<>();

    /**
     * Constructs a new sprite sheet.
     *
     * @param imagePath
     *          The path of the image to load.
     *
     * @param jsonPath
     *          The path of the JSON data.
     *
     * @throws FileNotFoundException
     *          If the image, or json, path points to a file that does not exist.
     *
     * @throws IOException
     *          If an I/O error occurs.
     */
    public SpriteSheet(final @NonNull Path imagePath, final @NonNull Path jsonPath) throws IOException {
        if (!Files.exists(imagePath)) {
            throw new FileNotFoundException("There is no file at '" + imagePath + "'.");
        }

        if (Files.isDirectory(imagePath)) {
            throw new IllegalArgumentException("The path '" + imagePath + "' points to a directory, not a file.");
        }

        if (!Files.exists(jsonPath)) {
            throw new FileNotFoundException("There is no file at '" + jsonPath + "'.");
        }

        if (Files.isDirectory(jsonPath)) {
            throw new IllegalArgumentException("The path '" + jsonPath + "' points to a directory, not a file.");
        }

        bufferedImage = ImageIO.read(Files.newInputStream(imagePath));

        final var parser = new JSONArray(Files.readString(jsonPath, StandardCharsets.UTF_8));
        parser.forEach(object -> {
            final var spriteData = (JSONObject) object;

            if (spriteData.has("name")) {
                final var name = (String) spriteData.get("name");
                final var sprite = new Sprite(this, spriteData);
                sprites.put(name.toLowerCase(), sprite);
            } else {
                throw new MissingFormatArgumentException("There is no name value.\n" + spriteData);
            }
        });
    }

    /**
     * Retrieves the volatile image, used for drawing sprites.
     *
     * @return
     *          The volatile image.
     */
    public VolatileImage getImage() {
        if (volatileImage == null || volatileImage.contentsLost()) {
            final var graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            final var graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
            final var graphicsConfiguration = graphicsDevice.getDefaultConfiguration();

            final int width = bufferedImage.getWidth();
            final int height = bufferedImage.getHeight();
            final int transparency = bufferedImage.getTransparency();
            volatileImage = graphicsConfiguration.createCompatibleVolatileImage(width, height, transparency);

            final var g2d = volatileImage.createGraphics();
            g2d.setComposite(AlphaComposite.Src);
            g2d.drawImage(bufferedImage, 0, 0, null);
            g2d.dispose();
        }

        return volatileImage;
    }

    /**
     * Retrieves a sprite by its name.
     *
     * @param name
     *          The name.
     *
     * @return
     *          The sprite, if a sprite with the given name exists.
     */
    public Optional<Sprite> getSpriteByName(final String name) {
        return Optional.ofNullable(sprites.get(name.toLowerCase()));
    }
}
