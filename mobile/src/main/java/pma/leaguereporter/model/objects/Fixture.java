package pma.leaguereporter.model.objects;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import pma.leaguereporter.util.DBConst;
import pma.leaguereporter.util.L;

public class Fixture implements Parcelable {

	public static final int FINISHED = 0;
	public static final int TIMED = 1;
	public static final int SCHEDULED = 2;
	public static final int INDEFINITE = 4;

	private boolean mCanNotified;
	private boolean mCanChange;
	private int mID;
	private int mSoccerSeasonID;
	private long mDate;
	private int mStatus = INDEFINITE;
	private int mMatchday;
	private int mHomeTeamID;
	private int mAwayTeamID;
	private String mHomeTeamName;
	private String mAwayTeamName;
	private int mGoalsHomeTeam = -1;
	private int mGoalsAwayTeam = -1;

	protected Fixture() {}

	protected Fixture(Parcel in) {
		mCanNotified = in.readByte() != 0;
		mCanChange = in.readByte() != 0;
		mID = in.readInt();
		mSoccerSeasonID = in.readInt();
		mDate = in.readLong();
		mStatus = in.readInt();
		mMatchday = in.readInt();
		mHomeTeamID = in.readInt();
		mAwayTeamID = in.readInt();
		mHomeTeamName = in.readString();
		mAwayTeamName = in.readString();
		mGoalsHomeTeam = in.readInt();
		mGoalsAwayTeam = in.readInt();
	}

	public static final Creator<Fixture> CREATOR = new Creator<Fixture>() {
		@Override
		public Fixture createFromParcel(Parcel in) {
			return new Fixture(in);
		}

		@Override
		public Fixture[] newArray(int size) {
			return new Fixture[size];
		}
	};

	@Nullable
	public static Fixture parse(Cursor fixtureCursor) {
		Fixture fixture = null;
		if (fixtureCursor.moveToFirst()) {
			int col_canNotified = fixtureCursor.getColumnIndex(DBConst.CAN_NOTIFIED);
			int col_id = fixtureCursor.getColumnIndex(DBConst.ID);
			int col_soccerSeasonId = fixtureCursor.getColumnIndex(DBConst.SOCCER_SEASON_ID);
			int col_date = fixtureCursor.getColumnIndex(DBConst.DATE);
			int col_status = fixtureCursor.getColumnIndex(DBConst.STATUS);
			int col_matchday = fixtureCursor.getColumnIndex(DBConst.MATCHDAY);
			int col_homeTeamId = fixtureCursor.getColumnIndex(DBConst.HOME_TEAM_ID);
			int col_homeTeamName = fixtureCursor.getColumnIndex(DBConst.HOME_TEAM_NAME);
			int col_awayTeamId = fixtureCursor.getColumnIndex(DBConst.AWAY_TEAM_ID);
			int col_awayTeamName = fixtureCursor.getColumnIndex(DBConst.AWAY_TEAM_NAME);
			int col_goalsHomeTeam = fixtureCursor.getColumnIndex(DBConst.GOALS_HOME_TEAM);
			int col_goalsAwayTeam = fixtureCursor.getColumnIndex(DBConst.GOALS_AWAY_TEAM);
			fixture = new Fixture();
			fixture.mCanNotified = fixtureCursor.getInt(col_canNotified) == 1;
			fixture.mID = fixtureCursor.getInt(col_id);
			fixture.mSoccerSeasonID = fixtureCursor.getInt(col_soccerSeasonId);
			fixture.mDate = fixtureCursor.getLong(col_date);
			fixture.mStatus = fixtureCursor.getInt(col_status);
			fixture.mMatchday = fixtureCursor.getInt(col_matchday);
			fixture.mHomeTeamID = fixtureCursor.getInt(col_homeTeamId);
			fixture.mHomeTeamName = fixtureCursor.getString(col_homeTeamName);
			fixture.mAwayTeamID = fixtureCursor.getInt(col_awayTeamId);
			fixture.mAwayTeamName = fixtureCursor.getString(col_awayTeamName);
			fixture.mGoalsHomeTeam = fixtureCursor.getInt(col_goalsHomeTeam);
			fixture.mGoalsAwayTeam = fixtureCursor.getInt(col_goalsAwayTeam);
		}
		return fixture;
	}

