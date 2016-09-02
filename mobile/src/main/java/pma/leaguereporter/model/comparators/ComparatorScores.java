package pma.leaguereporter.model.comparators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import pma.leaguereporter.model.objects.Scores;

public class ComparatorScores {

	public static void sortByPoints(ArrayList<Scores> list) {
		Collections.sort(list, new Comparator<Scores>() {
			@Override
			public int compare(Scores lhs, Scores rhs) {
				int first = lhs.getPoints();
				int next = rhs.getPoints();
				if (first > next) return -1;
				else if (first < next) return 1;
				else return 0;
			}
		});
	}

	public static void sortByPlayedGames(ArrayList<Scores> list) {
		Collections.sort(list, new Comparator<Scores>() {
			@Override
			public int compare(Scores lhs, Scores rhs) {
				int first = lhs.getPlayedGames();
				int next = rhs.getPlayedGames();
				if (first > next) return -1;
				else if (first < next) return 1;
				else return 0;
			}
		});
	}

	public static void sortByGoals(ArrayList<Scores> list) {
		Collections.sort(list, new Comparator<Scores>() {
			@Override
			public int compare(Scores lhs, Scores rhs) {
				int first = lhs.getGoals();
				int next = rhs.getGoals();
				if (first > next) return -1;
				else if (first < next) return 1;
				else return 0;
			}
		});
	}

}
