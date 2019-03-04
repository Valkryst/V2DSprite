package com.valkryst.V2DSprite;

import lombok.NonNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;

public class SpriteSheet {
    /** The sprites contained in the sheet. */
    private final HashMap<String, Sprite> sprites = new HashMap<>(0);
    /** The animated sprites contained in the sheet. */
    private final HashMap<String, AnimatedSprite> animatedSprites = new HashMap<>(0);

    public SpriteSheet(final @NonNull SpriteAtlas atlas, final @NonNull JSONArray dataArray) {
        JSONObject data;
        for (int i = 0 ; i < dataArray.size() ; i++) {
            data = (JSONObject) dataArray.get(i);

            if (data.get("Data") != null) {
                final Sprite sprite = new Sprite(atlas, data);
                final String name = sprite.getName();

                if (sprites.containsValue(name)) {
                    throw new IllegalStateException("The sprite sheet has two sprites with the same name ('" + name + "').");
                }

                if (animatedSprites.containsValue(name)) {
                    throw new IllegalStateException("The sprite sheet has a sprite and an animated sprite with the same name ('" + name + "').");
                }

                sprites.put(name, sprite);
            } else if (data.get("Frames") != null) {
                final AnimatedSprite sprite = new AnimatedSprite(atlas, data);
                final String name = sprite.getName();

                if (sprites.containsValue(name)) {
                    throw new IllegalStateException("The sprite sheet has a sprite and an animated sprite with the same  ('" + name + "').");
                }

                if (animatedSprites.containsValue(name)) {
                    throw new IllegalStateException("The sprite sheet has two sprites with the same name ('" + name + "').");
                }

                animatedSprites.put(name, sprite);
            } else {
                throw new IllegalStateException("Unable to determine if the following data represents a Sprite or AnimatedSprite.\n" + data);
            }
        }
    }

    /**
     * Retrieves a specific sprite.
     *
     * @param name
     *          The sprite's name.
     *
     * @return
     *          The sprite, or null if no sprite with the name was found.
     */
    public Sprite getSprite(final String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        return sprites.get(name);
    }

    /**
     * Retrieves a specific animated sprite.
     *
     * @param name
     *          The sprite's name.
     *
     * @return
     *          The sprite, or null if no sprite with the name was found.
     */
    public AnimatedSprite getAnimatedSprite(final String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        return animatedSprites.get(name);
    }
}
