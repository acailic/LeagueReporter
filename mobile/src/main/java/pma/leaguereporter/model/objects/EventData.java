package pma.leaguereporter.model.objects;

import java.util.ArrayList;

public class EventData {

	private int mCode;
	private League mLeague;
	private Fixture mFixture;
	private Scores mScores;
	private Team mTeam;
	private Player mPlayer;
	private ArrayList<Fixture> mFixturesList;
	private ArrayList<Scores> mScoresList;
	private ArrayList<Player> mPlayersList;

	public EventData(int code) {
		mCode = code;
	}

	public int gecCode() {
		return mCode;
	}

	public League getLeague() {
		return mLeague;
	}

	public Fixture getFixture() {
		return mFixture;
	}

	public Scores getScores() {
		return mScores;
	}

	public Team getTeam() {
		return mTeam;
	}

	public Player getPlayer() {
		return mPlayer;
	}

	public ArrayList<Fixture> getFixturesList() {
		return mFixturesList;
	}

	public ArrayList<Scores> getScoresList() {
		return mScoresList;
	}

	public ArrayList<Player> getPlayersList() {
		return mPlayersList;
	}

	public EventData setLeague(League league) {
		mLeague = league;
		return this;
	}

	public EventData setFixture(Fixture fixture) {
		mFixture = fixture;
		return this;
	}

	public EventData setScores(Scores scores) {
		mScores = scores;
		return this;
	}

	public EventData setTeam(Team team) {
		mTeam = team;
		return this;
	}

	public EventData setPlayer(Player player) {
		mPlayer = player;
		return this;
	}

	public EventData setFixturesList(ArrayList<Fixture> list) {
		mFixturesList = list;
		return this;
	}

	public EventData setScoresList(ArrayList<Scores> list) {
		mScoresList = list;
		return this;
	}

	public EventData setPlayersList(ArrayList<Player> list) {
		mPlayersList = list;
		return this;
	}

}
