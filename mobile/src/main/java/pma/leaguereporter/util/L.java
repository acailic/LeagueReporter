package pma.leaguereporter.util;

import android.util.Log;

public class L {

	public static void i(String message) {
		if (Configs.IS_DEBUG_MODE) {
			Log.i(Configs.APP_TAG, message);
		}
	}

	public static void i(Class c, String message) {
		if (Configs.IS_DEBUG_MODE) {
			Log.i(Configs.APP_TAG, "{" + c.getSimpleName() + "} : " + message);
		}
	}

	public static void e(String message) {
		if (Configs.IS_DEBUG_MODE) {
			Log.e(Configs.APP_TAG, message);
		}
	}

	public static void e(Class c, String message) {
		if (Configs.IS_DEBUG_MODE) {
			Log.e(Configs.APP_TAG, "{" + c.getSimpleName() + "} : " + message);
		}
	}

	public static void d(String message) {
		if (Configs.IS_DEBUG_MODE) {
			Log.d(Configs.APP_TAG, message);
		}
	}

	public static void d(Class c, String message) {
		if (Configs.IS_DEBUG_MODE) {
			Log.d(Configs.APP_TAG, "{" + c.getSimpleName() + "} : " + message);
		}
	}

}
