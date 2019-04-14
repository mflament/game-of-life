package org.yah.games.gameoflife.universe.coordinates;

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
import org.yah.games.gameoflife.universe.coordinates.BinaryWrapper;
import org.yah.games.gameoflife.universe.coordinates.CoordinateWrapper;
import org.yah.games.gameoflife.universe.coordinates.ModWrapper;
import org.yah.games.gameoflife.universe.coordinates.RecursiveWrapper;


@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class CoordinatesWrapperBenchmark {

	@org.openjdk.jmh.annotations.State(Scope.Benchmark)
	public static class CoordinateInput {

		private static final int WIDTH = 256;

		private final Random random = new Random();

		private final CoordinateWrapper recursiveWrapper = new RecursiveWrapper(WIDTH);
		private final CoordinateWrapper modWrapper = new ModWrapper(WIDTH);
		private final CoordinateWrapper binaryWrapper = new BinaryWrapper(WIDTH);

		public int coordinate;

		@Setup(Level.Iteration)
		public void setup() {
			coordinate = WIDTH - random.nextInt(WIDTH * 4);
		}
	}

	@Benchmark
	public void recursiveWrapper(CoordinateInput input, Blackhole blackhole) {
		blackhole.consume(input.recursiveWrapper.get(input.coordinate));
	}

	@Benchmark
	public void modWrapper(CoordinateInput input, Blackhole blackhole) {
		blackhole.consume(input.modWrapper.get(input.coordinate));
	}

	@Benchmark
	public void binaryWrapper(CoordinateInput input, Blackhole blackhole) {
		blackhole.consume(input.binaryWrapper.get(input.coordinate));
	}

}
