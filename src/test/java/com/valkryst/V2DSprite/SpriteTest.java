package com.valkryst.V2DSprite;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.MissingFormatArgumentException;

public class SpriteTest {
    private final SpriteSheet spriteSheet;

    public SpriteTest() throws IOException {
        final var imagePath = SuiteHelper.getResourcePath("image.png");
        final var jsonPath = SuiteHelper.getResourcePath("data-valid.json");
        spriteSheet = new SpriteSheet(imagePath, jsonPath);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_withNullSpriteSheet() {
        final var jsonObject = new JSONObject();
        jsonObject.put("x", 0L);
        jsonObject.put("y", 0L);
        jsonObject.put("width", 1L);
        jsonObject.put("height", 1L);
        new Sprite(null, jsonObject);
    }

    @Test(expected = MissingFormatArgumentException.class)
    public void testConstructor_withMissingXValue() {
        final var jsonObject = new JSONObject();
        jsonObject.put("y", 0L);
        jsonObject.put("width", 1L);
        jsonObject.put("height", 0L);
        new Sprite(spriteSheet, jsonObject);
    }

    @Test(expected = MissingFormatArgumentException.class)
    public void testConstructor_withMissingYValue() {
        final var jsonObject = new JSONObject();
        jsonObject.put("x", 0L);
        jsonObject.put("width", 1L);
        jsonObject.put("height", 0L);
        new Sprite(spriteSheet, jsonObject);
    }

    @Test(expected = MissingFormatArgumentException.class)
    public void testConstructor_withMissingWidthValue() {
        final var jsonObject = new JSONObject();
        jsonObject.put("x", 0L);
        jsonObject.put("y", 0L);
        jsonObject.put("height", 0L);
        new Sprite(spriteSheet, jsonObject);
    }

    @Test(expected = MissingFormatArgumentException.class)
    public void testConstructor_withMissingHeightValue() {
        final var jsonObject = new JSONObject();
        jsonObject.put("x", 0L);
        jsonObject.put("y", 0L);
        jsonObject.put("width", 1L);
        new Sprite(spriteSheet, jsonObject);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_withXLessThanZero() {
        final var jsonObject = new JSONObject();
        jsonObject.put("x", -1L);
        jsonObject.put("y", 0L);
        jsonObject.put("width", 1L);
        jsonObject.put("height", 1L);
        new Sprite(spriteSheet, jsonObject);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_withYLessThanZero() {
        final var jsonObject = new JSONObject();
        jsonObject.put("x", 0L);
        jsonObject.put("y", -1L);
        jsonObject.put("width", 1L);
        jsonObject.put("height", 1L);
        new Sprite(spriteSheet, jsonObject);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_withWidthLessThanOne() {
        final var jsonObject = new JSONObject();
        jsonObject.put("x", 0L);
        jsonObject.put("y", 0L);
        jsonObject.put("width", 0L);
        jsonObject.put("height", 1L);
        new Sprite(spriteSheet, jsonObject);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_withHeightLessThanOne() {
        final var jsonObject = new JSONObject();
        jsonObject.put("x", 0L);
        jsonObject.put("y", 0L);
        jsonObject.put("width", 1L);
        jsonObject.put("height", 0L);
        new Sprite(spriteSheet, jsonObject);
    }

    @Test
    public void testToJson() {
        final var sprite = spriteSheet.getSpriteByName("Yellow");
        Assert.assertTrue(sprite.isPresent());

        final var jsonObject = sprite.get().toJson();
        Assert.assertTrue(jsonObject.has("x"));
        Assert.assertTrue(jsonObject.has("y"));
        Assert.assertTrue(jsonObject.has("width"));
        Assert.assertTrue(jsonObject.has("height"));
        Assert.assertEquals(0L, jsonObject.get("x"));
        Assert.assertEquals(0L, jsonObject.get("y"));
        Assert.assertEquals(32L, jsonObject.get("width"));
        Assert.assertEquals(32L, jsonObject.get("height"));
    }

    @Test
    public void testGetWidth() {
        final var sprite = spriteSheet.getSpriteByName("Yellow");
        Assert.assertTrue(sprite.isPresent());

        Assert.assertEquals((short) 32, sprite.get().getWidth());
    }

    @Test
    public void testGetHeight() {
        final var sprite = spriteSheet.getSpriteByName("Yellow");
        Assert.assertTrue(sprite.isPresent());

        Assert.assertEquals((short) 32, sprite.get().getHeight());
    }

    @Test
    public void testDraw() {
        if (SuiteHelper.isEnvironmentCircleCI()) {
            System.out.println("Skipping this test due to headless mode issues in CircleCI.");
            return;
        }

        final var sprite = spriteSheet.getSpriteByName("Yellow");
        Assert.assertTrue(sprite.isPresent());


        final var loadedImage = spriteSheet.getImage().getSnapshot();

        final var canvas = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        final var gc = (Graphics2D) canvas.getGraphics();

        sprite.get().draw(gc, new Point(0, 0));

        for (int y = 0 ; y < canvas.getWidth(); y++) {
            for (int x = 0 ; x < canvas.getHeight() ; x++) {
                Assert.assertEquals(canvas.getRGB(x, y), loadedImage.getRGB(x, y));
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void testDraw_withNullGraphicsContext() {
        final var sprite = spriteSheet.getSpriteByName("Yellow");
        Assert.assertTrue(sprite.isPresent());

        sprite.get().draw(null, new Point(0, 0));
    }

    @Test(expected = NullPointerException.class)
    public void testDraw_withNullPosition() {
        final var sprite = spriteSheet.getSpriteByName("Yellow");
        Assert.assertTrue(sprite.isPresent());

        final var canvas = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        final var gc = (Graphics2D) canvas.getGraphics();

        sprite.get().draw(gc, null);
    }

    @Test
    public void testDraw_withAffineTransform() throws IOException {
        final var imagePath = SuiteHelper.getResourcePath("affine-test-image.png");
        final var jsonPath = SuiteHelper.getResourcePath("affine-test-data.json");
        final var spriteSheet = new SpriteSheet(imagePath, jsonPath);

        final var sprite = spriteSheet.getSpriteByName("Test");
        Assert.assertTrue(sprite.isPresent());

        final var loadedImage = spriteSheet.getImage().getSnapshot();

        final var canvas = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        final var gc = (Graphics2D) canvas.getGraphics();

        final var transform = AffineTransform.getScaleInstance(-1, 1);
        transform.translate(-loadedImage.getWidth(null), 0);

        sprite.get().draw(gc, new Point(0, 0), transform);

        for (int y = 0 ; y < canvas.getWidth(); y++) {
            for (int x = 0 ; x < canvas.getHeight() ; x++) {
                if (x == canvas.getHeight() - 1) {
                    Assert.assertEquals(canvas.getRGB(x, y), Color.BLACK.getRGB());
                } else {
                    Assert.assertEquals(canvas.getRGB(x, y), Color.WHITE.getRGB());
                }
            }
        }
    }
}
