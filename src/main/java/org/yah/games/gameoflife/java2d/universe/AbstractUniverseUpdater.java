package org.yah.games.gameoflife.java2d.universe;

import org.yah.games.gameoflife.java2d.rule.Rule;

public abstract class AbstractUniverseUpdater implements UniverseUpdater {

	protected final Rule rule;

	public AbstractUniverseUpdater(Rule rule) {
		super();
		this.rule = rule;
	}

	protected final void update(Universe from, Universe to, int offset, int height) {
		int max = offset + height;
		int width = from.width();
		for (int y = offset; y < max; y++) {
			for (int x = 0; x < width; x++) {
				State s = rule.evaluate(from.get(x, y), from.neighbors(x, y));
				to.set(x, y, s);
			}
		}
	}

}
