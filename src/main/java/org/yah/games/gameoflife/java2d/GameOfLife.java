package org.yah.games.gameoflife.java2d;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Closeable;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import org.yah.games.gameoflife.counter.TimeTracker;
import org.yah.games.gameoflife.java2d.rule.IndexedRule;
import org.yah.games.gameoflife.java2d.rule.Rule;
import org.yah.games.gameoflife.java2d.rule.Rules;
import org.yah.games.gameoflife.java2d.universe.BufferedImageUniverse;
import org.yah.games.gameoflife.java2d.universe.MTUniverseUpdater;
import org.yah.games.gameoflife.java2d.universe.STUniverseUpdater;
import org.yah.games.gameoflife.java2d.universe.State;
import org.yah.games.gameoflife.java2d.universe.Universe;
import org.yah.games.gameoflife.java2d.universe.UniverseUpdater;

public class GameOfLife {

	private static final Long SEED = null;

	private static final int SIZE = 1024;

	private static final int THREADS = 4;

	private static final float RANDOM_FACTOR = .08f;

	private final int universeSize;

	private final int updateThreadCount;

	private Universe universe;

	private UniverseUpdater universeUpdater;

	private Canvas canvas;

	private UniverseImageProducer imageProducer;

	private JFrame frame;

	private ExecutorService updaterService;

	private final AtomicReference<UpdaterTask> currentTask = new AtomicReference<>();

	private final Rule rule;

	private Image image;

	public static void main(String[] args) {
		new GameOfLife().run(args);
	}

	public GameOfLife() {
		this(SIZE, THREADS, Rules.create(Rules.STANDARD_DEFINITION, IndexedRule::new));
	}

	public GameOfLife(int universeSize, int updateThreadCount, Rule rule) {
		super();
		this.universeSize = universeSize;
		this.updateThreadCount = updateThreadCount;
		this.rule = rule;
	}

	public void run(String[] args) {
		SwingUtilities.invokeLater(this::setup);
	}

	private void setup() {
		updaterService = Executors.newSingleThreadExecutor(r -> new Thread(r, "updater thread"));
		universe = new BufferedImageUniverse(universeSize, universeSize);
		if (updateThreadCount > 1)
			universeUpdater = new MTUniverseUpdater(rule, updateThreadCount);
		else
			universeUpdater = new STUniverseUpdater(rule);
		createFrame();
		imageProducer = new UniverseImageProducer(universe);
		image = canvas.createImage(imageProducer);
	}

	private void destroy() {
		UpdaterTask t = currentTask.get();
		if (t != null) {
			t.requestStop();
		}

		updaterService.shutdown();
		try {
			updaterService.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (universeUpdater instanceof Closeable) {
			try {
				((Closeable) universeUpdater).close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void render() {
		render(universe);
	}

	private void render(Universe u) {
		imageProducer.update(u);
		Graphics graphics = canvas.getGraphics();
		if (graphics != null)
			graphics.drawImage(image, 0, 0, null);
	}

	private void clear() {
		universe.clear();
		render();
	}

	private void randomize() {
		clear();
		long seed = SEED == null ? System.currentTimeMillis() : SEED;
		Random random = new Random(seed);
		for (int y = 0; y < universe.height(); y++) {
			for (int x = 0; x < universe.width(); x++) {
				float pct = random.nextFloat();
				universe.set(x, y, pct < RANDOM_FACTOR ? State.ON : State.OFF);
			}
		}
		render();
	}

	private void toggleUpdate() {
		UpdaterTask t = currentTask.get();
		if (t == null || t.isStopRequested()) {
			t = new UpdaterTask();
			currentTask.set(t);
			updaterService.submit(t);
		} else {
			t.requestStop();
		}
	}

	private void toggleState(Point point) {
		if (point.x >= 0 && point.x < universe.width() && point.y >= 0 && point.y < universe.width()) {
			universe.set(point.x, point.y, State.toggle(universe.get(point.x, point.y)));
		}
		render();
	}

	private void createFrame() {
		frame = new JFrame("Game of Life (Java2D) [" + rule + "]");

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
				destroy();
			}

			@Override
			public void windowOpened(WindowEvent e) {
				render();
			}
		});
	}

	private Canvas createCanvas() {
		Canvas canvas = new Canvas() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paint(Graphics g) {
				render();
			}
		};

		canvas.setSize(universe.width(), universe.height());
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
		canvas.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				render();
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

	private final class LockableUniverse {

		@SuppressWarnings("hiding")
		private final Universe universe;

		private boolean locked;

		public LockableUniverse(Universe universe) {
			super();
			this.universe = universe;
		}

		public synchronized boolean lock() {
			try {
				while (locked) {
					wait();
					locked = true;
				}
				return true;
			} catch (InterruptedException e) {
				return false;
			}
		}

		public synchronized void unlock() {
			if (locked) {
				locked = false;
				notifyAll();
			}
		}

		public void update(LockableUniverse target) {
			universeUpdater.update(universe, target.universe);
		}

		public void render() {
			GameOfLife.this.render(universe);
		}
	}

	private final class UpdaterTask implements Runnable {

		private final AtomicBoolean stopRequested = new AtomicBoolean();

		private LockableUniverse frontBuffer;
		private LockableUniverse backBuffer;

		public UpdaterTask() {
			super();
			this.frontBuffer = new LockableUniverse(universe);
			BufferedImageUniverse backUniverse = new BufferedImageUniverse(universeSize, universeSize);
			this.backBuffer = new LockableUniverse(backUniverse);
		}

		@Override
		public void run() {
			try {
				TimeTracker timedTracker = new TimeTracker();
				while (!stopRequested.get()) {
					if (backBuffer.lock()) {
						timedTracker.track(() -> frontBuffer.update(backBuffer));
						SwingUtilities.invokeLater(() -> render(backBuffer));
						swap();
						universe = frontBuffer.universe;
					}
				}
			} catch (RuntimeException e) {
				e.printStackTrace();
			} finally {
				currentTask.set(null);
			}
		}

		private void swap() {
			LockableUniverse tmp = frontBuffer;
			frontBuffer = backBuffer;
			backBuffer = tmp;
		}

		private void render(LockableUniverse u) {
			u.render();
			u.unlock();
		}

		public void requestStop() {
			stopRequested.set(true);
		}

		public boolean isStopRequested() {
			return stopRequested.get();
		}
	}

}
