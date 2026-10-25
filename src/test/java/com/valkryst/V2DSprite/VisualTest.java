package com.valkryst.V2DSprite;

import com.valkryst.V2DSprite.event.AnimationEvent;
import com.valkryst.V2DSprite.listener.AnimationEventListener;
import lombok.NonNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class VisualTest {
    /** Pixel scale factor applied when drawing the sprite. 32×32 → 256×256 on screen. */
    private static final int SCALE = 8;

    private static final int SPRITE_W = 32;
    private static final int SPRITE_H = 32;
    private static final int SCALED_W = SPRITE_W * SCALE;
    private static final int SCALED_H = SPRITE_H * SCALE;

    private static final int MAX_LOG_ENTRIES = 16;

    public static void main(final String[] args) {
        final var animation = new Animation("valid", "idle");

        final var currentFrameIndex = new AtomicInteger(0);
        final Deque<String> eventLog = new ArrayDeque<>(MAX_LOG_ENTRIES);
        final var lastFrameChangeNanos = new AtomicLong(System.nanoTime());

        animation.addAnimationEventListener(new AnimationEventListener() {
            @Override
            public void onFirstFrame(final @NonNull AnimationEvent event) {
                currentFrameIndex.set(event.getIndex());
                lastFrameChangeNanos.set(System.nanoTime());
                addLog(eventLog, "FIRST_FRAME  → " + event.getIndex());
            }

            @Override
            public void onNewFrame(final @NonNull AnimationEvent event) {
                currentFrameIndex.set(event.getIndex());
                lastFrameChangeNanos.set(System.nanoTime());
                addLog(eventLog, "NEW_FRAME    → " + event.getIndex());
            }

            @Override
            public void onLastFrame(final @NonNull AnimationEvent event) {
                currentFrameIndex.set(event.getIndex());
                lastFrameChangeNanos.set(System.nanoTime());
                addLog(eventLog, "LAST_FRAME   → " + event.getIndex());
            }

            @Override
            public void onPause(final @NonNull AnimationEvent event) {
                addLog(eventLog, "PAUSE");
            }

            @Override
            public void onResume(final @NonNull AnimationEvent event) {
                addLog(eventLog, "RESUME");
            }
        });

        final var panel = new JPanel() {
            @Override
            protected void paintComponent(final Graphics g) {
                super.paintComponent(g);
                final var gc = (Graphics2D) g;
                gc.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
                );
                render(gc, animation, currentFrameIndex, lastFrameChangeNanos, eventLog);
            }
        };
        panel.setPreferredSize(new Dimension(512, 512));
        panel.setFocusable(true);

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE -> animation.setPaused(!animation.isPaused());
                    case KeyEvent.VK_R -> animation.setReversed(!animation.isReversed());
                    case KeyEvent.VK_ENTER -> animation.reset();
                    case KeyEvent.VK_EQUALS, KeyEvent.VK_PLUS ->
                        animation.setSpeed(Math.min(4.0, animation.getSpeed() + 0.25));
                    case KeyEvent.VK_MINUS ->
                        animation.setSpeed(Math.max(0.25, animation.getSpeed() - 0.25));
                }
            }
        });

        final var window = new JFrame();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.add(panel);
        window.pack();
        window.revalidate();
        window.setVisible(true);
        panel.requestFocusInWindow();

        // 100 ms ticks → deltaTime = 0.1 s. Each 1000 ms frame advances after 10 ticks.
        final var executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            animation.update(0.1);
            panel.repaint();
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    private static void render(
        final Graphics2D gc,
        final Animation animation,
        final AtomicInteger currentFrameIndex,
        final AtomicLong lastFrameChangeNanos,
        final Deque<String> eventLog
    ) {
        // Clear canvas.
        gc.setColor(new Color(40, 40, 40));
        gc.fillRect(0, 0, SCALED_W + 256, SCALED_H + 256);

        // 1. Checkerboard background behind the sprite area.
        drawCheckerboard(gc);

        // 2. Draw animation at SCALE× zoom.
        final AffineTransform savedTransform = gc.getTransform();
        final Shape savedClip = gc.getClip();
        gc.setTransform(AffineTransform.getScaleInstance(SCALE, SCALE));
        gc.setClip(0, 0, SPRITE_W, SPRITE_H);
        animation.draw(gc);
        gc.setTransform(savedTransform);
        gc.setClip(savedClip);

        // 3. Collision box overlay (green) and hitbox overlay (red).
        // Box coordinates are in sprite-sheet space; subtract the frame origin to get screen-relative coords.
        final var cf = animation.getCurrentFrame();
        final var collisionBox = animation.getCurrentCollisionBox();
        if (collisionBox != null) {
            drawOverlayRect(gc,
                (collisionBox.x() - cf.x()) * SCALE,
                (collisionBox.y() - cf.y()) * SCALE,
                collisionBox.width() * SCALE,
                collisionBox.height() * SCALE,
                new Color(0, 220, 0));
        }

        // 4. Hitbox overlay (red).
        final var hitbox = animation.getCurrentHitbox();
        if (hitbox != null) {
            drawOverlayRect(gc,
                (hitbox.x() - cf.x()) * SCALE,
                (hitbox.y() - cf.y()) * SCALE,
                hitbox.width() * SCALE,
                hitbox.height() * SCALE,
                new Color(220, 0, 0));
        }

        // 5. HUD: frame info and controls below the sprite.
        gc.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        final int hudX = 8;
        int hudY = SCALED_H + 18;

        final long frameElapsedMs = (System.nanoTime() - lastFrameChangeNanos.get()) / 1_000_000L;

        drawHudLine(gc, hudX, hudY,
            String.format("Frame  %d  (x=%d y=%d w=%d h=%d  dur=%dms  elapsed=%dms)",
                currentFrameIndex.get(),
                cf.x(), cf.y(), cf.width(), cf.height(),
                cf.duration(), frameElapsedMs));
        hudY += 16;

        drawHudLine(gc, hudX, hudY,
            String.format("Speed  %.2fx   Reversed: %-5b   Paused: %b",
                animation.getSpeed(), animation.isReversed(), animation.isPaused()));
        hudY += 20;

        gc.setColor(new Color(160, 160, 160));
        gc.drawString("[SPACE] pause   [R] reverse   [ENTER] reset   [+/-] speed", hudX, hudY);
        hudY += 16;

        gc.setColor(new Color(0, 220, 0));
        gc.drawString("■ collision box", hudX, hudY);
        gc.setColor(new Color(220, 0, 0));
        gc.drawString("■ hitbox", hudX + 150, hudY);

        // 6. Event log on the right side of the sprite.
        final int logX = SCALED_W + 12;
        gc.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        gc.setColor(new Color(200, 200, 200));
        gc.drawString("Event log", logX, 16);

        gc.setColor(new Color(80, 80, 80));
        gc.drawLine(logX, 20, logX, SCALED_H);

        final String[] entries;
        synchronized (eventLog) {
            entries = eventLog.toArray(new String[0]);
        }

        int logY = 34;
        for (int i = entries.length - 1; i >= 0; i--) {
            gc.setColor(i == entries.length - 1 ? Color.WHITE : new Color(140, 140, 140));
            gc.drawString(entries[i], logX + 8, logY);
            logY += 14;
        }
    }

    private static void addLog(final Deque<String> log, final String entry) {
        synchronized (log) {
            if (log.size() >= MAX_LOG_ENTRIES) {
                log.pollFirst();
            }
            log.addLast(entry);
        }
    }

    /**
     * Draws a box outline with a dark border underneath so it remains legible against any sprite colour.
     * The dark stroke is 3px wide; the coloured stroke is 1px wide and sits on top of it.
     */
    private static void drawOverlayRect(
        final Graphics2D gc,
        final int x, final int y,
        final int w, final int h,
        final Color color
    ) {
        final Stroke savedStroke = gc.getStroke();

        gc.setStroke(new BasicStroke(3f));
        gc.setColor(new Color(0, 0, 0, 180));
        gc.drawRect(x, y, w - 1, h - 1);

        gc.setStroke(new BasicStroke(1f));
        gc.setColor(color);
        gc.drawRect(x, y, w - 1, h - 1);

        gc.setStroke(savedStroke);
    }

    /** Draws a two-tone checkerboard covering the sprite area (0, 0, SCALED_W, SCALED_H). */
    private static void drawCheckerboard(final Graphics2D gc) {
        for (int row = 0; row * SCALE < SCALED_H; row++) {
            for (int col = 0; col * SCALE < SCALED_W; col++) {
                gc.setColor((row + col) % 2 == 0 ? new Color(180, 180, 180) : new Color(110, 110, 110));
                gc.fillRect(col * SCALE, row * SCALE, SCALE, SCALE);
            }
        }
    }

    /** Draws a HUD text line with a white foreground and a subtle dark shadow for readability. */
    private static void drawHudLine(final Graphics2D gc, final int x, final int y, final String text) {
        gc.setColor(new Color(0, 0, 0, 160));
        gc.drawString(text, x + 1, y + 1);
        gc.setColor(Color.WHITE);
        gc.drawString(text, x, y);
    }
}
