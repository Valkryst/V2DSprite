package com.valkryst.V2DSprite;

import lombok.NonNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class SpriteAtlas {
    /** The image loaded from disk. */
    private final BufferedImage bufferedAtlasImage;
    /** The image used to draw with. */
    private VolatileImage volatileAtlasImage;
    /** The sprite sheets contained on the atlas image. */
    private final HashMap<String, SpriteSheet> spriteSheets = new HashMap<>();

    /**
     * Constructs a new SpriteAtlas.
     *
     * Closes streams after use.
     *
     * @param atlasImageStream
     *          The input stream for the atlas' image.
     *
     * @param atlasJSONStream
     *          The input stream for the atlas' JSON data.
     *
     * @throws NullPointerException
     *          If the either stream is null.
     *
     * @throws IOException
     *          If an error occurs during reading or when not able to create required ImageInputStream.
     *
     * @throws IllegalArgumentException
     *          If the size of the loaded image is greater than 2048x2048.
     *
     * @throws ParseException
     *          If an error occurs during parsing of the JSON file.
     */
    public SpriteAtlas(final @NonNull InputStream atlasImageStream, final @NonNull InputStream atlasJSONStream) throws IOException, ParseException {
        // Load Buffered Image:
        bufferedAtlasImage = ImageIO.read(atlasImageStream);

        if (bufferedAtlasImage.getWidth() > 2048 || bufferedAtlasImage.getHeight() > 2048) {
            throw new IllegalArgumentException("The image used by the sprite atlas should not exceed 2048x2048 in size.");
        }

        volatileAtlasImage = convertToVolatileImage(bufferedAtlasImage);
        atlasImageStream.close();

        // Load JSON Data:
        final JSONParser parser = new JSONParser();
        final JSONObject atlasData = (JSONObject) parser.parse(new InputStreamReader(atlasJSONStream));

        final JSONArray sheetDataArray = (JSONArray) atlasData.get("Sheets");
        sheetDataArray.forEach(sheetData -> {
            final SpriteSheet sheet = new SpriteSheet(this, (JSONObject) sheetData);
            spriteSheets.put(sheet.getName(), sheet);
        });

        // Close Streams:
        atlasImageStream.close();
        atlasJSONStream.close();
    }

    /**
     * Converts a BufferedImage into a VolatileImage,
     *
     * @param source
     *        The BufferedImage.
     *
     * @return
     *        The VolatileImage.
     */
    private static VolatileImage convertToVolatileImage(final BufferedImage source) {
        final GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
        final GraphicsConfiguration graphicsConfiguration = graphicsDevice.getDefaultConfiguration();

        final VolatileImage destination = graphicsConfiguration.createCompatibleVolatileImage(source.getWidth(), source.getHeight(), source.getTransparency());

        final Graphics2D g2d = destination.createGraphics();
        g2d.setComposite(AlphaComposite.Src);
        g2d.drawImage(source, 0, 0, null);
        g2d.dispose();

        return destination;
    }

    /**
     * Retrieves the volatile atlas image.
     *
     * @return
     *          The volatile atlas image.
     */
    public VolatileImage getAtlasImage() {
        if (volatileAtlasImage == null || volatileAtlasImage.contentsLost()) {
            volatileAtlasImage = convertToVolatileImage(bufferedAtlasImage);
        }

        return volatileAtlasImage;
    }

    /**
     * Retrieves a specific sprite sheet.
     *
     * @param name
     *          The name of the sheet to retrieve.
     *
     * @return
     *          Either the sheet, or null if the name is null, empty, or if
     *          no sheet using the specified name exists.
     */
    public SpriteSheet getSpriteSheet(final String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        return spriteSheets.get(name);
    }
}
