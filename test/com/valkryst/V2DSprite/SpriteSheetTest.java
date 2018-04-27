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

public class SpriteSheetTest {
    private SpriteAtlas atlas;
    private JSONObject spriteJSONData;

    @Before
    public void loadAtlas() throws IOException, ParseException {
        final FileInputStream atlasImageStream = new FileInputStream("test_res/Atlas.png");
        FileInputStream atlasJSONStream = new FileInputStream("test_res/Atlas.json");
        atlas = new SpriteAtlas(atlasImageStream, atlasJSONStream);

        atlasJSONStream = new FileInputStream("test_res/Atlas.json");
        final JSONParser parser = new JSONParser();
        final JSONObject atlasData = (JSONObject) parser.parse(new InputStreamReader(atlasJSONStream));
        final JSONArray sheetDataArray = (JSONArray) atlasData.get("Sheets");
        spriteJSONData = (JSONObject) sheetDataArray.get(0);
        atlasJSONStream.close();
    }

    @Test
    public void testConstructor_withValidParams() {
        final SpriteSheet sheet = new SpriteSheet(atlas, spriteJSONData);
        Assert.assertEquals("Player", sheet.getName());

        // Ensure Sprite Animation Exists & Data Is Correct
        Assert.assertNotNull(sheet.getAnimation("Standing"));

        final SpriteAnimation animation = sheet.getAnimation("Standing");
        Assert.assertEquals("Standing", animation.getName());
        Assert.assertEquals(3, animation.getTotalFrames());
    }

    @Test(expected=NullPointerException.class)
    public void testConstructor_withNullAtlas() {
        new SpriteSheet(null, spriteJSONData);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructor_withNullSheetData() {
        new SpriteSheet(atlas, null);
    }

    @Test
    public void testGetAnimation_withExistingAnimation() {
        final SpriteSheet sheet = new SpriteSheet(atlas, spriteJSONData);
        Assert.assertNotNull(sheet.getAnimation("Standing"));
    }

    @Test
    public void testGetAnimation_WithNonExistingAnimation() {
        final SpriteSheet sheet = new SpriteSheet(atlas, spriteJSONData);
        Assert.assertNull(sheet.getAnimation("SomeRandomWords"));
    }
}
