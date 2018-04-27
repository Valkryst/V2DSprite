package com.valkryst.V2DSprite;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.VolatileImage;

public class SpriteAnimation {
    /** The sprite atlas. */
    private final SpriteAtlas spriteAtlas;

    /** The unique name of the animation. */
    @Getter private final String name;
    /** The top-left point of each frame within the sprite atlas. */
    private final Point[] framePositions;
    /** The width and height of each frame within the sprite atlas. */
    private final Dimension[] frameDimensions;

    /** The current frame of animation. */
    @Getter private int curFrame = 0;

    /** Whether to flip the sprite horizontally when drawing. */
    @Getter @Setter private boolean flippedHorizontally = false;
    /** Whether to flip the sprite vertically when drawing. */
    @Getter @Setter private boolean flippedVertically = false;

    /**
     * Constructs a new SpriteAnimation.
     *
     * @param spriteAtlas
     *          The sprite atlas.
     *
     * @param animData
     *          The JSON object to parse the animation data from.
     *
     * @throws NullPointerException
     *          If the atlas or data is null.
     */
    public SpriteAnimation(final @NonNull SpriteAtlas spriteAtlas, final @NonNull JSONObject animData) {
        this.spriteAtlas = spriteAtlas;
        name = (String) animData.get("Name");

        final JSONArray frameDataArray = (JSONArray) animData.get("Frames");
        framePositions = new Point[frameDataArray.size()];
        frameDimensions = new Dimension[frameDataArray.size()];

        for (int index = 0 ; index < frameDataArray.size() ; index++) {
            final JSONObject frameData = (JSONObject) frameDataArray.get(index);

            final int x = ((Number) frameData.get("x")).intValue();
            final int y = ((Number) frameData.get("y")).intValue();
            final int width = ((Number) frameData.get("width")).intValue();
            final int height = ((Number) frameData.get("height")).intValue();

            framePositions[index] = new Point(x, y);
            frameDimensions[index] = new Dimension(width, height);
        }
    }

    /**
     * Draws the current frame of animation on a graphics context.
     *
     * @param gc
     *          The graphics context.
     *
     * @param destination
     *          The top-left corner of the point, on the graphics context, where the frame is to be drawn.
     */
    public void draw(final Graphics gc, final Point destination) {
        if (gc == null || destination == null) {
            return;
        }

        final Point position = framePositions[curFrame];
        final Dimension dimension = frameDimensions[curFrame];

        final VolatileImage image = spriteAtlas.getAtlasImage();
        final int dx1;
        final int dy1;
        final int dx2;
        final int dy2;
        final int sx1 = position.x;
        final int sy1 = position.y;
        final int sx2 = sx1 + dimension.width;
        final int sy2 = sy1 + dimension.height;

        if (flippedHorizontally) {
            dx1 = destination.x + dimension.width;
            dx2 = destination.x;
        } else {
            dx1 = destination.x;
            dx2 = dx1 + dimension.width;
        }

        if (flippedVertically) {
            dy1 = destination.y + dimension.height;
            dy2 = destination.y;
        } else {
            dy1 = destination.y;
            dy2 = dy1 + dimension.height;
        }

        gc.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
    }

    /** Resets the animation. */
    public void reset() {
        curFrame = 0;
    }

    /**
     * Advances the frame counter by one frame.
     *
     * If the current frame is the last frame, then the frame counter will cycle to the first frame.
     */
    public void toNextFrame() {
        if (curFrame < framePositions.length - 1) {
            curFrame++;
        } else {
            curFrame = 0;
        }
    }

    /**
     * Retracts the frame counter by one frame.
     *
     * If the current frame is the first frame, then the frame counter will cycle to the last frame.
     */
    public void toPreviousFrame() {
        if (curFrame > 0) {
            curFrame--;
        } else {
            curFrame = framePositions.length - 1;
        }
    }

    /**
     * Retrieves the total number of frames.
     *
     * @return
     *          The total number of frames.
     */
    public int getTotalFrames() {
        return framePositions.length;
    }
}
