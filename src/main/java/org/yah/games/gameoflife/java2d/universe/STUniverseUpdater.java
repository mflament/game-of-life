package org.yah.games.gameoflife.java2d.universe;

import org.yah.games.gameoflife.java2d.rule.Rule;

public class STUniverseUpdater extends AbstractUniverseUpdater {

	public STUniverseUpdater(Rule rule) {
		super(rule);
	}

	@Override
	public void update(Universe from, Universe to) {
		update(from, to, 0, from.height());
	}

}
