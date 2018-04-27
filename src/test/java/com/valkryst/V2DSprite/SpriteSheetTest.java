package com.valkryst.V2DSprite;

import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.MissingFormatArgumentException;

public class SpriteSheetTest {
    private final Path imagePath = SuiteHelper.getResourcePath("image.png");
    private final Path jsonPath = SuiteHelper.getResourcePath("data-valid.json");

    @Test
    public void testConstructor_withValidImageAndJsonPaths() throws IOException {
        final var spriteSheet = new SpriteSheet(imagePath, jsonPath);
        Assert.assertNotNull(spriteSheet.getSpriteByName("Yellow"));
        Assert.assertNotNull(spriteSheet.getSpriteByName("Green"));
        Assert.assertNotNull(spriteSheet.getSpriteByName("Teal"));
        Assert.assertNotNull(spriteSheet.getSpriteByName("Magenta"));
        Assert.assertNotNull(spriteSheet.getSpriteByName("Orange"));
        Assert.assertNotNull(spriteSheet.getSpriteByName("Red"));
    }

    @Test(expected = FileNotFoundException.class)
    public void testConstructor_withNonExistentImagePath() throws IOException {
        new SpriteSheet(SuiteHelper.getResourcePath("fake_image.png"), jsonPath);
    }

    @Test(expected = FileNotFoundException.class)
    public void testConstructor_withNonExistentJsonPath() throws IOException {
        new SpriteSheet(imagePath, SuiteHelper.getResourcePath("fake_data.json"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_withFolderAtImagePath() throws IOException {
        new SpriteSheet(SuiteHelper.getResourcePath(), jsonPath);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_withFolderAtJsonPath() throws IOException {
        new SpriteSheet(imagePath, SuiteHelper.getResourcePath());
    }

    @Test(expected = MissingFormatArgumentException.class)
    public void testConstructor_withSpriteJsonThatIsMissingANameValue() throws IOException {
        new SpriteSheet(imagePath, SuiteHelper.getResourcePath("data-missing-name.json"));
    }

    @Test
    public void testGetImage() throws IOException {
        final var spriteSheet = new SpriteSheet(imagePath, jsonPath);

        final var originalImage = ImageIO.read(Files.newInputStream(imagePath));
        final var volatileImage = spriteSheet.getImage();

        Assert.assertNotNull(originalImage);
        Assert.assertNotNull(volatileImage);
        Assert.assertEquals(originalImage.getHeight(), volatileImage.getHeight());
        Assert.assertEquals(originalImage.getWidth(), volatileImage.getWidth());

        final var loadedImage = volatileImage.getSnapshot();

        for (int y = 0 ; y < originalImage.getHeight() ; y++) {
            for (int x = 0 ; x < originalImage.getWidth() ; x++) {
                Assert.assertEquals(originalImage.getRGB(x, y), loadedImage.getRGB(x, y));
            }
        }
    }

    @Test
    public void testGetSpriteByName_withExistingSprite() throws IOException {
        final var spriteSheet = new SpriteSheet(imagePath, jsonPath);
        final var optional = spriteSheet.getSpriteByName("Yellow");
        Assert.assertTrue(optional.isPresent());
    }

    @Test
    public void testGetSpriteByName_withMultiCaseName() throws IOException {
        final var spriteSheet = new SpriteSheet(imagePath, jsonPath);
        final var optional = spriteSheet.getSpriteByName("YeLlOw");
        Assert.assertTrue(optional.isPresent());
    }

    @Test
    public void testGetSpriteByName_withNonExistentSprite() throws IOException {
        final var spriteSheet = new SpriteSheet(imagePath, jsonPath);
        final var optional = spriteSheet.getSpriteByName("Test");
        Assert.assertFalse(optional.isPresent());
    }
}
