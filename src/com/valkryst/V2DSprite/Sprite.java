package com.valkryst.V2DSprite;

import com.valkryst.VJSON.VJSON;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.awt.image.VolatileImage;

public class Sprite {
    /** The sprite atlas. */
    private final SpriteAtlas atlas;

    /** The sprite's name. */
    @Getter private final String name;

    /** The position of the top-left point of the sprite within the atlas. */
    private final Point position;
    /** The sprite's dimensions. */
    private final Dimension dimensions;

    /** The sprite's bounding boxes. */
    private final BoundingBox[] boundingBoxes;

    /** Whether to flip the sprite horizontally when drawing. */
    @Getter @Setter private boolean flippedHorizontally = false;
    /** Whether to flip the sprite vertically when drawing. */
    @Getter @Setter private boolean flippedVertically = false;

    /**
     * Constructs a new Sprite.
     *
     * @param atlas
     *          The sprite atlas.
     *
     * @param data
     *          A JSON object containing the sprite's data.
     */
    public Sprite(final @NonNull SpriteAtlas atlas, final @NonNull JSONObject data) {
        this.atlas = atlas;

        name = VJSON.getString(data, "Name");

        // Load position and dimensions
        final int x = VJSON.getInt(data, "x");
        final int y = VJSON.getInt(data, "y");
        final int width = VJSON.getInt(data, "width");
        final int height = VJSON.getInt(data, "height");

        position = new Point(x, y);
        dimensions = new Dimension(width, height);

        // Load bounding boxes.
        final JSONArray boundingBoxData = (JSONArray) data.get("Bounding Boxes");

        if (boundingBoxData == null) {
            boundingBoxes = new BoundingBox[0];
        } else {
            boundingBoxes = new BoundingBox[boundingBoxData.size()];

            for (int i = 0; i < boundingBoxData.size(); i++) {
                boundingBoxes[i] = new BoundingBox((JSONObject) boundingBoxData.get(i));
            }
        }

        // Ensure bounding boxes have unique names.
        for (int i = 0 ; i < boundingBoxes.length - 1 ; i++) {
            for (int k = i + 1 ; k < boundingBoxes.length ; k++) {
                final String nameA = boundingBoxes[i].getName();
                final String nameB = boundingBoxes[k].getName();

                if (nameA.equals(nameB)) {
                    throw new IllegalStateException("The sprite " + name + " has two bounding boxes with the name name ('" + nameA + "').");
                }
            }
        }
    }

    /**
     * Draws the sprite on a graphics context.
     *
     * @param gc
     *          The graphics context.
     *
     * @param x
     *          The x-axis position of the top-left corner of the point, on the graphics context, where the
     *          sprite is to be drawn.
     *
     * @param y
     *          The y-axis position of the top-left corner of the point, on the graphics context, where the
     *          sprite is to be drawn.
     */
    public void draw(final Graphics2D gc, final int x, final int y) {
        if (gc == null) {
            return;
        }

        final VolatileImage image = atlas.getAtlasImage();
        final int dx1;
        final int dy1;
        final int dx2;
        final int dy2;
        final int sx1 = position.x;
        final int sy1 = position.y;
        final int sx2 = sx1 + dimensions.width;
        final int sy2 = sy1 + dimensions.height;

        if (flippedHorizontally) {
            dx1 = x + dimensions.width;
            dx2 = x;
        } else {
            dx1 = x;
            dx2 = dx1 + dimensions.width;
        }

        if (flippedVertically) {
            dy1 = y + dimensions.height;
            dy2 = y;
        } else {
            dy1 = y;
            dy2 = dy1 + dimensions.height;
        }

        gc.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
    }

    public void drawBoundingBox(final Graphics2D gc, final String name, final int xOffset, final int yOffset) {
        if (gc == null || name == null) {
            return;
        }

        final BoundingBox boundingBox = getBoundingBox(name);

        if (boundingBox == null) {
            return;
        }

        final Rectangle bounds = getBoundingBox(name).getBounds();
        int x = xOffset;
        int y = yOffset;

        if (flippedVertically && flippedHorizontally) {
            x -= dimensions.width;
            y -= dimensions.height;
        } else if (flippedVertically) {
            y -= dimensions.height;
        } else if (flippedHorizontally) {
            x += dimensions.width;
            x -= bounds.width;
            x -= bounds.x;
        } else {
            x += bounds.x;
            y += bounds.y;
        }

        gc.drawRect(x, y, bounds.width, bounds.height);
    }

    /**
     * Retrieves the sprite's width.
     *
     * @return
     *          The sprite's width.
     */
    public int getWidth() {
        return dimensions.width;
    }

    /**
     * Retrieves the sprite's height.
     *
     * @return
     *          The sprite's height.
     */
    public int getHeight() {
        return dimensions.height;
    }

    /**
     * Retrieves a bounding box by name.
     *
     * @param name
     *          The name of the bounding box to retrieve.
     *
     * @return
     *          The bounding box, or null if no bounding box with the specified name could be found.
     */
    public BoundingBox getBoundingBox(final String name) {
        for (final BoundingBox boundingBox : boundingBoxes) {
            if (boundingBox.getName().equals(name)) {
                return boundingBox;
            }
        }

        return null;
    }
}
