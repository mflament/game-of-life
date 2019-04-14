package org.yah.games.gameoflife.universe.updater.st;

import org.yah.games.gameoflife.rule.Rule;
import org.yah.games.gameoflife.universe.Universe;
import org.yah.games.gameoflife.universe.updater.AbstractUniverseUpdater;

public class STUniverseUpdater extends AbstractUniverseUpdater {

	public STUniverseUpdater(Rule rule) {
		super(rule);
	}

	@Override
	public void update(Universe from, Universe to) {
		update(from, to, 0, from.height());
	}

}
