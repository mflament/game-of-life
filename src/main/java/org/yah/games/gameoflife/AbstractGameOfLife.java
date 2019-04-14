package org.yah.games.gameoflife;

import java.io.Closeable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.apache.commons.io.IOUtils;
import org.yah.games.gameoflife.ExecutionTimeTracker.ExecutionTimeSnapshot;
import org.yah.games.gameoflife.universe.Universe;
import org.yah.games.gameoflife.universe.array.ArrayUniverse;
import org.yah.games.gameoflife.universe.updater.UniverseUpdater;
import org.yah.games.gameoflife.universe.updater.mt.MTUniverseUpdater;
import org.yah.games.gameoflife.universe.updater.st.STUniverseUpdater;

public abstract class AbstractGameOfLife {

	protected final UserConfiguration configuration;

	private final Queue<Runnable> eventsQueue = new LinkedList<>();

	private final int fpsInterval;

	private UniverseUpdater universeUpdater;

	private Universe universe;

	private Universe nextUniverse;

	private boolean dirty;

	private long nextRenderTime;

	private boolean updateStarted;

	public AbstractGameOfLife(UserConfiguration configuration) {
		this.configuration = configuration;
		fpsInterval = 1000 / configuration.getTargetFps();
	}

	public void run() {
		createWindow();
		createUniverse();

		int updates = 0, frames = 0;
		ExecutionTimeTracker renderTimeTracker = new ExecutionTimeTracker();
		ExecutionTimeTracker updateTimeTracker = new ExecutionTimeTracker();

		// prepare the first render
		dirty = true;

		long start = getTime();
		while (!isCloseRequested()) {
			processEvents();

			if (mustUpdate()) {
				updateTimeTracker.track(this::update);
				updates++;
			}

			if (mustRender()) {
				renderTimeTracker.track(this::render);
				frames++;
			}

			pollEvents();

			long elapsed = getTime() - start;
			if (elapsed >= 1000) {
				int ups = (int) ((updates / (float) elapsed) * 1000);
				int fps = (int) ((frames / (float) elapsed) * 1000);
				updateStats(ups, fps, renderTimeTracker.snapshot(configuration.isResetTrackerOnUpdate()),
						updateTimeTracker.snapshot(configuration.isResetTrackerOnUpdate()));
				updates = frames = 0;
				start = getTime();
			}
		}
		destroy();
	}

	private boolean mustRender() {
		// render only when dirty (renders required) and FPS interval is passed
		return dirty && getTime() >= nextRenderTime;
	}

	private void render() {
		// to keep FPS at fixed rate, compute the next render time before rendering
		nextRenderTime = getTime() + fpsInterval;
		doRender(universe);
		dirty = false;
	}

	private boolean mustUpdate() {
		return updateStarted && (!configuration.isSyncUpdate() || !dirty);
	}

	private void update() {
		universeUpdater.update(universe, nextUniverse);
		swapUniverse();
		dirty = true;
	}

	protected abstract void createWindow();

	protected abstract boolean isCloseRequested();

	protected abstract String getTitle();

	protected abstract void updateTitle(String title);

	protected abstract void doRender(Universe universe);

	protected void cleanup() {}

	protected void pollEvents() {}

	private void createUniverse() {
		universeUpdater = createUniverseUpdater();
		universe = createUniverse(universeUpdater, configuration.getUniverseSize());
		nextUniverse = createUniverse(universeUpdater, configuration.getUniverseSize());
	}

	protected UniverseUpdater createUniverseUpdater() {
		String updaterName = getUpdaterName();

		switch (updaterName) {
		case "st":
			return new STUniverseUpdater(configuration.getRule());
		case "mt":
			return new MTUniverseUpdater(configuration.getRule(), configuration.getUdpaterThreads());
		default:
			throw new IllegalArgumentException("Unknown or unsupported updater " + updaterName);
		}
	}

	protected Universe createUniverse(UniverseUpdater universeUpdater, int universeSize) {
		return new ArrayUniverse(universeSize, universeSize);
	}

	protected final String getUpdaterName() {
		String updaterName = configuration.getArgument("updater", getDefaultUpdaterName());
		if (updaterName == null)
			throw new IllegalArgumentException("Missing updater name");
		return updaterName;
	}

	protected String getDefaultUpdaterName() {
		return "mt";
	}

	private void destroy() {
		destroyUniverse(universe);
		destroyUniverse(nextUniverse);
		destroyUniverseUpdater();
		cleanup();
	}

	private void destroyUniverse(Universe u) {
		if (u instanceof Closeable)
			IOUtils.closeQuietly((Closeable) u);
	}

	private void destroyUniverseUpdater() {
		if (universeUpdater instanceof Closeable)
			IOUtils.closeQuietly((Closeable) universeUpdater);
	}

	private final long getTime() {
		return TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
	}

	protected final void clear() {
		submitUniverseUpdateEvent(Universe::clear);
	}

	protected final void randomize() {
		submitUniverseUpdateEvent(u -> u.randomize(configuration.getRandomSeed(), configuration.getRandomFactor()));
	}

	protected final void toggleUpdate() {
		submitEvent(() -> updateStarted = !updateStarted);
	}

	protected final void submitUniverseUpdateEvent(Consumer<Universe> event) {
		submitEvent(() -> {
			event.accept(universe);
			dirty = true;
		});
	}

	protected final void submitEvent(Runnable event) {
		synchronized (eventsQueue) {
			eventsQueue.offer(event);
		}
	}

	private void processEvents() {
		Runnable event;
		synchronized (eventsQueue) {
			while ((event = eventsQueue.poll()) != null) {
				event.run();
			}
		}
	}

	private void swapUniverse() {
		Universe tmp = universe;
		universe = nextUniverse;
		nextUniverse = tmp;
	}

	private void updateStats(int ups, int fps, ExecutionTimeSnapshot renderTimeSnapshot,
			ExecutionTimeSnapshot updateTimeSnapshot) {
		System.out.println(String.format("FPS=%d (%s), UPS=%d (%s)", fps, renderTimeSnapshot, ups, updateTimeSnapshot));
		updateTitle(String.format("%s [FPS=%d, UPS=%d]", getTitle(), fps, ups));
	}

}
