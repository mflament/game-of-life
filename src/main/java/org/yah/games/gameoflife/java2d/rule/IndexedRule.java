package org.yah.games.gameoflife.java2d.rule;

import java.util.Arrays;

import org.yah.games.gameoflife.java2d.universe.State;


/**
 * Rule implementation using an array of states per neighbors possibilities for each current state :
 * 	[current_state][neighbors_count] => new_state
 * @author Oodrive
 * @created 2019/02/05
 */
public final class IndexedRule extends AbstractRule {

	private final State[][] states = new State[2][NEIGHBORS_POSIBILITIES];

	public IndexedRule(RuleConfiguration ruleConfiguration) {
		super(ruleConfiguration);
		initializeStates(states[State.OFF.ordinal()], ruleConfiguration.getBorns());
		initializeStates(states[State.ON.ordinal()], ruleConfiguration.getSurvives());
	}

	private static void initializeStates(State[] states, int[] neighbors) {
		Arrays.fill(states, State.OFF);
		for (int i = 0; i < neighbors.length; i++) {
			int count = neighbors[i];
			states[count] = State.ON;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.yah.games.gameoflife.java2d.rule.Rule#evaluate(org.yah.games.gameoflife.java2d.universe.State, int)
	 */
	@Override
	public State evaluate(State state, int neighbors) {
		return states[state.ordinal()][neighbors];
	}

}
