package pma.leaguereporter.model.comparators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import pma.leaguereporter.model.objects.Player;

public class ComparatorPlayers {

	public static void sortByJerseyNumber(ArrayList<Player> list) {
		Collections.sort(list, new Comparator<Player>() {
			@Override
			public int compare(Player lhs, Player rhs) {
				int first = lhs.getJerseyNumber();
				int next = rhs.getJerseyNumber();
				if (first > next) return 1;
				else if (first < next) return -1;
				else return 0;
			}
		});
	}

}
