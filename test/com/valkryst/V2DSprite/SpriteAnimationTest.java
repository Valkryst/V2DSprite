package com.valkryst.V2DSprite;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class SpriteAnimationTest {
    private final String pngFilePath = "test_res/Atlas.png";
    private final String jsonFilePath = "test_res/Atlas.json";

    @Test
    public void testConstructor_withValidParams() throws IOException, ParseException {
        final SpriteAtlas atlas = SpriteAtlas.createSpriteAtlas(pngFilePath, jsonFilePath);
        final SpriteSheet sheet = atlas.getSpriteSheet("Player");
        final SpriteAnimation animation = sheet.getAnimation("Standing");
        Assert.assertEquals("Standing", animation.getName());
        Assert.assertEquals(3, animation.getTotalFrames());
        Assert.assertEquals(0, animation.getCurFrame());
    }

    @Test
    public void testReset() throws IOException, ParseException {
        final SpriteAtlas atlas = SpriteAtlas.createSpriteAtlas(pngFilePath, jsonFilePath);
        final SpriteSheet sheet = atlas.getSpriteSheet("Player");
        final SpriteAnimation animation = sheet.getAnimation("Standing");
        Assert.assertEquals(3, animation.getTotalFrames());
        Assert.assertEquals(0, animation.getCurFrame());

        animation.toNextFrame();
        Assert.assertEquals(1, animation.getCurFrame());

        animation.reset();
        Assert.assertEquals(0, animation.getCurFrame());
    }

    @Test
    public void testToNextFrame() throws IOException, ParseException {
        final SpriteAtlas atlas = SpriteAtlas.createSpriteAtlas(pngFilePath, jsonFilePath);
        final SpriteSheet sheet = atlas.getSpriteSheet("Player");
        final SpriteAnimation animation = sheet.getAnimation("Standing");
        Assert.assertEquals(3, animation.getTotalFrames());
        Assert.assertEquals(0, animation.getCurFrame());

        animation.toNextFrame();
        Assert.assertEquals(1, animation.getCurFrame());

        animation.toNextFrame();
        Assert.assertEquals(2, animation.getCurFrame());

        animation.toNextFrame();
        Assert.assertEquals(0, animation.getCurFrame());
    }

    @Test
    public void testToPreviousFrame() throws IOException, ParseException {
        final SpriteAtlas atlas = SpriteAtlas.createSpriteAtlas(pngFilePath, jsonFilePath);
        final SpriteSheet sheet = atlas.getSpriteSheet("Player");
        final SpriteAnimation animation = sheet.getAnimation("Standing");
        Assert.assertEquals(3, animation.getTotalFrames());
        Assert.assertEquals(0, animation.getCurFrame());

        animation.toPreviousFrame();
        Assert.assertEquals(2, animation.getCurFrame());

        animation.toPreviousFrame();
        Assert.assertEquals(1, animation.getCurFrame());

        animation.toPreviousFrame();
        Assert.assertEquals(0, animation.getCurFrame());
    }

    @Test
    public void testTotalFrames() throws IOException, ParseException {
        final SpriteAtlas atlas = SpriteAtlas.createSpriteAtlas(pngFilePath, jsonFilePath);
        final SpriteSheet sheet = atlas.getSpriteSheet("Player");
        final SpriteAnimation animation = sheet.getAnimation("Standing");
        Assert.assertEquals(3, animation.getTotalFrames());
    }
}
