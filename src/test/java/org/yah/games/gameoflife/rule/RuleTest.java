package org.yah.games.gameoflife.rule;

import static org.junit.Assert.assertEquals;

import java.util.Locale;
import java.util.Random;
import java.util.function.Function;

import org.junit.Test;
import org.yah.games.gameoflife.rule.BitmaskRule;
import org.yah.games.gameoflife.rule.ConditionalRule;
import org.yah.games.gameoflife.rule.IndexedRule;
import org.yah.games.gameoflife.rule.OptimizedConditionalRule;
import org.yah.games.gameoflife.rule.Rule;
import org.yah.games.gameoflife.rule.RuleConfiguration;
import org.yah.games.gameoflife.rule.Rules;
import org.yah.games.gameoflife.universe.State;


public class RuleTest {

	private static final int BENCHMARK_ITERATIONS = 100_000_000;

	private static final long SEED = System.currentTimeMillis();

	private static final State[] STATES = State.values();

	@Test
	public void test_indexed_rule() {
		test_rule(IndexedRule::new);
	}

	@Test
	public void test_conditional_rule() {
		test_rule(ConditionalRule::new);
	}

	@Test
	public void test_optimized_conditional_rule() {
		test_rule(OptimizedConditionalRule::new);
	}

	@Test
	public void test_bitmask_rule() {
		test_rule(BitmaskRule::new);
	}

	@Test
	public void benchmark_rules() {
		benchmark_rule(IndexedRule::new);
		benchmark_rule(BitmaskRule::new);
		benchmark_rule(ConditionalRule::new);
		benchmark_rule(OptimizedConditionalRule::new);
	}

	private static State[] benchmark_rule(Function<RuleConfiguration, Rule> factory) {
		return benchmark_rule(factory, BENCHMARK_ITERATIONS);
	}

	private static State[] benchmark_rule(Function<RuleConfiguration, Rule> factory, int iterations) {
		Random random = new Random(SEED);
		State[] states = new State[iterations];
		int[] neighbors = new int[iterations];
		for (int i = 0; i < iterations; i++) {
			states[i] = STATES[random.nextInt(2)];
			neighbors[i] = random.nextInt(9);
		}

		Rule rule = Rules.create("B3/S23", factory);
		State[] results = new State[iterations];
		long start = System.currentTimeMillis();
		for (int i = 0; i < iterations; i++) {
			results[i] = rule.evaluate(states[i], neighbors[i]);
		}
		long elapsed = System.currentTimeMillis() - start;
		System.out.println(String.format(Locale.FRANCE,
				"%s: %dms (%,d/s)",
				rule.getClass().getSimpleName(),
				elapsed,
				(int) (iterations / ((float) elapsed / 1000))));
		return results;
	}

	private static void test_rule(Function<RuleConfiguration, Rule> factory) {
		Rule rule = Rules.create("B3/S23", factory);
		for (int i = 0; i < Rule.NEIGHBORS_POSIBILITIES; i++) {
			if (i == 3)
				assertOn(rule.evaluate(State.OFF, i));
			else
				assertOff(rule.evaluate(State.OFF, i));
		}

		for (int i = 0; i < IndexedRule.NEIGHBORS_POSIBILITIES; i++) {
			if (i == 2 || i == 3)
				assertOn(rule.evaluate(State.ON, i));
			else
				assertOff(rule.evaluate(State.ON, i));
		}

		rule = Rules.create("B012345678/S", factory);
		for (int i = 0; i < IndexedRule.NEIGHBORS_POSIBILITIES; i++) {
			assertOn(rule.evaluate(State.OFF, i));
		}

		for (int i = 0; i < IndexedRule.NEIGHBORS_POSIBILITIES; i++) {
			assertOff(rule.evaluate(State.ON, i));
		}

	}

	private static void assertOn(State state) {
		assertEquals(State.ON, state);
	}

	private static void assertOff(State state) {
		assertEquals(State.OFF, state);
	}

}
