package pma.leaguereporter.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesManager {

	private static final String PREF_NAME = "leaguereporter.Pref";
	private static PreferencesManager sInstance;

	private SharedPreferences mPref;
	private SharedPreferences.Editor mEditor;

	public static synchronized PreferencesManager from(Context context) {
		if (sInstance == null) synchronized (PreferencesManager.class){
			if (sInstance == null) sInstance = new PreferencesManager(context);
		}
		return sInstance;
	}

	protected PreferencesManager(Context context) {
		//if (mPref == null) mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		if (mPref == null) mPref = PreferenceManager.getDefaultSharedPreferences(context);
		if (mEditor == null) mEditor = mPref.edit();
	}

	public PreferencesManager setString(String key, String value) {
		mEditor.putString(key, value);
		return this;
	}

	public PreferencesManager setInt(String key, int value) {
		mEditor.putInt(key, value);
		return this;
	}

	public PreferencesManager setLong(String key, long value) {
		mEditor.putLong(key, value);
		return this;
	}

	public PreferencesManager setFloat(String key, float value) {
		mEditor.putFloat(key, value);
		return this;
	}

	public PreferencesManager setBoolean(String key, boolean value) {
		mEditor.putBoolean(key, value);
		return this;
	}

	public PreferencesManager remove(String key) {
		mEditor.remove(key);
		return this;
	}

	public void clearAll() {
		mEditor.clear();
	}

	public void commit() {
		mEditor.commit();
	}

	public String getString(String key) {
		return mPref.getString(key, null);
	}

	public String getString(String key, String def) {
		String val = getString(key);
		return (val != null) ? val : def;
	}

	public int getInt(String key) {
		return mPref.getInt(key, -1);
	}

	public int getInt(String key, int def) {
		return mPref.getInt(key, def);
	}

	public long getLong(String key) {
		return mPref.getLong(key, -1);
	}

	public long getLong(String key, long def) {
		return mPref.getLong(key, def);
	}

	public float getFloat(String key) {
		return mPref.getFloat(key, -1);
	}

	public float getFloat(String key, float def) {
		return mPref.getFloat(key, def);
	}

	public boolean getBoolean(String key) {
		return mPref.getBoolean(key, false);
	}

	public boolean getBoolean(String key, boolean def) {
		return mPref.getBoolean(key, def);
	}

}
