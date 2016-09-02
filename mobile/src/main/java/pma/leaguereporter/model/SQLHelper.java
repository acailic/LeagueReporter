package pma.leaguereporter.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import pma.leaguereporter.util.DBConst;

public class SQLHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "leaguereporter.ApplicationDB";
	private static final int DB_VERSION = 1;

	private static volatile SQLHelper sInstance;

	/**
	 * This method initialized DataBase and return references for singleton object
	 * @param context application context
	 * @return references for singleton object
	 */
	public static synchronized SQLHelper getInstance(Context context) {
		if (sInstance == null) synchronized (SQLHelper.class) {
			if (sInstance == null) sInstance = new SQLHelper(context);
		}
		return sInstance;
	}

	protected SQLHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String params1 = DBConst.ID + " INTEGER NOT NULL," +
				DBConst.CAPTION + " TEXT," +
				DBConst.LEAGUE + " TEXT," +
				DBConst.YEAR + " NUMERIC," +
				DBConst.CURRENT_MATCHDAY + " INTEGER," +
				DBConst.NUMBER_OF_MATCHDAYS + " INTEGER," +
				DBConst.NUMBER_OF_TEAMS + " INTEGER," +
				DBConst.NUMBER_OF_GAMES + " INTEGER," +
				DBConst.LAST_UPDATED + " NUMERIC";

		String params2 = DBConst.CAN_NOTIFIED + " NUMERIC DEFAULT 0," +
				DBConst.ID + " INTEGER NOT NULL," +
				DBConst.SOCCER_SEASON_ID + " INTEGER," +
				DBConst.DATE + " NUMERIC," +
				DBConst.STATUS + " INTEGER," +
				DBConst.MATCHDAY + " INTEGER," +
				DBConst.HOME_TEAM_ID + " INTEGER NOT NULL," +
				DBConst.HOME_TEAM_NAME + " TEXT," +
				DBConst.AWAY_TEAM_ID + " INTEGER NOT NULL," +
				DBConst.AWAY_TEAM_NAME + " TEXT," +
				DBConst.GOALS_HOME_TEAM + " INTEGER," +
				DBConst.GOALS_AWAY_TEAM + " INTEGER";

		String params3 = DBConst.FIXTURE_ID + " INTEGER NOT NULL," +
				DBConst.COUNT + " INTEGER," +
				DBConst.TIME_FRAME_START + " NUMERIC," +
				DBConst.TIME_FRAME_END + " NUMERIC," +
				DBConst.HOME_TEAM_WINS + " INTEGER," +
				DBConst.AWAY_TEAM_WINS + " INTEGER," +
				DBConst.DRAWS + " INTEGER";

		String params4 = DBConst.SOCCER_SEASON_ID + " INTEGER NOT NULL," +
				DBConst.RANK + " INTEGER," +
				DBConst.TEAM + " TEXT," +
				DBConst.TEAM_ID + " INTEGER," +
				DBConst.PLAYED_GAMES + " INTEGER," +
				DBConst.CREST_URL + " TEXT," +
				DBConst.POINTS + " INTEGER," +
				DBConst.GOALS + " INTEGER," +
				DBConst.GOAL_AGAINST + " INTEGER," +
				DBConst.GOAL_DIFFERENCE + " INTEGER";

		String params5 = DBConst.ID + " INTEGER NOT NULL," +
				DBConst.CAN_FAVORITE + " NUMERIC," +
				DBConst.NAME + " TEXT," +
				DBConst.SHORT_NAME + " TEXT," +
				DBConst.SQUAD_MARKET_VALUE + " TEXT," +
				DBConst.CREST_URL + " TEXT";

		String params6 = DBConst.TEAM_ID + " INTEGER NOT NULL," +
				DBConst.ID + " INTEGER NOT NULL," +
				DBConst.NAME + " TEXT," +
				DBConst.POSITION + " TEXT," +
				DBConst.JERSEY_NUMBER + " INTEGER," +
				DBConst.DATE_OF_BIRTH + " NUMERIC," +
				DBConst.NATIONALITY + " TEXT," +
				DBConst.CONTRACT_UNTIL + " NUMERIC," +
				DBConst.MARKET_VALUE + " TEXT";

		createTable(db, DBConst.TABLE_LEAGUES, params1);
		createTable(db, DBConst.TABLE_FIXTURES, params2);
		createTable(db, DBConst.TABLE_HEAD2HEAD, params3);
		createTable(db, DBConst.TABLE_SCORES, params4);
		createTable(db, DBConst.TABLE_TEAMS, params5);
		createTable(db, DBConst.TABLE_PLAYERS, params6);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropTables(db);
		onCreate(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropTables(db);
		onCreate(db);
	}

	/**
	 * Drop all tables
	 * @param db database
	 */
	private void dropTables(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + DBConst.TABLE_LEAGUES);
		db.execSQL("DROP TABLE IF EXISTS " + DBConst.TABLE_FIXTURES);
		db.execSQL("DROP TABLE IF EXISTS " + DBConst.TABLE_HEAD2HEAD);
		db.execSQL("DROP TABLE IF EXISTS " + DBConst.TABLE_SCORES);
		db.execSQL("DROP TABLE IF EXISTS " + DBConst.TABLE_TEAMS);
		db.execSQL("DROP TABLE IF EXISTS " + DBConst.TABLE_PLAYERS);
	}

	/**
	 * Create table
	 * @param db database
	 * @param tableName table name
	 * @param fields fields
	 */
	private void createTable(SQLiteDatabase db, String tableName, String fields) {
		StringBuilder query = new StringBuilder()
				.append("CREATE TABLE ").append(tableName)
				.append("(").append(fields).append(");");
		db.execSQL(query.toString());
	}

	/**
	 * Insert row by table name
	 * @param tableName table name
	 * @param values values in row
	 */
	public void insert(String tableName, ContentValues values) {
		getWritableDatabase().insert(tableName, null, values);
	}

	/**
	 * Update row by table name
	 * @param tableName table name
	 * @param values values in row
	 * @param where array where
	 * @param arguments array arguments
	 */
	public void update(String tableName, ContentValues values, String[] where, String[] arguments) {
		StringBuilder sb = new StringBuilder();
		int len = where.length;
		sb.append(where[0]).append("=?");
		for (int i = 1; i < len; i++)
			sb.append(" AND ").append(where[i]).append("=?");
		getWritableDatabase().update(tableName, values, sb.toString(), arguments);
	}

	/**
	 * Return reference on row by table name
	 * @param tableName table name
	 * @param columns columns to return
	 * @param where array where
	 * @param arguments array arguments
	 * @param groupBy group by
	 * @param having having
	 * @param orderBy your column + <b>ASC</b> or <b>DESC</b>
	 * @return reference on row
	 */
	public Cursor get(String tableName, String[] columns, String[] where,
					  String[] arguments, String groupBy, String having, String orderBy) {
		StringBuilder sb = null;
		if (where != null) {
			sb = new StringBuilder();
			int len = where.length;
			sb.append(where[0]).append("=?");
			for (int i = 1; i < len; i++)
				sb.append(" AND ").append(where[i]).append("=?");
		}
		String where_str = null;
		if (sb != null) where_str = sb.toString();
		//order [ASC|DESC]
		return getReadableDatabase()
				.query(tableName, columns, where_str, arguments, groupBy, having, orderBy);
	}

	/**
	 * Return reference on row by table name
	 * @param tableName table name
	 * @return reference on row
	 */
	public Cursor getAll(String tableName) {
		return get(tableName, null, null, null, null, null, null);
	}

	/**
	 * Return reference on row by table name
	 * @param tableName table name
	 * @param where array where
	 * @param arguments array arguments
	 * @return reference on row
	 */
	public Cursor getAll(String tableName, String[] where, String[] arguments) {
		return get(tableName, null, where, arguments, null, null, null);
	}

	/**
	 * Return reference on row by table name
	 * @param tableName table name
	 * @param where array where
	 * @param arguments array arguments
	 * @param orderBy column which sort
	 * @param beginningAtLarge begin at large
	 * @return reference on row
	 */
	public Cursor getAll(
			String tableName,
			String[] where,
			String[] arguments,
			String orderBy,
			boolean beginningAtLarge) {
		orderBy += beginningAtLarge ? " DESC" : " ASC";
		return get(tableName, null, where, arguments, null, null, orderBy);
	}

	/**
	 * Remove selected rows in table
	 * @param tableName table name
	 * @param where array where
	 * @param arguments array arguments
	 */
	public void remove(String tableName, String[] where, String[] arguments) {
		StringBuilder sb = null;
		if (where != null) {
			sb = new StringBuilder();
			int len = where.length;
			sb.append(where[0]).append("=?");
			for (int i = 1; i < len; i++)
				sb.append(" AND ").append(where[i]).append("=?");
		}
		String where_str = null;
		if (sb != null) where_str = sb.toString();
		getWritableDatabase().delete(tableName, where_str, arguments);
	}

	/**
	 * Clear table by table name
	 * @param tableName table name
	 */
	public void removeAll(String tableName) {
		getWritableDatabase().delete(tableName, null, null);
	}

	public Cursor query(String table_name, String[] columns, String where,
						String[] arguments, String group_by, String having, String order_by) {

		return getReadableDatabase()
				.query(table_name, columns, where, arguments, group_by, having, order_by);
	}

}
