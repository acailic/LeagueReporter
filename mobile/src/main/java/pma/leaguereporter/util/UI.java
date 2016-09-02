package pma.leaguereporter.util;

import android.view.View;

public class UI {

	public static void show(View... views) {
		for (View view : views) view.setVisibility(View.VISIBLE);
	}

	public static void hide(View... views) {
		for (View view : views) view.setVisibility(View.GONE);
	}

}
