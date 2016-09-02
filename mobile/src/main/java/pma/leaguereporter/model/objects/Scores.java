package pma.leaguereporter.model.objects;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pma.leaguereporter.util.DBConst;
import pma.leaguereporter.util.L;

public class Scores implements Parcelable {

	private int mSoccerSeasonID;
	private int mRank;
	private String mTeam;
	private int mTeamId;
	private int mPlayedGames;
	private String mCrestURL;
	private int mPoints;
	private int mGoals;
	private int mGoalAgainst;
	private int mGoalDifference;

	protected Scores() {}

	protected Scores(Parcel in) {
		mSoccerSeasonID = in.readInt();
		mRank = in.readInt();
		mTeam = in.readString();
		mTeamId = in.readInt();
		mPlayedGames = in.readInt();
		mCrestURL = in.readString();
		mPoints = in.readInt();
		mGoals = in.readInt();
		mGoalAgainst = in.readInt();
		mGoalDifference = in.readInt();
	}

	public static final Creator<Scores> CREATOR = new Creator<Scores>() {
		@Override
		public Scores createFromParcel(Parcel in) {
			return new Scores(in);
		}

		@Override
		public Scores[] newArray(int size) {
			return new Scores[size];
		}
	};

	@Nullable
	public static Scores parse(Cursor scoresCursor) {
		Scores scores = null;
		if (scoresCursor.moveToFirst()) {
			int col_soccerSeasonId = scoresCursor.getColumnIndex(DBConst.SOCCER_SEASON_ID);
			int col_rank = scoresCursor.getColumnIndex(DBConst.RANK);
			int col_team = scoresCursor.getColumnIndex(DBConst.TEAM);
			int col_teamId = scoresCursor.getColumnIndex(DBConst.TEAM_ID);
			int col_playedGames = scoresCursor.getColumnIndex(DBConst.PLAYED_GAMES);
			int col_crestURL = scoresCursor.getColumnIndex(DBConst.CREST_URL);
			int col_points = scoresCursor.getColumnIndex(DBConst.POINTS);
			int col_goals = scoresCursor.getColumnIndex(DBConst.GOALS);
			int col_goalAgainst = scoresCursor.getColumnIndex(DBConst.GOAL_AGAINST);
			int col_goalDifference = scoresCursor.getColumnIndex(DBConst.GOAL_DIFFERENCE);
			scores = new Scores();
			scores.mSoccerSeasonID = scoresCursor.getInt(col_soccerSeasonId);
			scores.mRank = scoresCursor.getInt(col_rank);
			scores.mTeam = scoresCursor.getString(col_team);
			scores.mTeamId = scoresCursor.getInt(col_teamId);
			scores.mPlayedGames = scoresCursor.getInt(col_playedGames);
			scores.mCrestURL = scoresCursor.getString(col_crestURL);
			scores.mPoints = scoresCursor.getInt(col_points);
			scores.mGoals = scoresCursor.getInt(col_goals);
			scores.mGoalAgainst = scoresCursor.getInt(col_goalAgainst);
			scores.mGoalDifference = scoresCursor.getInt(col_goalDifference);
		}
		return scores;
	}

	@Nullable
	public static ArrayList<Scores> parseArray(Cursor scoresCursor) {
		if (scoresCursor.moveToFirst()) {
			int col_soccerSeasonId = scoresCursor.getColumnIndex(DBConst.SOCCER_SEASON_ID);
			int col_rank = scoresCursor.getColumnIndex(DBConst.RANK);
			int col_team = scoresCursor.getColumnIndex(DBConst.TEAM);
			int col_teamId = scoresCursor.getColumnIndex(DBConst.TEAM_ID);
			int col_playedGames = scoresCursor.getColumnIndex(DBConst.PLAYED_GAMES);
			int col_crestURL = scoresCursor.getColumnIndex(DBConst.CREST_URL);
			int col_points = scoresCursor.getColumnIndex(DBConst.POINTS);
			int col_goals = scoresCursor.getColumnIndex(DBConst.GOALS);
			int col_goalAgainst = scoresCursor.getColumnIndex(DBConst.GOAL_AGAINST);
			int col_goalDifference = scoresCursor.getColumnIndex(DBConst.GOAL_DIFFERENCE);
			ArrayList<Scores> list = new ArrayList<>();
			do {
				Scores scores = new Scores();
				scores.mSoccerSeasonID = scoresCursor.getInt(col_soccerSeasonId);
				scores.mRank = scoresCursor.getInt(col_rank);
				scores.mTeam = scoresCursor.getString(col_team);
				scores.mTeamId = scoresCursor.getInt(col_teamId);
				scores.mPlayedGames = scoresCursor.getInt(col_playedGames);
				scores.mCrestURL = scoresCursor.getString(col_crestURL);
				scores.mPoints = scoresCursor.getInt(col_points);
				scores.mGoals = scoresCursor.getInt(col_goals);
				scores.mGoalAgainst = scoresCursor.getInt(col_goalAgainst);
				scores.mGoalDifference = scoresCursor.getInt(col_goalDifference);
				list.add(scores);
			} while (scoresCursor.moveToNext());
			return list;
		}
		else return null;
	}

	@Nullable
	public static Scores parse(int soccerSeasonId, JSONObject o) {
		if (o != null) {
			Scores scores = new Scores();
			scores.mSoccerSeasonID = soccerSeasonId;
			scores.mRank = o.optInt("rank");
			scores.mTeam = o.optString("team");
			scores.mTeamId = o.optInt("teamId");
			scores.mPlayedGames = o.optInt("playedGames");
			scores.mCrestURL = o.optString("crestURI");
			scores.mPoints = o.optInt("points");
			scores.mGoals = o.optInt("goals");
			scores.mGoalAgainst = o.optInt("goalsAgainst");
			scores.mGoalDifference = o.optInt("goalDifference");
			return scores;
		}
		else return null;
	}

	@Nullable
	public static ArrayList<Scores> parseArray(int soccerSeasonId, JSONArray array) {
		if (array != null) {
			ArrayList<Scores> list = new ArrayList<>();
			int count = array.length();
			for (int i = 0; i < count; i++) {
				try {
					list.add(Scores.parse(soccerSeasonId, array.getJSONObject(i)));
				} catch (JSONException e) {
					L.e(Scores.class, e.toString());
				}
			}
			return list;
		}
		else return null;
	}

	public int getSoccerSeasonID() {
		return mSoccerSeasonID;
	}

	public int getRank() {
		return mRank;
	}

	public String getTeam() {
		return mTeam;
	}

	public int getTeamId() {
		return mTeamId;
	}

	public int getPlayedGames() {
		return mPlayedGames;
	}

	public String getCrestURI() {
		return mCrestURL;
	}

	public int getPoints() {
		return mPoints;
	}

	public int getGoals() {
		return mGoals;
	}

	public int getGoalAgainst() {
		return mGoalAgainst;
	}

	public int getGoalDifference() {
		return mGoalDifference;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeInt(mSoccerSeasonID);
		dest.writeInt(mRank);
		dest.writeString(mTeam);
		dest.writeInt(mTeamId);
		dest.writeInt(mPlayedGames);
		dest.writeString(mCrestURL);
		dest.writeInt(mPoints);
		dest.writeInt(mGoals);
		dest.writeInt(mGoalAgainst);
		dest.writeInt(mGoalDifference);
	}

}
