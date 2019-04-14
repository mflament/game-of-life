package org.yah.games.gameoflife.rule;

import org.yah.games.gameoflife.universe.State;


public interface Rule {

	public static final int MAX_NEIGHBOR_COUNT = 8;

	public static final int NEIGHBORS_POSIBILITIES = 9;

	/**
	 * @param state
	 * the current cell state
	 * @param neighbors
	 * the neighbors count of the cell
	 * @return the next state from the current state and the number of neighbors
	 */
	State evaluate(State state, int neighbors);

}
