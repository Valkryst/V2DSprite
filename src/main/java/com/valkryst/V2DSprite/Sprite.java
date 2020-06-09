package com.valkryst.V2DSprite;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.MissingFormatArgumentException;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.json.JSONObject;

public class Sprite {
    /** The SpriteSheet containing this sprite. */
    private final SpriteSheet spriteSheet;

    /** The x-axis position of the top-left pixel, within the sprite sheet. */
    private final int x;
    /** The y-axis position of the top-left pixel, within the sprite sheet. */
    private final int y;

    /** The width. */
    @Getter private final short width;
    /** The height. */
    @Getter private final short height;

    /**
     * Whether the sprite should be drawn using a
     * {@link java.awt.image.VolatileImage} or a
     * {@link java.awt.image.BufferedImage}.
     */
    @Setter private boolean useVolatileImage = true;

    /**
     * Constructs a new sprite.
     *
     * @param spriteSheet
     *          The sprite sheet containing this sprite.
     *
     * @param jsonObject
     *          The JSON definition of this sprite.
     */
    public Sprite(final @NonNull SpriteSheet spriteSheet, final @NonNull JSONObject jsonObject) {
        if (!jsonObject.has("x")) {
            throw new MissingFormatArgumentException("There is no x-axis coordinate value.");
        }

        if (!jsonObject.has("y")) {
            throw new MissingFormatArgumentException("There is no y-axis coordinate value.");
        }

        if (!jsonObject.has("width")) {
            throw new MissingFormatArgumentException("There is no width value.");
        }

        if (!jsonObject.has("height")) {
            throw new MissingFormatArgumentException("There is no height value.");
        }

        this.spriteSheet = spriteSheet;
        x = jsonObject.getInt("x");
        y = jsonObject.getInt("y");
        width = (short) jsonObject.getInt("width");
        height = (short) jsonObject.getInt("height");

        if (x < 0) {
            throw new IllegalArgumentException("The x-axis coordinate cannot be a negative number.");
        }

        if (y < 0) {
            throw new IllegalArgumentException("The y-axis coordinate cannot be a negative number.");
        }

        if (height < 1) {
            throw new IllegalArgumentException("The height must be a positive number.");
        }

        if (width < 1) {
            throw new IllegalArgumentException("The height must be a positive number.");
        }
    }

    /**
     * Constructs a JSON representation of this sprite.
     *
     * @return
     *          The JSON representation of this sprite.
     */
    public JSONObject toJson() {
        final JSONObject object = new JSONObject();
        object.put("x", (long) x);
        object.put("y", (long) y);
        object.put("width", (long) width);
        object.put("height", (long) height);
        return object;
    }

    /**
     * Draws this sprite on a graphics context.
     *
     * @param gc
     *          The graphics context.
     *
     * @param position
     *          The position, on the graphics context, to draw this sprite.
     */
    public void draw(final @NonNull Graphics2D gc, final @NonNull Point position) {
        final var image = useVolatileImage ? spriteSheet.getImage() : spriteSheet.getBufferedImage();
        gc.drawImage(image, position.x, position.y, position.x + width, position.y + height, x, y, x + width, y + height, null);
    }

    /**
     * Draws this sprite on the graphics context, after applying an affine transformation.
     *
     * @param gc
     *          The graphics context.
     *
     * @param position
     *          The position, on the graphics context, to draw this sprite.
     *
     * @param transform
     *          The affine transformation to apply to this sprite before drawing it.
     */
    public void draw(final Graphics2D gc, final Point position, final AffineTransform transform) {
        final var originalTransform = gc.getTransform();
        gc.setTransform(transform);
        draw(gc, position);
        gc.setTransform(originalTransform);
    }
}
