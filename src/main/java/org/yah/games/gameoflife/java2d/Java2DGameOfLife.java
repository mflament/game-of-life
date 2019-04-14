package org.yah.games.gameoflife.java2d;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import org.yah.games.gameoflife.AbstractGameOfLife;
import org.yah.games.gameoflife.UserConfiguration;
import org.yah.games.gameoflife.universe.State;
import org.yah.games.gameoflife.universe.Universe;

public class Java2DGameOfLife extends AbstractGameOfLife {

	private static final String TITLE = "Game of Life (Java2D)";

	private Canvas canvas;

	private UniverseImageProducer imageProducer;

	private JFrame frame;

	private Image image;

	private boolean closeRequested;

	public static void main(String[] args) {
		new Java2DGameOfLife(UserConfiguration.parse(args)).run();
	}

	private Java2DGameOfLife(UserConfiguration configuration) {
		super(configuration);
	}

	@Override
	protected void createWindow() {
		invokeAndWait(() -> {
			createFrame();
			imageProducer = new UniverseImageProducer();
			image = canvas.createImage(imageProducer);
		});
	}

	@Override
	protected boolean isCloseRequested() {
		return closeRequested;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected void doRender(Universe universe) {
		invokeAndWait(() -> {
			Graphics graphics = canvas.getGraphics();
			if (graphics == null)
				return;
			imageProducer.update(universe);
			graphics.drawImage(image, 0, 0, null);
		});
	}

	@Override
	protected void updateTitle(String title) {
		invokeAndWait(() -> frame.setTitle(title));
	}

	private void invokeAndWait(Runnable task) {
		try {
			SwingUtilities.invokeAndWait(task);
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof RuntimeException)
				throw (RuntimeException) e.getTargetException();
			else if (e.getTargetException() instanceof Error)
				throw (Error) e.getTargetException();
			else
				throw new IllegalStateException("Error during EDT update", e);
		} catch (InterruptedException e) {
			Thread.interrupted();
		}
	}

	private void toggleState(Point point) {
		submitUniverseUpdateEvent(universe -> {
			if (point.x >= 0 && point.x < universe.width() && point.y >= 0 && point.y < universe.width())
				universe.set(point.x, point.y, State.toggle(universe.get(point.x, point.y)));
		});
	}

	private void createFrame() {
		frame = new JFrame(TITLE);

		canvas = createCanvas();
		frame.getContentPane().add(canvas);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		frame.setLocationByPlatform(true);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);

		canvas.requestFocus();

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				submitEvent(() -> closeRequested = true);
			}
		});
	}

	private Canvas createCanvas() {
		Canvas canvas = new Canvas() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paint(Graphics g) {
				submitUniverseUpdateEvent(u -> {});
			}
		};

		int universeSize = configuration.getUniverseSize();
		canvas.setSize(universeSize, universeSize);
		canvas.addMouseListener(new MouseInputAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					toggleState(e.getPoint());
				}
			}
		});
		canvas.addMouseMotionListener(new MouseInputAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				toggleState(e.getPoint());
			}
		});

		canvas.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_R:
					randomize();
					break;
				case KeyEvent.VK_C:
					clear();
					break;
				case KeyEvent.VK_SPACE:
					toggleUpdate();
					break;
				case KeyEvent.VK_ESCAPE:
					frame.dispose();
					break;
				default:
					break;
				}
			}
		});
		return canvas;
	}

}
