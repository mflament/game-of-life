/**
 * 
 */
package org.yah.games.gameoflife.java2d.rule;

import org.yah.games.gameoflife.java2d.universe.State;


/**
 * @author Oodrive
 * @created 2019/02/05
 */
public class OptimizedConditionalRule extends AbstractRule {

	public OptimizedConditionalRule(RuleConfiguration configuration) {
		super(configuration);
	}

	@Override
	public State evaluate(State state, int neighbors) {
		int[] matchingNeighbors = configuration.getMatchingNeighbors(state);
		for (int i = 0; i < matchingNeighbors.length; i++) {
			if (matchingNeighbors[i] == neighbors)
				return State.ON;
			if (matchingNeighbors[i] > neighbors) // since matchingNeighbors is sorted, we can stop searching for neighbors
				break;
		}
		return State.OFF;
	}

}
