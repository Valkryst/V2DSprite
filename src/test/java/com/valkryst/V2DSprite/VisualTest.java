package com.valkryst.V2DSprite;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;

public class VisualTest {
    public static void main(final String[] args) throws IOException {
        final Canvas canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(512, 512));


        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(canvas);
        frame.pack();
        frame.revalidate();
        frame.setVisible(true);

        final Path imagePath = SuiteHelper.getResourcePath("image.png");
        final Path jsonPath = SuiteHelper.getResourcePath("data-valid.json");
        final SpriteSheet spriteSheet = new SpriteSheet(imagePath, jsonPath);

        final Graphics2D gc = (Graphics2D) canvas.getGraphics();
        spriteSheet.getSpriteByName("Yellow").get().draw(gc, new Point(0, 0));
        spriteSheet.getSpriteByName("Green").get().draw(gc, new Point(32, 32));
        spriteSheet.getSpriteByName("Teal").get().draw(gc, new Point(64, 64));
        spriteSheet.getSpriteByName("Magenta").get().draw(gc, new Point(96, 96));
        spriteSheet.getSpriteByName("Orange").get().draw(gc, new Point(128, 128));
        spriteSheet.getSpriteByName("Red").get().draw(gc, new Point(160, 160));
    }
}
