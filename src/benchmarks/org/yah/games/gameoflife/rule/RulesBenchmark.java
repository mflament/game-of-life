package org.yah.games.gameoflife.rule;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.infra.Blackhole;
import org.yah.games.gameoflife.rule.BitmaskRule;
import org.yah.games.gameoflife.rule.ConditionalRule;
import org.yah.games.gameoflife.rule.IndexedRule;
import org.yah.games.gameoflife.rule.OptimizedConditionalRule;
import org.yah.games.gameoflife.rule.Rule;
import org.yah.games.gameoflife.rule.Rules;
import org.yah.games.gameoflife.universe.State;


@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class RulesBenchmark {

	@org.openjdk.jmh.annotations.State(Scope.Benchmark)
	public static class RuleInput {

		private final Random random = new Random();

		private final Rule indexedRule = Rules.create("B3/S23", IndexedRule::new);
		private final Rule conditionalRule = Rules.create("B3/S23", ConditionalRule::new);
		private final Rule optimizedConditionalRule = Rules.create("B3/S23", OptimizedConditionalRule::new);
		private final Rule bitmaskRule = Rules.create("B3/S23", BitmaskRule::new);

		public State state;
		public int neighbors;

		@Setup(Level.Iteration)
		public void setup() {
			state = random.nextBoolean() ? State.ON : State.OFF;
			neighbors = random.nextInt(9);
		}
	}

	@Benchmark
	public void indexedRule(RuleInput input, Blackhole blackhole) {
		blackhole.consume(input.indexedRule.evaluate(input.state, input.neighbors));
	}

	@Benchmark
	public void conditionalRule(RuleInput input, Blackhole blackhole) {
		blackhole.consume(input.conditionalRule.evaluate(input.state, input.neighbors));
	}

	@Benchmark
	public void optimizedConditionalRule(RuleInput input, Blackhole blackhole) {
		blackhole.consume(input.optimizedConditionalRule.evaluate(input.state, input.neighbors));
	}

	@Benchmark
	public void bitmaskRule(RuleInput input, Blackhole blackhole) {
		blackhole.consume(input.bitmaskRule.evaluate(input.state, input.neighbors));
	}
}
