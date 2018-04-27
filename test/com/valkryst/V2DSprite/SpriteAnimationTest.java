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
    private SpriteAtlas atlas;
    private JSONObject animationJSONData;

    @Before
    public void loadAtlas() throws IOException, ParseException {
        final FileInputStream atlasImageStream = new FileInputStream("test_res/Atlas.png");
        FileInputStream atlasJSONStream = new FileInputStream("test_res/Atlas.json");
        atlas = new SpriteAtlas(atlasImageStream, atlasJSONStream);

        atlasJSONStream = new FileInputStream("test_res/Atlas.json");
        final JSONParser parser = new JSONParser();
        final JSONObject atlasData = (JSONObject) parser.parse(new InputStreamReader(atlasJSONStream));
        final JSONArray sheetDataArray = (JSONArray) atlasData.get("Sheets");
        final JSONObject spriteJSONData = (JSONObject) sheetDataArray.get(0);
        final JSONArray animDataArray = (JSONArray) spriteJSONData.get("Animations");
        animationJSONData = (JSONObject) animDataArray.get(0);
        atlasJSONStream.close();
    }

    @Test
    public void testConstructor_withValidParams() {
        final SpriteAnimation animation = new SpriteAnimation(atlas, animationJSONData);
        Assert.assertEquals("Standing", animation.getName());
        Assert.assertEquals(3, animation.getTotalFrames());
        Assert.assertEquals(0, animation.getCurFrame());
    }

    @Test(expected=NullPointerException.class)
    public void testConstructor_withNullAtlas() {
        new SpriteAnimation(null, animationJSONData);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructor_withNullAnimationData() {
        new SpriteAnimation(atlas, null);
    }

    @Test
    public void testReset() {
        final SpriteAnimation animation = new SpriteAnimation(atlas, animationJSONData);
        Assert.assertEquals(3, animation.getTotalFrames());
        Assert.assertEquals(0, animation.getCurFrame());

        animation.toNextFrame();
        Assert.assertEquals(1, animation.getCurFrame());

        animation.reset();
        Assert.assertEquals(0, animation.getCurFrame());
    }

    @Test
    public void testToNextFrame() {
        final SpriteAnimation animation = new SpriteAnimation(atlas, animationJSONData);
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
    public void testToPreviousFrame() {
        final SpriteAnimation animation = new SpriteAnimation(atlas, animationJSONData);
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
    public void testTotalFrames() {
        final SpriteAnimation animation = new SpriteAnimation(atlas, animationJSONData);
        Assert.assertEquals(3, animation.getTotalFrames());
    }
}
