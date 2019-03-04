package com.valkryst.V2DSprite;

import com.valkryst.VJSON.VJSON;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;

public class AnimatedSprite {
    /** The animated sprite's name. */
    @Getter private final String name;

    /** The index of the current frame of animation. */
    private int currentFrame = 0;
    /** The sprites sprites that comprise the animation. */
    private final Sprite[] sprites;

    /** Whether to flip the sprite horizontally when drawing. */
    @Getter @Setter private boolean flippedHorizontally = false;
    /** Whether to flip the sprite vertically when drawing. */
    @Getter @Setter private boolean flippedVertically = false;

    /**
     * Constructs a new AnimatedSprite.
     *
     * @param atlas
     *          The sprite atlas.
     *
     * @param data
     *          A JSON object containing the sprite's data.
     */
    public AnimatedSprite(final @NonNull SpriteAtlas atlas, final @NonNull JSONObject data) {
        name = VJSON.getString(data, "Name");

        // Load sprites
        final JSONArray frameData = (JSONArray) data.get("Frames");
        sprites = new Sprite[frameData.size()];

        for (int i = 0 ; i < frameData.size() ; i++) {
            sprites[i] = new Sprite(atlas, (JSONObject) frameData.get(i));
        }
    }

    /**
     * Draws the current sprite on a graphics context.
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

        sprites[currentFrame].setFlippedHorizontally(flippedHorizontally);
        sprites[currentFrame].setFlippedVertically(flippedVertically);
        sprites[currentFrame].draw(gc, x, y);
    }

    /** Resets the animation */
    public void reset() {
        currentFrame = 0;
    }

    /**
     * Increments the frame counter by one.
     *
     * Loops around to the first frame if at the last frame.
     */
    public void toNextFrame() {
        if (currentFrame < sprites.length - 1) {
            currentFrame++;
        } else {
            currentFrame = 0;
        }
    }

    /**
     * Decrements the frame counter by one.
     *
     * Loops around to the last frame if at the first frame.
     */
    public void toPreviousFrame() {
        if (currentFrame > 0) {
            currentFrame--;
        } else {
            currentFrame = sprites.length - 1;
        }
    }

    /**
     * Attempts to set the frame counter, so that the sprite with a specific name is displayed.
     *
     * Does nothing if no sprite with the name is found.
     *
     * @param name
     *          The name.
     */
    public void toFrameWithName(final String name) {
        for (int i = 0 ; i < sprites.length ; i++) {
            final String temp = sprites[i].getName();

            if (temp == null) {
                continue;
            }

            if (temp.equals(name)) {
                currentFrame = i;
                break;
            }
        }
    }

    /**
     * Determines whether the animation is on its first frame.
     *
     * @return
     *          Whether the animation is on its first frame.
     */
    public boolean isOnFirstFrame() {
        return currentFrame == 0;
    }

    /**
     * Determines whether the animation is on its last frame.
     *
     * @return
     *          Whether the animation is on its last frame.
     */
    public boolean isOnLastFrame() {
        return currentFrame == sprites.length - 1;
    }

    /**
     * Retrieves the current sprite.
     *
     * @return
     *          The current sprite.
     */
    public Sprite getCurrentSprite() {
        return sprites[currentFrame];
    }

    /**
     * Retrieves the width of the current sprite.
     *
     * @return
     *          The width of the current sprite.
     */
    public int getCurrentWidth() {
        return sprites[currentFrame].getWidth();
    }

    /**
     * Retrieves the height of the current sprite
     * @return
     */
    public int getCurrentHeight() {
        return sprites[currentFrame].getHeight();
    }
}
