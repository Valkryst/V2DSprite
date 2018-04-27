package com.valkryst.V2DSprite;

import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SpriteAtlasTest {
    private FileInputStream atlasImageStream;
    private FileInputStream atlasJSONStream;

    @Before
    public void openStreams() throws FileNotFoundException {
        atlasImageStream = new FileInputStream("test_res/Atlas.png");
        atlasJSONStream = new FileInputStream("test_res/Atlas.json");
    }

    @After
    public void closeStreams() throws IOException {
        atlasImageStream.close();
        atlasJSONStream.close();
    }

    @Test
    public void testConstructor_withValidParams() throws IOException, ParseException {
        final SpriteAtlas atlas = new SpriteAtlas(atlasImageStream, atlasJSONStream);

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
    public void testConstructor_withNullImageStream() throws IOException, ParseException {
        new SpriteAtlas(null, atlasJSONStream);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructor_withNullJSONStream() throws IOException, ParseException {
        new SpriteAtlas(atlasImageStream, null);
    }

    @Test
    public void testGetAtlasImage() throws IOException, ParseException {
        final SpriteAtlas atlas = new SpriteAtlas(atlasImageStream, atlasJSONStream);
        Assert.assertNotNull(atlas.getAtlasImage());
    }

    @Test
    public void testGetSpriteSheet_withExistingSpriteSheet() throws IOException, ParseException {
        final SpriteAtlas atlas = new SpriteAtlas(atlasImageStream, atlasJSONStream);
        Assert.assertNotNull(atlas.getSpriteSheet("Player"));
    }

    @Test
    public void testGetSpriteSheet_withNonExistingSpriteSheet() throws IOException, ParseException {
        final SpriteAtlas atlas = new SpriteAtlas(atlasImageStream, atlasJSONStream);
        Assert.assertNull(atlas.getSpriteSheet("SomeRandomWords"));
    }
}