	@Nullable
	public static ArrayList<Fixture> parseArray(Cursor fixturesCursor) {
		if (fixturesCursor.moveToFirst()) {
			int col_canNotified = fixturesCursor.getColumnIndex(DBConst.CAN_NOTIFIED);
			int col_id = fixturesCursor.getColumnIndex(DBConst.ID);
			int col_soccerSeasonId = fixturesCursor.getColumnIndex(DBConst.SOCCER_SEASON_ID);
			int col_date = fixturesCursor.getColumnIndex(DBConst.DATE);
			int col_status = fixturesCursor.getColumnIndex(DBConst.STATUS);
			int col_matchday = fixturesCursor.getColumnIndex(DBConst.MATCHDAY);
			int col_homeTeamId = fixturesCursor.getColumnIndex(DBConst.HOME_TEAM_ID);
			int col_homeTeamName = fixturesCursor.getColumnIndex(DBConst.HOME_TEAM_NAME);
			int col_awayTeamId = fixturesCursor.getColumnIndex(DBConst.AWAY_TEAM_ID);
			int col_awayTeamName = fixturesCursor.getColumnIndex(DBConst.AWAY_TEAM_NAME);
			int col_goalsHomeTeam = fixturesCursor.getColumnIndex(DBConst.GOALS_HOME_TEAM);
			int col_goalsAwayTeam = fixturesCursor.getColumnIndex(DBConst.GOALS_AWAY_TEAM);
			ArrayList<Fixture> list = new ArrayList<>();
			do {
				Fixture fixture = new Fixture();
				fixture.mCanNotified = fixturesCursor.getInt(col_canNotified) == 1;
				fixture.mID = fixturesCursor.getInt(col_id);
				fixture.mSoccerSeasonID = fixturesCursor.getInt(col_soccerSeasonId);
				fixture.mDate = fixturesCursor.getLong(col_date);
				fixture.mStatus = fixturesCursor.getInt(col_status);
				fixture.mMatchday = fixturesCursor.getInt(col_matchday);
				fixture.mHomeTeamID = fixturesCursor.getInt(col_homeTeamId);
				fixture.mHomeTeamName = fixturesCursor.getString(col_homeTeamName);
				fixture.mAwayTeamID = fixturesCursor.getInt(col_awayTeamId);
				fixture.mAwayTeamName = fixturesCursor.getString(col_awayTeamName);
				fixture.mGoalsHomeTeam = fixturesCursor.getInt(col_goalsHomeTeam);
				fixture.mGoalsAwayTeam = fixturesCursor.getInt(col_goalsAwayTeam);
				list.add(fixture);
			} while (fixturesCursor.moveToNext());
			return list;
		}
		else return null;
	}

	@Nullable
	public static Fixture parse(JSONObject o) {
		if (o != null) {
			Fixture fixture = new Fixture();
			fixture.mID = o.optInt("id");
			fixture.mSoccerSeasonID = o.optInt("soccerseasonId");
			String date = o.optString("date", null);
			if (date != null) {
				try {
					fixture.mDate = new SimpleDateFormat(
							"yyyy-MM-dd'T'HH:mm:ss'Z'",
							Locale.getDefault())
							.parse(date)
							.getTime();
				} catch (ParseException e) {
					L.e(Fixture.class, e.toString());
				}
			}
			String status = o.optString("status", null);
			if (status != null) {
				switch (status) {
					case "FINISHED":
						fixture.mStatus = FINISHED;
						break;

					case "TIMED":
						fixture.mStatus = TIMED;
						break;

					case "SCHEDULED":
						fixture.mStatus = SCHEDULED;
						break;

					default:
						fixture.mStatus = INDEFINITE;
				}
			}
			fixture.mMatchday = o.optInt("matchday");
			fixture.mHomeTeamID = o.optInt("homeTeamId");
			fixture.mHomeTeamName = o.optString("homeTeamName");
			fixture.mAwayTeamID = o.optInt("awayTeamId");
			fixture.mAwayTeamName = o.optString("awayTeamName");
			JSONObject result = o.optJSONObject("result");
			if (result != null) {
				fixture.mGoalsHomeTeam = result.optInt("goalsHomeTeam", -1);
				fixture.mGoalsAwayTeam = result.optInt("goalsAwayTeam", -1);
				//
				/*
				FIXME: API PROBLEM WITH STATUS OF THE GAME.
				 */
				if (fixture.mGoalsHomeTeam >= 0 && fixture.getGoalsAwayTeam() >= 0) {
					fixture.mStatus = FINISHED;
				}
			}
			return fixture;
		}
		else return null;
	}

	@Nullable
	public static ArrayList<Fixture> parseArray(JSONArray array) {
		if (array != null) {
			ArrayList<Fixture> list = new ArrayList<>();
			int count = array.length();
			for (int i = 0; i < count; i++) {
				try {
					list.add(Fixture.parse(array.getJSONObject(i)));
				} catch (JSONException e) {
					L.e(Fixture.class, e.toString());
				}
			}
			return list;
		}
		else return null;
	}

	public void setChange() {
		mCanChange = true;
		mCanNotified = !mCanNotified;
	}

	public boolean isChange() {
		return mCanChange;
	}

	public boolean isNotified() {
		return mCanNotified;
	}

	public int getID() {
		return mID;
	}

	public int getSoccerSeasonID() {
		return mSoccerSeasonID;
	}

	public long getDate() {
		return mDate;
	}

	public int getStatus() {
		return mStatus;
	}

	public int getMatchday() {
		return mMatchday;
	}

	public int getHomeTeamID() {
		return mHomeTeamID;
	}

	public int getAwayTeamID() {
		return mAwayTeamID;
	}

	public String getHomeTeamName() {
		return mHomeTeamName;
	}

	public String getAwayTeamName() {
		return mAwayTeamName;
	}

	public Integer getGoalsHomeTeam() {
		return mGoalsHomeTeam;
	}

	public Integer getGoalsAwayTeam() {
		return mGoalsAwayTeam;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeByte((byte) (mCanNotified ? 1 : 0));
		dest.writeByte((byte) (mCanChange ? 1 : 0));
		dest.writeInt(mID);
		dest.writeInt(mSoccerSeasonID);
		dest.writeLong(mDate);
		dest.writeInt(mStatus);
		dest.writeInt(mMatchday);
		dest.writeInt(mHomeTeamID);
		dest.writeInt(mAwayTeamID);
		dest.writeString(mHomeTeamName);
		dest.writeString(mAwayTeamName);
		dest.writeInt(mGoalsHomeTeam);
		dest.writeInt(mGoalsAwayTeam);
	}
}
