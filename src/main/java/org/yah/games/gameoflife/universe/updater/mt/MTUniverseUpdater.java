package org.yah.games.gameoflife.universe.updater.mt;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.yah.games.gameoflife.rule.Rule;
import org.yah.games.gameoflife.universe.Universe;
import org.yah.games.gameoflife.universe.updater.AbstractUniverseUpdater;

public class MTUniverseUpdater extends AbstractUniverseUpdater implements Closeable {

	private final int threadCount;

	private final ExecutorService executorService;

	public MTUniverseUpdater(Rule rule) {
		this(rule, Runtime.getRuntime().availableProcessors());
	}

	public MTUniverseUpdater(Rule rule, int threadCount) {
		super(rule);
		this.threadCount = threadCount;
		executorService = Executors.newFixedThreadPool(threadCount, new ThreadFactory() {
			private final AtomicInteger threadId = new AtomicInteger();

			@Override
			public Thread newThread(Runnable r) {
				Thread thread = new Thread(r);
				thread.setName("universe-updater-" + threadId.getAndIncrement());
				return thread;
			}
		});
	}

	@Override
	public void close() throws IOException {
		executorService.shutdown();
	}

	@Override
	public void update(Universe from, Universe to) {
		int chunk = from.height() / threadCount;
		int remaining = chunk % threadCount;
		int offset = 0;
		CountDownLatch countDownLatch = new CountDownLatch(threadCount);
		for (int i = 0; i < threadCount; i++) {
			int h = chunk;
			if (remaining > 0) {
				h++;
				remaining--;
			}
			submit(from, to, offset, h, countDownLatch);
			offset += h;
		}

		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void submit(Universe from, Universe to, int offset, int h, CountDownLatch countDownLatch) {
		executorService.submit(() -> update(from, to, offset, h, countDownLatch));
	}

	private void update(Universe from, Universe to, int offset, int height, CountDownLatch countDownLatch) {
		// System.out.println("Update[" + offset + "->" + height + "]");
		update(from, to, offset, height);
		countDownLatch.countDown();
	}
}
