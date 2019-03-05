package com.valkryst.V2DSprite;

import com.valkryst.VJSON.VJSON;
import lombok.Getter;
import lombok.NonNull;
import org.json.simple.JSONObject;

import java.awt.*;

public class BoundingBox {
    /** The bounding box's name. */
    @Getter private final String name;

    /** The rectangle representing the bounding box within a sprite. */
    @Getter private final Rectangle bounds;

    /**
     * Constructs a new BoundingBox.
     *
     * @param data
     *          A JSON object containing the bounding box's data.
     */
    public BoundingBox(final @NonNull JSONObject data) {
        name = VJSON.getString(data, "Name");

        final int x = VJSON.getInt(data, "x");
        final int y = VJSON.getInt(data, "y");
        final int width = VJSON.getInt(data, "width");
        final int height = VJSON.getInt(data, "height");
        bounds = new Rectangle(x, y, width, height);
    }

    /**
     * Draws the bounding box on a graphics context.
     *
     * @param gc
     *          The graphics context.
     *
     * @param xOffset
     *          The x-axis position of the sprite, associated with the bounding box, on the graphics context.
     *
     * @param yOffset
     *          The y-axis position of the sprite, associated with the bounding box, on the graphics context.
     */
    public void draw(final Graphics2D gc, final int xOffset, final int yOffset) {
        gc.drawRect(xOffset + bounds.x, yOffset + bounds.y, bounds.width, bounds.height);
    }

    /**
     * Determines whether the this bounding box intersects another.
     *
     * @param other
     *          The other bounding box.
     *
     * @return
     *          Whether this bounding box intersects another.
     */
    public boolean intersects(final BoundingBox other) {
        return other.getBounds().intersects(bounds);
    }

    /**
     * Determines whether the this bounding box intersects another.
     *
     * @param other
     *          The other bounding box.
     *
     * @return
     *          Whether this bounding box intersects another.
     */
    public boolean intersects(final Rectangle other) {
        return other.intersects(bounds);
    }
}
