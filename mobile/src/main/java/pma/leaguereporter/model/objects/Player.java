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

public class Player implements Parcelable {

	private int mTeamID;
	private int mID;
	private String mName;
	private String mPosition;
	private int mJerseyNumber;
	private long mDateOfBirth;
	private String mNationality;
	private long mContractUntil;
	private String mMarketValue;

	protected Player() {}

	protected Player(Parcel in) {
		mTeamID = in.readInt();
		mID = in.readInt();
		mName = in.readString();
		mPosition = in.readString();
		mJerseyNumber = in.readInt();
		mDateOfBirth = in.readLong();
		mNationality = in.readString();
		mContractUntil = in.readLong();
		mMarketValue = in.readString();
	}

	public static final Creator<Player> CREATOR = new Creator<Player>() {
		@Override
		public Player createFromParcel(Parcel in) {
			return new Player(in);
		}

		@Override
		public Player[] newArray(int size) {
			return new Player[size];
		}
	};

	@Nullable
	public static Player parse(Cursor playerCursor) {
		Player player = null;
		if (playerCursor.moveToFirst()) {
			int col_teamId = playerCursor.getColumnIndex(DBConst.TEAM_ID);
			int col_id = playerCursor.getColumnIndex(DBConst.ID);
			int col_name = playerCursor.getColumnIndex(DBConst.NAME);
			int col_position = playerCursor.getColumnIndex(DBConst.POSITION);
			int col_jerseyNumber = playerCursor.getColumnIndex(DBConst.JERSEY_NUMBER);
			int col_dateOfBirth = playerCursor.getColumnIndex(DBConst.DATE_OF_BIRTH);
			int col_nationality = playerCursor.getColumnIndex(DBConst.NATIONALITY);
			int col_contractUntil = playerCursor.getColumnIndex(DBConst.CONTRACT_UNTIL);
			int col_marketValue = playerCursor.getColumnIndex(DBConst.MARKET_VALUE);
			player = new Player();
			player.mTeamID = playerCursor.getInt(col_teamId);
			player.mID = playerCursor.getInt(col_id);
			player.mName = playerCursor.getString(col_name);
			player.mPosition = playerCursor.getString(col_position);
			player.mJerseyNumber = playerCursor.getInt(col_jerseyNumber);
			player.mDateOfBirth = playerCursor.getLong(col_dateOfBirth);
			player.mNationality = playerCursor.getString(col_nationality);
			player.mContractUntil = playerCursor.getLong(col_contractUntil);
			player.mMarketValue = playerCursor.getString(col_marketValue);
		}
		return player;
	}

	@Nullable
	public static ArrayList<Player> parseArray(Cursor playerCursor) {
		if (playerCursor.moveToFirst()) {
			int col_teamId = playerCursor.getColumnIndex(DBConst.TEAM_ID);
			int col_id = playerCursor.getColumnIndex(DBConst.ID);
			int col_name = playerCursor.getColumnIndex(DBConst.NAME);
			int col_position = playerCursor.getColumnIndex(DBConst.POSITION);
			int col_jerseyNumber = playerCursor.getColumnIndex(DBConst.JERSEY_NUMBER);
			int col_dateOfBirth = playerCursor.getColumnIndex(DBConst.DATE_OF_BIRTH);
			int col_nationality = playerCursor.getColumnIndex(DBConst.NATIONALITY);
			int col_contractUntil = playerCursor.getColumnIndex(DBConst.CONTRACT_UNTIL);
			int col_marketValue = playerCursor.getColumnIndex(DBConst.MARKET_VALUE);
			ArrayList<Player> list = new ArrayList<>();
			do {
				Player player = new Player();
				player.mTeamID = playerCursor.getInt(col_teamId);
				player.mID = playerCursor.getInt(col_id);
				player.mName = playerCursor.getString(col_name);
				player.mPosition = playerCursor.getString(col_position);
				player.mJerseyNumber = playerCursor.getInt(col_jerseyNumber);
				player.mDateOfBirth = playerCursor.getLong(col_dateOfBirth);
				player.mNationality = playerCursor.getString(col_nationality);
				player.mContractUntil = playerCursor.getLong(col_contractUntil);
				player.mMarketValue = playerCursor.getString(col_marketValue);
				list.add(player);
			} while (playerCursor.moveToNext());
			return list;
		}
		else return null;
	}

	@Nullable
	public static Player parse(int teamId, JSONObject o) {
		if (o != null) {
			Player player = new Player();
			player.mTeamID = teamId;
			player.mID = o.optInt("id");
			player.mName = o.optString("name");
			player.mPosition = o.optString("position");
			player.mJerseyNumber = o.optInt("jerseyNumber");
			String dateOfBirth = o.optString("dateOfBirth", null);
			if (dateOfBirth != null) {
				try {
					player.mDateOfBirth = new SimpleDateFormat(
							"yyyy-MM-dd",
							Locale.getDefault())
							.parse(dateOfBirth)
							.getTime();
				} catch (ParseException e) {
					L.e(Player.class, e.toString());
				}
			}
			player.mNationality = o.optString("nationality");
			String contractUntil = o.optString("contractUntil", null);
			if (contractUntil != null) {
				try {
					player.mContractUntil = new SimpleDateFormat(
							"yyyy-MM-dd",
							Locale.getDefault())
							.parse(contractUntil)
							.getTime();
				} catch (ParseException e) {
					L.e(Player.class, e.toString());
				}
			}
			player.mMarketValue = o.optString("marketValue");
			return player;
		}
		else return null;
	}

	@Nullable
	public static ArrayList<Player> parseArray(int teamId, JSONArray array) {
		if (array != null) {
			ArrayList<Player> list = new ArrayList<>();
			int count = array.length();
			for (int i = 0; i < count; i++) {
				try {
					list.add(Player.parse(teamId, array.getJSONObject(i)));
				} catch (JSONException e) {
					L.e(Player.class, e.toString());
				}
			}
			return list;
		}
		else return null;
	}

	public int getTeamID() {
		return mTeamID;
	}

	public int getID() {
		return mID;
	}

	public String getName() {
		return mName;
	}

	public String getPosition() {
		return mPosition;
	}

	public int getJerseyNumber() {
		return mJerseyNumber;
	}

	public long getDateOfBirth() {
		return mDateOfBirth;
	}

	public String getNationality() {
		return mNationality;
	}

	public long getContractUntil() {
		return mContractUntil;
	}

	public String getMarketValue() {
		return mMarketValue;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeInt(mTeamID);
		dest.writeInt(mID);
		dest.writeString(mName);
		dest.writeString(mPosition);
		dest.writeInt(mJerseyNumber);
		dest.writeLong(mDateOfBirth);
		dest.writeString(mNationality);
		dest.writeLong(mContractUntil);
		dest.writeString(mMarketValue);
	}

}
