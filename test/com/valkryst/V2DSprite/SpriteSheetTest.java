package com.valkryst.V2DSprite;

import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class SpriteSheetTest {
    private final String pngFilePath = "test_res/Atlas.png";
    private final String jsonFilePath = "test_res/Atlas.json";

    @Test
    public void testConstructor_withValidParams() throws IOException, ParseException {
        final SpriteAtlas atlas = SpriteAtlas.createSpriteAtlas(pngFilePath, jsonFilePath);
        final SpriteSheet sheet = atlas.getSpriteSheet("Player");
        Assert.assertEquals("Player", sheet.getName());

        // Ensure Sprite Animation Exists & Data Is Correct
        Assert.assertNotNull(sheet.getAnimation("Standing"));

        final SpriteAnimation animation = sheet.getAnimation("Standing");
        Assert.assertEquals("Standing", animation.getName());
        Assert.assertEquals(3, animation.getTotalFrames());
    }

    @Test
    public void testGetAnimation_withExistingAnimation() throws IOException, ParseException {
        final SpriteAtlas atlas = SpriteAtlas.createSpriteAtlas(pngFilePath, jsonFilePath);
        final SpriteSheet sheet = atlas.getSpriteSheet("Player");
        Assert.assertNotNull(sheet.getAnimation("Standing"));
    }

    @Test
    public void testGetAnimation_WithNonExistingAnimation() throws IOException, ParseException {
        final SpriteAtlas atlas = SpriteAtlas.createSpriteAtlas(pngFilePath, jsonFilePath);
        final SpriteSheet sheet = atlas.getSpriteSheet("Player");
        Assert.assertNull(sheet.getAnimation("SomeRandomWords"));
    }
}
