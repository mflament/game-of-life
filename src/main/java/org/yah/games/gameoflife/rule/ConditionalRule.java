/**
 * 
 */
package org.yah.games.gameoflife.rule;

import org.yah.games.gameoflife.universe.State;


/**
 * A rule implementation testing if the neighbors count is contained in the matching rule configuration array.
 * 
 * @author Marc Flament
 * @created 2019/02/05
 */
public class ConditionalRule extends AbstractRule {

	public ConditionalRule(RuleConfiguration configuration) {
		super(configuration);
	}

	@Override
	public State evaluate(State state, int neighbors) {
		int[] matchingNeighbors = configuration.getMatchingNeighbors(state);
		for (int i = 0; i < matchingNeighbors.length; i++) {
			if (matchingNeighbors[i] == neighbors)
				return State.ON;
		}
		return State.OFF;
	}

}
