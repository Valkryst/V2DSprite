package com.valkryst.V2DSprite;

import org.json.simple.parser.ParseException;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.io.FileInputStream;
import java.io.IOException;

public class Driver {
    public static void main(final String[] args) throws InterruptedException, IOException, ParseException {
        final Frame frame = new Frame();
        frame.setTitle("V2DSprite Test");
        frame.setResizable(false);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent e) {
                frame.dispose();
                System.exit(0);
            }
        });
        frame.setLocationRelativeTo(null);
        frame.setBackground(Color.MAGENTA);

        final Canvas canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(512, 512));
        canvas.setIgnoreRepaint(true);
        canvas.setBackground(Color.BLACK);

        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);

        // Load Atlas
        final FileInputStream atlasImageStream = new FileInputStream("test_res/Atlas.png");
        final FileInputStream atlasJSONStream = new FileInputStream("test_res/Atlas.json");
        final SpriteAtlas atlas = new SpriteAtlas(atlasImageStream, atlasJSONStream);

        final SpriteAnimation playerAnim = atlas.getSpriteSheet("Player").getAnimation("Standing");
        final SpriteAnimation enemyAnim = atlas.getSpriteSheet("Enemy").getAnimation("Standing");

        long lastLoopTime = System.nanoTime();
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;

        int spriteChangeCounter = 0;

        // keep looping round til the game ends
        while (true) {
            // work out how long its been since the last update, this
            // will be used to calculate how far the entities should
            // move this loop
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double delta = updateLength / ((double) OPTIMAL_TIME);


            BufferStrategy bs = canvas.getBufferStrategy();

            if (bs == null) {
                canvas.createBufferStrategy(2);
                bs = canvas.getBufferStrategy();
            }

            if (spriteChangeCounter == 30) {
                playerAnim.toNextFrame();
                enemyAnim.toNextFrame();
                spriteChangeCounter = 0;
            } else {
                spriteChangeCounter++;
            }

            do {
                do {
                    final Graphics2D gc = (Graphics2D) bs.getDrawGraphics();

                    gc.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
                    gc.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
                    gc.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                    gc.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

                    // Particles are rectangles, so no need for AA.
                    gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

                    // No-need for text rendering related options.
                    gc.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
                    gc.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

                    // If alpha is used, we want computations related to drawing with it to be fast.
                    gc.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);

                    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                    long bef = System.nanoTime();
                    playerAnim.draw(gc, new Point(0, 0));
                    enemyAnim.draw(gc, new Point(32, 0));
                    System.out.println("Draw Time: " + ((System.nanoTime() - bef) / 1000000.0) + "ms");

                    gc.dispose();
                }
                while (bs.contentsRestored()); // Repeat render if drawing buffer contents were restored.

                bs.show();
                Toolkit.getDefaultToolkit().sync();
            } while (bs.contentsLost()); // Repeat render if drawing buffer was lost.


            // we want each frame to take 10 milliseconds, to do this
            // we've recorded when we started the frame. We add 10 milliseconds
            // to this and then factor in the current time to give
            // us our final value to wait for
            // remember this is in ms, whereas our lastLoopTime etc. vars are in ns.
            try {
                Thread.sleep( (lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000 );
            } catch (final IllegalArgumentException ignored) {}
        }
    }
}
