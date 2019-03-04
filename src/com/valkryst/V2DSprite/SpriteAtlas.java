package com.valkryst.V2DSprite;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.valkryst.VJSON.VJSON;
import lombok.NonNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SpriteAtlas {
    /** The cache of recently loaded atlases. */
    private final static Cache<Integer, SpriteAtlas> ATLAS_CACHE = Caffeine.newBuilder().initialCapacity(1).expireAfterAccess(5, TimeUnit.MINUTES).build();

    /** The image loaded from disk. */
    private final BufferedImage bufferedAtlasImage;
    /** The image used to draw with. */
    private VolatileImage volatileAtlasImage;

    /** The sprite sheets on the atlas. */
    private final HashMap<String, SpriteSheet> spriteSheets = new HashMap<>(0);

    /**
     * Constructs a new SpriteAtlas.
     *
     * @param atlas
     *          The atlas.
     *
     * @param dataArray
     *          A JSON array containing the atlas' data.
     */
    private SpriteAtlas(final BufferedImage atlas, final JSONArray dataArray) {
        this.bufferedAtlasImage = atlas;
        volatileAtlasImage = convertToVolatileImage(bufferedAtlasImage);

        JSONObject data;
        for (int i = 0 ; i < dataArray.size() ; i++) {
            data = (JSONObject) dataArray.get(i);
            final String name = VJSON.getString(data, "Name");

            if (spriteSheets.containsValue(name)) {
                throw new IllegalStateException("The sprite atlas has two sprite sheets wit hthe name name ('" + name + "').");
            }

            spriteSheets.put(name, new SpriteSheet(this, (JSONArray) data.get("Sprite Sheet")));
        }
    }

    /**
     * Creates a new SpriteAtlas.
     *
     * @param pngPath
     *          The file path of the atlas' image.
     *
     * @param jsonPath
     *          The file path of the atlas' JSON data.
     *
     * @return
     *          The sprite atlas.
     *
     * @throws FileNotFoundException
     *          If the PNG/JSON path is empty.
     *          If the PNG/JSON path points to a directory.
     *          If the PNG/JSON file cannot be found within the Jar or on the filesystem.
     *
     * @throws NullPointerException
     *          If the either file path is null.
     *
     * @throws IOException
     *          If an error occurs during reading or when not able to create required ImageInputStream.
     *
     * @throws IllegalArgumentException
     *          If the size of the loaded image is greater than 2048x2048.
     *
     * @throws ParseException
     *          If there's an error when parsing the JSON.
     */
    public static SpriteAtlas createSpriteAtlas(final @NonNull String pngPath, final @NonNull String jsonPath) throws IOException, ParseException {
        // Check cache for atlas.
        final int hash = Objects.hash(pngPath, jsonPath);
        final SpriteAtlas cachedAtlas = ATLAS_CACHE.getIfPresent(hash);

        if (cachedAtlas != null) {
            return cachedAtlas;
        }

        // Check for empty paths.
        if (pngPath.isEmpty()) {
            throw new FileNotFoundException("The PNG path cannot be empty.");
        }

        if (jsonPath.isEmpty()) {
            throw new FileNotFoundException("The JSON path cannot be empty.");
        }

        // Attempt to load from Jar.
        InputStream pngInputStream = SpriteAtlas.class.getClassLoader().getResourceAsStream(pngPath);
        InputStream jsonInputStream = SpriteAtlas.class.getClassLoader().getResourceAsStream(jsonPath);

        if (pngInputStream != null && jsonInputStream != null) {
            // Load Buffered Image:
            final BufferedImage bufferedAtlasImage = ImageIO.read(pngInputStream); // IO Exception

            if (bufferedAtlasImage.getWidth() > 2048 || bufferedAtlasImage.getHeight() > 2048) {
                throw new IllegalArgumentException("The image used by the sprite atlas should not exceed 2048x2048 in size.");
            }

            pngInputStream.close(); // IO Exception

            // Create the atlas, add it to the cache, and return it.
            final JSONParser parser = new JSONParser();
            final JSONObject atlasData = (JSONObject) parser.parse(new InputStreamReader(jsonInputStream));

            final SpriteAtlas atlas = new SpriteAtlas(bufferedAtlasImage, (JSONArray) atlasData.get("Sprite Atlas"));
            jsonInputStream.close();

            ATLAS_CACHE.put(hash, atlas);

            return atlas;
        }

        // Attempt to load from filesystem.
        final File pngFile = new File(pngPath);
        final File jsonFile = new File(jsonPath);

        if (!pngFile.exists()) {
            throw new FileNotFoundException("The PNG file '" + pngPath + "' does not exist.");
        }

        if (!jsonFile.exists()) {
            throw new FileNotFoundException("The JSON file '" + jsonPath + "' does not exist.");
        }

        if (pngFile.isDirectory()) {
            throw new FileNotFoundException("The PNG file path '" + pngPath + "' points to a directory.");
        }

        if (jsonFile.isDirectory()) {
            throw new FileNotFoundException("The JSON file path '" + jsonPath + "' points to a directory.");
        }

        pngInputStream = new FileInputStream(pngFile);
        jsonInputStream = new FileInputStream(jsonFile);

        // Load Buffered Image:
        final BufferedImage bufferedAtlasImage = ImageIO.read(pngInputStream); // IO Exception

        if (bufferedAtlasImage.getWidth() > 2048 || bufferedAtlasImage.getHeight() > 2048) {
            throw new IllegalArgumentException("The image used by the sprite atlas should not exceed 2048x2048 in size.");
        }

        pngInputStream.close(); // IO Exception

        // Create the atlas, add it to the cache, and return it.
        final JSONParser parser = new JSONParser();
        final JSONObject atlasData = (JSONObject) parser.parse(new InputStreamReader(jsonInputStream));

        final SpriteAtlas atlas = new SpriteAtlas(bufferedAtlasImage, (JSONArray) atlasData.get("Sheets"));
        jsonInputStream.close();

        ATLAS_CACHE.put(hash, atlas);
        return atlas;
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
     *          The sheet's name.
     *
     * @return
     *          The sheet, or null if no sprite with the name was found.
     */
    public SpriteSheet getSpriteSheet(final String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        return spriteSheets.get(name);
    }
}
