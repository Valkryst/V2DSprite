package com.valkryst.V2DSprite;

import com.valkryst.V2DSprite.event.AnimationEvent;
import com.valkryst.V2DSprite.listener.AnimationEventListener;
import lombok.NonNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class VisualTest {
    public static void main(final String[] args) {
        final var canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(512, 512));

        final var frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(canvas);
        frame.pack();
        frame.revalidate();
        frame.setVisible(true);

		final var animation = new Animation("valid", "idle");
		animation.addAnimationEventListener(new AnimationEventListener() {
			@Override
			public void onFirstFrame(final @NonNull AnimationEvent event) {
				System.out.println("onFirstFrame");
			}

			@Override
			public void onNewFrame(final @NonNull AnimationEvent event) {
				System.out.println("onNewFrame");
			}

			@Override
			public void onLastFrame(final @NonNull AnimationEvent event) {
				System.out.println("onLastFrame");
			}

			@Override
			public void onPause(final @NonNull AnimationEvent event) {
				System.out.println("onPause");
			}

			@Override
			public void onResume(final @NonNull AnimationEvent event) {
				System.out.println("onResume");
			}
		});


		canvas.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					animation.setPaused(!animation.isPaused());
				}
			}
		});


		final var executorService = Executors.newSingleThreadScheduledExecutor();
		executorService.scheduleAtFixedRate(() -> {
			System.out.println("\n\nTICK");
			animation.update(1);

			final var gc = (Graphics2D) canvas.getGraphics();
			gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
			gc.setClip(0, 0, canvas.getWidth(), canvas.getHeight());
			animation.draw(gc);
		}, 0, 1000, TimeUnit.MILLISECONDS);
    }
}
