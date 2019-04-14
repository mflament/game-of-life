package org.yah.games.gameoflife.rule;

import org.yah.games.gameoflife.universe.State;


public class BitmaskRule extends AbstractRule {

	private final State[] STATES = State.values();

	private final int[] masks = new int[2];

	public BitmaskRule(RuleConfiguration configuration) {
		super(configuration);
		masks[State.OFF.ordinal()] = createMask(configuration.getBorns());
		masks[State.ON.ordinal()] = createMask(configuration.getSurvives());
	}

	@Override
	public State evaluate(State state, int neighbors) {
		int i = (masks[state.ordinal()] & countMask(neighbors)) >> neighbors;
		return STATES[i];
	}

	private static int createMask(int[] counts) {
		int res = 0;
		for (int i = 0; i < counts.length; i++) {
			res |= countMask(counts[i]);
		}
		return res;
	}

	private static int countMask(int c) {
		return 1 << c;
	}

}
