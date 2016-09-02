package pma.leaguereporter.model.objects;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import org.json.JSONObject;

import pma.leaguereporter.util.DBConst;

public class Team implements Parcelable {

	private int mID;
	private boolean mIsChanged;
	private boolean mIsFavorite;
	private String mName;
	private String mShortName;
	private String mSquadMarketValue;
	private String mCrestURL;

	protected Team() {}

	protected Team(Parcel in) {
		mID = in.readInt();
		mIsChanged = in.readByte() != 0;
		mIsFavorite = in.readByte() != 0;
		mName = in.readString();
		mShortName = in.readString();
		mSquadMarketValue = in.readString();
		mCrestURL = in.readString();
	}

	public static final Creator<Team> CREATOR = new Creator<Team>() {
		@Override
		public Team createFromParcel(Parcel in) {
			return new Team(in);
		}

		@Override
		public Team[] newArray(int size) {
			return new Team[size];
		}
	};

	@Nullable
	public static Team parse(Cursor teamCursor) {
		Team team = null;
		if (teamCursor.moveToFirst()) {
			int col_id = teamCursor.getColumnIndex(DBConst.ID);
			int col_isFavorite = teamCursor.getColumnIndex(DBConst.CAN_FAVORITE);
			int col_name = teamCursor.getColumnIndex(DBConst.NAME);
			int col_shortName = teamCursor.getColumnIndex(DBConst.SHORT_NAME);
			int col_squadMarketValue = teamCursor.getColumnIndex(DBConst.SQUAD_MARKET_VALUE);
			int col_crestUrl = teamCursor.getColumnIndex(DBConst.CREST_URL);
			team = new Team();
			team.mID = teamCursor.getInt(col_id);
			team.mIsFavorite = teamCursor.getInt(col_isFavorite) == 1;
			team.mName = teamCursor.getString(col_name);
			team.mShortName = teamCursor.getString(col_shortName);
			team.mSquadMarketValue = teamCursor.getString(col_squadMarketValue);
			team.mCrestURL = teamCursor.getString(col_crestUrl);
		}
		return team;
	}

	@Nullable
	public static Team parse(JSONObject o) {
		if (o != null) {
			Team team = new Team();
			team.mID = o.optInt("id");
			team.mName = o.optString("name");
			team.mShortName = o.optString("shortName");
			team.mSquadMarketValue = o.optString("squadMarketValue");
			team.mCrestURL = o.optString("crestUrl");
			return team;
		}
		else return null;
	}

	public void setFavorite(boolean isFavorite) {
		mIsFavorite = isFavorite;
		mIsChanged = true;
	}

	public int getID() {
		return mID;
	}

	public boolean isChanged() {
		return mIsChanged;
	}

	public boolean getIsFavorite() {
		return mIsFavorite;
	}

	public String getName() {
		return mName;
	}

	public String getShortName() {
		return mShortName;
	}

	public String getSquadMarketValue() {
		return mSquadMarketValue;
	}

	public String getCrestURL() {
		return mCrestURL;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeInt(mID);
		dest.writeByte((byte) (mIsChanged ? 1 : 0));
		dest.writeByte((byte) (mIsFavorite ? 1 : 0));
		dest.writeString(mName);
		dest.writeString(mShortName);
		dest.writeString(mSquadMarketValue);
		dest.writeString(mCrestURL);
	}
}
