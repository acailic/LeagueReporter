package pma.leaguereporter.model.comparators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import pma.leaguereporter.model.objects.Fixture;

public class ComparatorFixtures {

	public static void sortByDate(ArrayList<Fixture> list) {
		Collections.sort(list, new Comparator<Fixture>() {
			@Override
			public int compare(Fixture lhs, Fixture rhs) {
				long first = lhs.getDate();
				long next = rhs.getDate();
				if (first > next) return 1;
				else if (first < next) return -1;
				else return 0;
			}
		});
	}

}
