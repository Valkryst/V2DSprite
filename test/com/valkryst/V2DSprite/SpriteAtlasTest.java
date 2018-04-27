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

        // Ensure Sprite Sheet Exists
        Assert.assertNotNull(atlas.getSpriteSheet("Player"));
    }

    @Test(expected=NullPointerException.class)
    public void testConstructor_withNullImageStream() throws IOException, ParseException {
        new SpriteAtlas(null, atlasJSONStream);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructor_withNullJSONStream() throws IOException, ParseException {
        new SpriteAtlas(atlasImageStream, null);
    }
}
