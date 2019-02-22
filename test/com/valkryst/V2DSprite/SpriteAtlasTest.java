package com.valkryst.V2DSprite;

import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class SpriteAtlasTest {
    private final String pngFilePath = "test_res/Atlas.png";
    private final String jsonFilePath = "test_res/Atlas.json";

    @Test
    public void testCreateSpriteAtlas_withValidParams() throws IOException, ParseException {
        final SpriteAtlas atlas = SpriteAtlas.createSpriteAtlas(pngFilePath, jsonFilePath);

        // Ensure Image Exists
        Assert.assertNotNull(atlas.getAtlasImage());


        // Validate Player Sprite Sheet & Animations
        Assert.assertNotNull(atlas.getSpriteSheet("Player"));

        SpriteSheet sheet = atlas.getSpriteSheet("Player");
        Assert.assertEquals("Player", sheet.getName());

        Assert.assertNotNull(sheet.getAnimation("Standing"));
        Assert.assertNotNull(sheet.getAnimation("Sitting"));

        SpriteAnimation animation = sheet.getAnimation("Standing");
        Assert.assertEquals("Standing", animation.getName());
        Assert.assertEquals(3, animation.getTotalFrames());

        animation = sheet.getAnimation("Sitting");
        Assert.assertEquals("Sitting", animation.getName());
        Assert.assertEquals(3, animation.getTotalFrames());


        // Validate enemy Sprite Sheet & Animations
        Assert.assertNotNull(atlas.getSpriteSheet("Enemy"));

        sheet = atlas.getSpriteSheet("Enemy");
        Assert.assertEquals("Enemy", sheet.getName());

        Assert.assertNotNull(sheet.getAnimation("Standing"));
        Assert.assertNotNull(sheet.getAnimation("Sitting"));

        animation = sheet.getAnimation("Standing");
        Assert.assertEquals("Standing", animation.getName());
        Assert.assertEquals(3, animation.getTotalFrames());

        animation = sheet.getAnimation("Sitting");
        Assert.assertEquals("Sitting", animation.getName());
        Assert.assertEquals(3, animation.getTotalFrames());
    }

    @Test(expected=NullPointerException.class)
    public void testCreateSpriteAtlas_withNullImagePath() throws IOException, ParseException {
        SpriteAtlas.createSpriteAtlas(null, jsonFilePath);
    }

    @Test(expected=NullPointerException.class)
    public void testCreateSpriteAtlas_withNullJSONPath() throws IOException, ParseException {
        SpriteAtlas.createSpriteAtlas(pngFilePath, null);
    }

    @Test
    public void testGetAtlasImage() throws IOException, ParseException {
        final SpriteAtlas atlas = SpriteAtlas.createSpriteAtlas(pngFilePath, jsonFilePath);
        Assert.assertNotNull(atlas.getAtlasImage());
    }

    @Test
    public void testGetSpriteSheet_withExistingSpriteSheet() throws IOException, ParseException {
        final SpriteAtlas atlas = SpriteAtlas.createSpriteAtlas(pngFilePath, jsonFilePath);
        Assert.assertNotNull(atlas.getSpriteSheet("Player"));
    }

    @Test
    public void testGetSpriteSheet_withNonExistingSpriteSheet() throws IOException, ParseException {
        final SpriteAtlas atlas = SpriteAtlas.createSpriteAtlas(pngFilePath, jsonFilePath);
        Assert.assertNull(atlas.getSpriteSheet("SomeRandomWords"));
    }
}
