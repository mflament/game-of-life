# Java implementation of John Conway Game of Life

A simple implementation in java of John Conway [Game of Life](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life).
Currently use Java 2D API to render the universe.

2 implementations of the universe updater : one [single threaded](src/main/java/org/yah/games/gameoflife/java2d/universe/STUniverseUpdater.java) and one [multi threaded](src/main/java/org/yah/games/gameoflife/java2d/universe/STUniverseUpdater.java).

An functional interface for [coordinate wrapping](src/main/java/org/yah/games/gameoflife/java2d/universe/coordinates), with 3 implementations used to demonstrate efficiency of bitset operators versus modulo.

Use org.yah.games.gameoflife.java2d.GameOfLife to launch the GUI, and the following keys mapping:
* R : random universe
* C : clear universe
* Space : start / stop universe update
