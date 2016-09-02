package pma.leaguereporter.controllers;

import android.content.Context;
import android.support.annotation.Nullable;
import pma.leaguereporter.R;
import pma.leaguereporter.model.Connection;

public class Api {

	/**
	 * This method returned json string of leagues
	 * @return json string of leagues
	 */
	public static String getLeagueByYear(Context context, int year) {
		Connection connection = getConnection(context);
		return connection.request(getBaseUrl() + "soccerseasons?season=" + String.valueOf(year));
	}

	/**
	 * This method returned json string of fixture details
	 * @param id id fixture
	 * @return json string of fixture details
	 */
	public static String getFixtureDetailsById(Context context, int id) {
		Connection connection = getConnection(context);
		return connection.request(getBaseUrl() + "fixtures/" + String.valueOf(id));
	}

	/**
	 * This method returned json string of fixtures by matchday
	 * @return json string of fixture
	 */
	public static String getFixturesByMatchday(Context context, int soccerseasonId, int matchday) {
		return getFixtures(context, soccerseasonId, "matchday=" + String.valueOf(matchday));
	}

	/**
	 * This method returned json string of fixtures by id
	 * @return json string of fixture
	 */
	public static String getFixtures(Context context, int soccerseasonId) {
		return getFixtures(context, soccerseasonId, null);
	}

	/**
	 * This method returned json string of fixtures by id with filter
	 * @return json string of fixture
	 */
	public static String getFixtures(Context context, int soccerseasonId, @Nullable String filter) {
		Connection connection = getConnection(context);
		StringBuilder request = new StringBuilder()
				.append(getBaseUrl())
				.append("soccerseasons/")
				.append(String.valueOf(soccerseasonId))
				.append("/fixtures");
		if (filter != null) request.append("?")
				.append(filter);
		else request.append("?timeFrame=n7");
		return connection.request(request.toString());
	}

	/**
	 * This method returned json string of scores
	 * @return json string of scores
	 */
	public static String getScores(Context context, int soccerSeasonId) {
		return getScores(context, soccerSeasonId, 0);
	}

	/**
	 * This method returned json string of scores
	 * @return json string of scores
	 */
	public static String getScores(Context context, int soccerSeasonId, int matchday) {
		Connection connection = getConnection(context);
		StringBuilder request = new StringBuilder()
				.append(getBaseUrl())
				.append("soccerseasons/")
				.append(soccerSeasonId)
				.append("/leagueTable");
		if (matchday > 0) {
			request.append("?matchday=").append(String.valueOf(matchday));
		}
		return connection.request(request.toString());
	}

	/**
	 * This method returned json string of team
	 * @return json string of team
	 */
	public static String getTeam(Context context, int teamId) {
		Connection connection = getConnection(context);
		StringBuilder request = new StringBuilder()
				.append(getBaseUrl())
				.append("teams/")
				.append(String.valueOf(teamId));
		return connection.request(request.toString());
	}

	/**
	 * This method returned json string fixtures by teamId
	 * @return json string of fixtures
	 */
	public static String getTeamFixtures(Context context, int teamId) {
		return getTeamFixtures(context, teamId, null);
	}

	/**
	 * This method returned json string of fixtures with filter
	 * @return json string of fixtures
	 */
	public static String getTeamFixtures(Context context, int teamId, @Nullable String filter) {
		Connection connection = getConnection(context);
		StringBuilder request = new StringBuilder()
				.append(getBaseUrl())
				.append("teams/")
				.append(String.valueOf(teamId))
				.append("/fixtures");
		if (filter != null) request.append("?")
				.append(filter);
		else request.append("?timeFrame=n10");
		return connection.request(request.toString());
	}

	/**
	 * This method returned json string of players with teamId
	 * @return json string of players
	 */
	public static String getTeamPlayers(Context context, int teamId) {
		Connection connection = getConnection(context);
		StringBuilder request = new StringBuilder()
				.append(getBaseUrl())
				.append("teams/")
				.append(String.valueOf(teamId))
				.append("/players");
		return connection.request(request.toString());
	}

	/**
	 * This method returned Connection object
	 * @return connection object
	 */
	private static Connection getConnection(Context context) {
		return new Connection.Creator()
				.putHeader("X-Auth-Token", context.getString(R.string.api_key))
				.putHeader("X-Response-Control", context.getString(R.string.api_response_control_mini))
				.create();
	}

	/**
	 * This method returned base api url
	 */
	private static String getBaseUrl() {
		return "http://api.football-data.org/v1/";
	}
}
