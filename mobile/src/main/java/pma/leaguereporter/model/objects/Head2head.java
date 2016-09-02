package pma.leaguereporter.model.objects;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import pma.leaguereporter.util.DBConst;
import pma.leaguereporter.util.L;

public class Head2head implements Parcelable {

	private int mFixtureID;
	private int mCount;
	private long mTimeFrameStart;
	private long mTimeFrameEnd;
	private int mHomeTeamWins;
	private int mAwayTeamWins;
	private int mDraws;
	private ArrayList<Fixture> mFixtures;

	protected Head2head() {}

	protected Head2head(Parcel in) {
		mFixtureID = in.readInt();
		mCount = in.readInt();
		mTimeFrameStart = in.readLong();
		mTimeFrameEnd = in.readLong();
		mHomeTeamWins = in.readInt();
		mAwayTeamWins = in.readInt();
		mDraws = in.readInt();
		mFixtures = in.createTypedArrayList(Fixture.CREATOR);
	}

	public static final Creator<Head2head> CREATOR = new Creator<Head2head>() {
		@Override
		public Head2head createFromParcel(Parcel in) {
			return new Head2head(in);
		}

		@Override
		public Head2head[] newArray(int size) {
			return new Head2head[size];
		}
	};

	@Nullable
	public static Head2head parse(Cursor head2headCursor) {
		Head2head head2head = null;
		if (head2headCursor.moveToFirst()) {
			int col_fixture_id = head2headCursor.getColumnIndex(DBConst.FIXTURE_ID);
			int col_count = head2headCursor.getColumnIndex(DBConst.COUNT);
			int col_time_frame_start = head2headCursor.getColumnIndex(DBConst.TIME_FRAME_START);
			int col_time_frame_end = head2headCursor.getColumnIndex(DBConst.TIME_FRAME_END);
			int col_home_team_wins = head2headCursor.getColumnIndex(DBConst.HOME_TEAM_WINS);
			int col_away_team_wins = head2headCursor.getColumnIndex(DBConst.AWAY_TEAM_WINS);
			int col_draws = head2headCursor.getColumnIndex(DBConst.DRAWS);
			head2head = new Head2head();
			head2head.mFixtureID = head2headCursor.getInt(col_fixture_id);
			head2head.mCount = head2headCursor.getInt(col_count);
			head2head.mTimeFrameStart = head2headCursor.getLong(col_time_frame_start);
			head2head.mTimeFrameEnd = head2headCursor.getLong(col_time_frame_end);
			head2head.mHomeTeamWins = head2headCursor.getInt(col_home_team_wins);
			head2head.mAwayTeamWins = head2headCursor.getInt(col_away_team_wins);
			head2head.mDraws = head2headCursor.getInt(col_draws);
		}
		return head2head;
	}

	@Nullable
	public static Head2head parse(int fixture_id, JSONObject o) {
		if (o != null) {
			Head2head head2head = new Head2head();
			head2head.mFixtureID = fixture_id;
			head2head.mCount = o.optInt("count");
			String timeFrameStart = o.optString("timeFrameStart", null);
			if (timeFrameStart != null) {
				try {
					head2head.mTimeFrameStart = new SimpleDateFormat(
							"yyyy-MM-dd",
							Locale.getDefault())
							.parse(timeFrameStart)
							.getTime();
				} catch (ParseException e) {
					L.e(Head2head.class, e.toString());
				}
			}
			String timeFrameEnd = o.optString("timeFrameEnd", null);
			if (timeFrameEnd != null) {
				try {
					head2head.mTimeFrameEnd = new SimpleDateFormat(
							"yyyy-MM-dd",
							Locale.getDefault())
							.parse(timeFrameEnd)
							.getTime();
				} catch (ParseException e) {
					L.e(Head2head.class, e.toString());
				}
			}
			head2head.mHomeTeamWins = o.optInt("homeTeamWins");
			head2head.mAwayTeamWins = o.optInt("awayTeamWins");
			head2head.mDraws = o.optInt("draws");
			head2head.mFixtures = Fixture.parseArray(o.optJSONArray("fixtures"));
			return head2head;
		}
		else return null;
	}

	public int getFixtureID() {
		return mFixtureID;
	}

	public int getCount() {
		return mCount;
	}

	public long getTimeFrameStart() {
		return mTimeFrameStart;
	}

	public long getTimeFrameEnd() {
		return mTimeFrameEnd;
	}

	public int getHomeTeamWins() {
		return mHomeTeamWins;
	}

	public int getAwayTeamWins() {
		return mAwayTeamWins;
	}

	public int getDraws() {
		return mDraws;
	}

	public ArrayList<Fixture> getFixtures() {
		return mFixtures;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeInt(mFixtureID);
		dest.writeInt(mCount);
		dest.writeLong(mTimeFrameStart);
		dest.writeLong(mTimeFrameEnd);
		dest.writeInt(mHomeTeamWins);
		dest.writeInt(mAwayTeamWins);
		dest.writeInt(mDraws);
		dest.writeTypedList(mFixtures);
	}
}
