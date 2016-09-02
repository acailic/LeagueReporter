package pma.leaguereporter.util;

public class Const {

	public static final String SUBMITTED = "submitted";
	public static final String ACTION_NOTIFICATION_BROADCAST = ".model.receivers.NotificationBroadcastReceiver";

	public static final String PREF_LAST_UPDATE_LEAGUES = "last_update_leagues";
	public static final String PREF_LAST_UPDATE_FIXTURES = "last_update_fixtures";
	public static final String PREF_LAST_UPDATE_SCORES = "last_update_scores";
	public static final String PREF_LAST_UPDATE_TEAM = "last_update_team";
	public static final String PREF_LAST_UPDATE_PLAYERS = "last_update_players";
	public static final String PREF_LAST_UPDATE_TEAM_FIXTURES = "last_update_teams_fixtures";

	public static final int REQUEST_CODE_NOTIFIED_FIXTURE = 1;

	public static final int GROUP_FIXTURES = 1;
	public static final int GROUP_MATCHDAYS = 2;

	public static final int ERROR_CODE_PARSE_ERROR = 1;
	public static final int ERROR_CODE_RESULT_NULL = 2;
	public static final int ERROR_CODE_RESULT_EMPTY = 4;

	public static final int EVENT_CODE_SELECT_LEAGUE = 11;
	public static final int EVENT_CODE_SELECT_FIXTURE_INFO = 12;
	public static final int EVENT_CODE_SELECT_SCORES = 13;
	public static final int EVENT_CODE_SELECT_FIXTURES = 14;
	public static final int EVENT_CODE_SELECT_PLAYERS = 15;
	public static final int EVENT_CODE_SELECT_PLAYER_INFO = 16;
	public static final int EVENT_CODE_SHOW_SCORES_TABLE = 17;
	public static final int EVENT_CODE_SHOW_SETTINGS = 18;
	public static final int EVENT_CODE_SHOW_ABOUT = 19;
	public static final int EVENT_CODE_SHARE_FIXTURE = 110;
	public static final int EVENT_CODE_SHARE_FIXTURES_LIST = 111;
	public static final int EVENT_CODE_SHARE_SCORES_TABLE = 112;
	public static final int EVENT_CODE_SHARE_PLAYERS_LIST = 113;
	public static final int EVENT_CODE_SHARE_TEAM_FIXTURES_LIST = 114;

}
