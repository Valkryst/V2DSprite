package com.valkryst.V2DSprite;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

@EqualsAndHashCode
public class SpriteSheet {
    /** The sprite atlas. */
    private final SpriteAtlas spriteAtlas;

    /** The unique name of the sprite sheet. */
    @Getter private final String name;
    /** The animation which is currently playing. */
    @Getter @Setter private SpriteAnimation currentAnimation;
    /** The animations supported by the sprite. */
    private final HashMap<String, JSONObject> animations = new HashMap<>();

    /**
     * Constructs a new SpriteSheet.
     *
     * @param spriteAtlas
     *          The sprite atlas.
     *
     * @param sheetData
     *          The JSON object to parse the sprite sheet data from.
     *
     * @throws NullPointerException
     *          If the atlas or data is null.
     */
    public SpriteSheet(final @NonNull SpriteAtlas spriteAtlas, final @NonNull JSONObject sheetData) {
        this.spriteAtlas = spriteAtlas;
        name = (String) sheetData.get("Name");

        final JSONArray animDataArray = (JSONArray) sheetData.get("Animations");
        ((ArrayList<JSONObject>) animDataArray).forEach(animData -> {
            animations.put(String.valueOf(animData.get("Name")), animData);
        });
    }

    /**
     * Retrieves a new instance of specific sprite animation.
     *
     * @param spriteName
     *          The name of the animation to retrieve.
     *
     * @return
     *          Either the animation, or null if the sprite name is null, empty, or if no animation using the
     *          specified name exists.
     */
    public SpriteAnimation getAnimation(final String spriteName) {
        if (spriteName == null || spriteName.isEmpty()) {
            return null;
        }

        final JSONObject animData = animations.get(spriteName);

        if (animData == null) {
            return null;
        } else {
            return new SpriteAnimation(spriteAtlas, animData);
        }
    }
}
