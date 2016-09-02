package pma.leaguereporter.util;

import java.util.Random;

public class Rand {
	private static final int min = 1;
	private static final int max = 88888888;

	public static int next() {
		return next(min, max);
	}

	public static int next(int min, int max) {
		Random random = new Random();
		return random.nextInt((max - min) + 1) + min;
	}
}
