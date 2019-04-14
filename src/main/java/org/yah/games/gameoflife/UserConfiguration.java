package org.yah.games.gameoflife;

import java.util.LinkedHashMap;
import java.util.Map;

import org.yah.games.gameoflife.rule.IndexedRule;
import org.yah.games.gameoflife.rule.Rule;
import org.yah.games.gameoflife.rule.Rules;

public class UserConfiguration {

	private static final int DEFAULT_SIZE = 512;

	private static final float DEFAULT_RANDOM_FACTOR = .08f;

	private static final String DEFAULT_RULE = Rules.STANDARD_DEFINITION;

	private static final int DEFAULT_TARGET_FPS = 60;

	private static final int DEFAULT_MT_UPDATER_THREADS = 4;

	private final Map<String, String> arguments;

	private final int universeSize;

	private final int targetFps;

	private final Rule rule;

	private final float randomFactor;

	private final long randomSeed;

	private final boolean syncUpdate;

	private final int udpaterThreads;

	private final boolean resetTrackerOnUpdate;

	public UserConfiguration(Map<String, String> arguments) {
		this.arguments = arguments;
		universeSize = getIntArgument("size", DEFAULT_SIZE);
		randomFactor = getFloatArgument("rf", DEFAULT_RANDOM_FACTOR);
		randomSeed = getLongArgument("rs", System.currentTimeMillis());

		String ruleDefinition = getArgument("rule", DEFAULT_RULE);
		if (ruleDefinition == null)
			throw new IllegalArgumentException("Missing rule definition");
		rule = Rules.create(ruleDefinition, IndexedRule::new);

		targetFps = getIntArgument("fps", DEFAULT_TARGET_FPS);
		syncUpdate = hasArgument("sync");

		udpaterThreads = getIntArgument("threads", DEFAULT_MT_UPDATER_THREADS);

		resetTrackerOnUpdate = hasArgument("rt");
	}

	public int getUniverseSize() {
		return universeSize;
	}

	public int getTargetFps() {
		return targetFps;
	}

	public Rule getRule() {
		return rule;
	}

	public float getRandomFactor() {
		return randomFactor;
	}

	public long getRandomSeed() {
		return randomSeed;
	}

	public boolean isSyncUpdate() {
		return syncUpdate;
	}

	public int getUdpaterThreads() {
		return udpaterThreads;
	}

	public boolean isResetTrackerOnUpdate() {
		return resetTrackerOnUpdate;
	}

	public String getArgument(String name, String defaultValue) {
		if (hasArgument(name))
			return arguments.get(name);
		return defaultValue;
	}

	public int getIntArgument(String name, int defaultValue) {
		if (hasArgument(name))
			return Integer.parseInt(arguments.get(name));
		return defaultValue;
	}

	public long getLongArgument(String name, long defaultValue) {
		if (hasArgument(name))
			return Long.parseLong(arguments.get(name));
		return defaultValue;
	}

	public float getFloatArgument(String name, float defaultValue) {
		if (hasArgument(name))
			return Float.parseFloat(arguments.get(name));
		return defaultValue;
	}

	public boolean hasArgument(String name) {
		return arguments.containsKey(name);
	}

	private static final String ARG_NAME_PREFIX = "--";

	public static UserConfiguration parse(String[] args) {
		Map<String, String> arguments = new LinkedHashMap<>();
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith(ARG_NAME_PREFIX)) {
				// last argument or no values (next is an argument
				if (i == args.length - 1 || args[i + 1].startsWith(ARG_NAME_PREFIX)) {
					arguments.put(args[i].substring(ARG_NAME_PREFIX.length()), null);
				} else {
					arguments.put(args[i].substring(ARG_NAME_PREFIX.length()), args[++i]);
				}
			} else {
				throw new IllegalArgumentException("Invalid argument name " + args[i]
						+ ", argument name must start with " + ARG_NAME_PREFIX);
			}
		}
		return new UserConfiguration(arguments);
	}

}
