package com.valkryst.V2DSprite;

import org.json.simple.parser.ParseException;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        frame.setBackground(Color.MAGENTA);

        final Canvas canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(1024, 1024));
        canvas.setIgnoreRepaint(true);
        canvas.setBackground(Color.BLACK);

        frame.add(canvas);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Load Atlas
        final FileInputStream atlasImageStream = new FileInputStream("test_res/Atlas.png");
        final FileInputStream atlasJSONStream = new FileInputStream("test_res/Atlas.json");
        final SpriteAtlas atlas = new SpriteAtlas(atlasImageStream, atlasJSONStream);

        final List<SpriteAnimation> animations = new ArrayList<>();
        int tmp = 0;

        for (int i = 0 ; i < 1024 ; i++) {
            switch (tmp) {
                case 0: {
                    animations.add(atlas.getSpriteSheet("Player").getAnimation("Standing"));
                    break;
                }
                case 1: {
                    animations.add(atlas.getSpriteSheet("Enemy").getAnimation("Standing"));
                    break;
                }
            }

            tmp++;
            if (tmp > 1) {
                tmp = 0;
            }
        }

        // Prepare Draw Time Variables
        double totalDrawTime = 0;
        long draws = 0;

        // Prepare Loop Variables
        long lastLoopTime = System.nanoTime();
        final int targetFPS = 60;
        final double optimalTime = 1000000000.0 / targetFPS;

        int spriteChangeCounter = 0;

        while (true) {
            final long now = System.nanoTime();
            final double delta = (now - lastLoopTime) / optimalTime;
            lastLoopTime = now;


            BufferStrategy bs = canvas.getBufferStrategy();

            if (bs == null) {
                canvas.createBufferStrategy(2);
                bs = canvas.getBufferStrategy();
            }

            if (spriteChangeCounter == 10) {
                animations.forEach(SpriteAnimation::toNextFrame);
                spriteChangeCounter = 0;
            } else {
                spriteChangeCounter++;
            }

            do {
                do {
                    final Graphics2D gc = (Graphics2D) bs.getDrawGraphics();

                    // Whether to bias algorithm choices more for speed or quality when evaluating tradeoffs.
                    gc.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

                    // Controls how closely to approximate a color when storing into a destination with limited
                    // color resolution.
                    gc.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);

                    // Controls the accuracy of approximation and conversion when storing colors into a
                    // destination image or surface.
                    gc.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);

                    // Controls how image pixels are filtered or resampled during an image rendering operation.
                    gc.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

                    // Controls whether or not the geometry rendering methods of a Graphics2D object will
                    // attempt to reduce aliasing artifacts along the edges of shapes.
                    gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

                    // Everything is done VIA images, so there's no need for text rendering options.
                    gc.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
                    gc.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

                    // It's possible that users will be using a large amount of transparent layers, so we want
                    // to ensure that rendering is as quick as we can get it.
                    //
                    // A general hint that provides a high level recommendation as to whether to bias alpha
                    // blending algorithm choices more for speed or quality when evaluating tradeoffs.
                    gc.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);

                    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                    long bef = System.nanoTime();

                    int x = 0, y = 0;
                    for (final SpriteAnimation animation : animations) {
                        animation.draw(gc, new Point(x * 32, y * 32));
                        x++;

                        if (x >= 32) {
                            x = 0;
                            y++;
                        }
                    }

                    totalDrawTime += (System.nanoTime() - bef) / 1000000.0;
                    draws++;

                    if (draws % 60 == 0) {
                        System.out.println("Avg. Draw Time: " + (totalDrawTime / draws) + "ms\tCurrent Delta Time: " + delta);
                    }

                    gc.dispose();
                }

                while (bs.contentsRestored()); // Repeat render if drawing buffer contents were restored.

                bs.show();
            } while (bs.contentsLost()); // Repeat render if drawing buffer was lost.


            // we want each frame to take 10 milliseconds, to do this
            // we've recorded when we started the frame. We add 10 milliseconds
            // to this and then factor in the current time to give
            // us our final value to wait for
            // remember this is in ms, whereas our lastLoopTime etc. vars are in ns.
            try {
                Thread.sleep((long) ((lastLoopTime-System.nanoTime() + optimalTime) / 1000000));
            } catch (final IllegalArgumentException ignored) {}
        }
    }
}
