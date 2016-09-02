package pma.leaguereporter.controllers;

import android.content.Context;
import android.os.Handler;

import pma.leaguereporter.model.adapters.FixturesAdapter;
import pma.leaguereporter.model.comparators.ComparatorFixtures;
import pma.leaguereporter.model.comparators.ComparatorPlayers;
import pma.leaguereporter.model.comparators.ComparatorScores;
import pma.leaguereporter.model.interfaces.OnResultListener;
import pma.leaguereporter.model.objects.Fixture;
import pma.leaguereporter.model.objects.Head2head;
import pma.leaguereporter.model.objects.League;
import pma.leaguereporter.model.objects.Player;
import pma.leaguereporter.model.objects.Scores;
import pma.leaguereporter.model.objects.Team;
import pma.leaguereporter.model.receivers.NotificationBroadcastReceiver;
import pma.leaguereporter.util.Configs;
import pma.leaguereporter.util.Const;
import pma.leaguereporter.util.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Controller {

	private static final int NUMBERS_OF_CORES = Runtime.getRuntime().availableProcessors();
	private static Controller mInstance;
	private ThreadPoolExecutor mExecutor;
	private Handler mHandler;
	private NotificationBroadcastReceiver mReceiver;

	public static synchronized void init(Context context) {
		DBManager.init(context);
		if (mInstance == null) synchronized (Controller.class) {
			if (mInstance == null) {
				mInstance = new Controller();
				mInstance.mHandler = new Handler();
				mInstance.mExecutor = new ThreadPoolExecutor(
						1,
						NUMBERS_OF_CORES,
						5000,
						TimeUnit.MILLISECONDS,
						new LinkedBlockingQueue<Runnable>()
				);
				mInstance.mReceiver = new NotificationBroadcastReceiver();
			}
		}
	}

	public static synchronized Controller getInstance() {
		return mInstance;
	}

	protected Controller() {}

	public void getListOfLeagues(final Context context, final OnGetLeagues callback) {
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String resultCurrent = null, resultLast = null;
				if (canUpdateLeaguesList(context)) {
					int year = Calendar.getInstance().get(Calendar.YEAR);
					resultCurrent= Api.getLeagueByYear(context, year);
					if (resultCurrent != null) resultLast = Api.getLeagueByYear(context, year - 1);
				}
				if (resultCurrent != null && resultLast != null) {
					try {
						JSONArray currentArray = new JSONArray(resultCurrent);
						JSONArray lastArray = new JSONArray(resultLast);
						final ArrayList<League> list = League.parseArray(currentArray);
						list.addAll(League.parseArray(lastArray));
						if (!list.isEmpty()) {
							PreferencesManager.from(context).setLong(
									Const.PREF_LAST_UPDATE_LEAGUES,
									System.currentTimeMillis())
									.commit();
							DBManager.setLeaguesList(list);
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									callback.onSuccess(list);
								}
							});
						}
						else mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Const.ERROR_CODE_RESULT_EMPTY);
							}
						});

					} catch (JSONException e) {
						L.e(Controller.class, e.toString());
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Const.ERROR_CODE_PARSE_ERROR);
							}
						});
					}
				}
				else {
					final ArrayList<League> dbList = DBManager.getLeaguesList();
					if (dbList != null) mHandler.post(new Runnable() {
						@Override
						public void run() {
							callback.onSuccess(dbList);
						}
					});
					else mHandler.post(new Runnable() {
						@Override
						public void run() {
							callback.onError(Const.ERROR_CODE_RESULT_NULL);
						}
					});
				}
			}
		});
	}

	public void getListOfFixtures(
			final Context context, final int soccerseasonId, final int matchday,
			final OnGetFixtures callback) {
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String result = null;
				if (canUpdateFixturesList(context, soccerseasonId, matchday)) {
					result = Api.getFixturesByMatchday(context, soccerseasonId, matchday);
				}
				if (result != null) {
					try {
						JSONArray array = new JSONObject(result).getJSONArray("fixtures");
						final ArrayList<Fixture> list = Fixture.parseArray(array);
						if (list != null && !list.isEmpty()) {
							PreferencesManager.from(context).setLong(
									getFixturesPref(soccerseasonId, matchday),
									System.currentTimeMillis())
									.commit();
							ComparatorFixtures.sortByDate(list);
							DBManager.setFixturesList(list);
							final ArrayList<Fixture> dbList = DBManager.getFixturesListByMatchday(
									soccerseasonId, matchday
							);
							if (dbList != null) {
								ComparatorFixtures.sortByDate(dbList);
								mHandler.post(new Runnable() {
									@Override
									public void run() {
										callback.onSuccess(dbList);
									}
								});
							}
							else mHandler.post(new Runnable() {
								@Override
								public void run() {
									callback.onError(Const.ERROR_CODE_RESULT_NULL);
								}
							});
						}
						else mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Const.ERROR_CODE_RESULT_EMPTY);
							}
						});
					} catch (JSONException e) {
						L.e(Controller.class, e.toString());
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Const.ERROR_CODE_PARSE_ERROR);
							}
						});
					}
				}
				else {
					final ArrayList<Fixture> dbList = DBManager.getFixturesListByMatchday(
							soccerseasonId, matchday
					);
					if (dbList != null) {
						ComparatorFixtures.sortByDate(dbList);
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onSuccess(dbList);
							}
						});
					}
					else mHandler.post(new Runnable() {
						@Override
						public void run() {
							callback.onError(Const.ERROR_CODE_RESULT_NULL);
						}
					});
				}
			}
		});
	}

	public void getScores(
			final Context context, final int soccerSeasonId, final OnGetScores callback) {
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String result = null;
				if (canUpdateScoresTable(context, soccerSeasonId)) {
					result = Api.getScores(context, soccerSeasonId);
				}
				if (result != null) {
					try {
						JSONArray array = new JSONObject(result).getJSONArray("standing");
						final ArrayList<Scores> list = Scores.parseArray(soccerSeasonId, array);
						if (list != null && !list.isEmpty()) {
							PreferencesManager.from(context).setLong(
									getScoresPref(soccerSeasonId),
									System.currentTimeMillis())
									.commit();
							ComparatorScores.sortByPoints(list);
							DBManager.setScoresList(list);
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									callback.onSuccess(list);
								}
							});
						}
						else mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Const.ERROR_CODE_RESULT_EMPTY);
							}
						});
					} catch (JSONException e) {
						L.e(Controller.class, e.toString());
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Const.ERROR_CODE_PARSE_ERROR);
							}
						});
					}
				}
				else {
					final ArrayList<Scores> dbList = DBManager.getScoresList(soccerSeasonId);
					if (dbList != null) {
						ComparatorScores.sortByPoints(dbList);
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onSuccess(dbList);
							}
						});
					}
					else mHandler.post(new Runnable() {
						@Override
						public void run() {
							callback.onError(Const.ERROR_CODE_RESULT_NULL);
						}
					});
				}
			}
		});
	}

	public void getFixtureDetails(
			final Context context, final int fixtureId, final OnGetFixtureDetails callback) {
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				final Head2head dbHead2head = DBManager.getHead2head(fixtureId);
				if (dbHead2head != null) mHandler.post(new Runnable() {
					@Override
					public void run() {
						callback.onSuccess(dbHead2head);
					}
				});
				String result = Api.getFixtureDetailsById(context, fixtureId);
				if (result != null) {
					try {
						JSONObject object = new JSONObject(result).getJSONObject("head2head");
						final Head2head head2head = Head2head.parse(fixtureId, object);
						if (head2head != null) {
							DBManager.setHead2head(dbHead2head);
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									callback.onSuccess(head2head);
								}
							});
						}
						else mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Const.ERROR_CODE_RESULT_NULL);
							}
						});
					} catch (JSONException e) {
						L.e(Controller.class, e.toString());
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Const.ERROR_CODE_PARSE_ERROR);
							}
						});
					}
				}
				else mHandler.post(new Runnable() {
					@Override
					public void run() {
						callback.onError(Const.ERROR_CODE_RESULT_NULL);
					}
				});
			}
		});
	}

	public void getTeam(
			final Context context,
			final int teamId,
			final OnGetTeam callback) {
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String result = null;
				if (canUpdateTeam(context, teamId)) {
					result = Api.getTeam(context, teamId);
				}
				if (result != null) {
					try {
						JSONObject object = new JSONObject(result);
						final Team team = Team.parse(object);
						if (team != null) {
							PreferencesManager.from(context).setLong(
									getTeamPref(teamId),
									System.currentTimeMillis())
									.commit();
							DBManager.setTeam(team);
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									callback.onSuccess(team);
								}
							});
						}
						else mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Const.ERROR_CODE_RESULT_NULL);
							}
						});
					} catch (JSONException e) {
						L.e(Controller.class, e.toString());
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Const.ERROR_CODE_PARSE_ERROR);
							}
						});
					}
				}
				else {
					final Team dbTeam = DBManager.getTeam(teamId);
					if (dbTeam != null) mHandler.post(new Runnable() {
						@Override
						public void run() {
							callback.onSuccess(dbTeam);
						}
					});
					else mHandler.post(new Runnable() {
						@Override
						public void run() {
							callback.onError(Const.ERROR_CODE_RESULT_NULL);
						}
					});
				}
			}
		});
	}

	public void getListOfPlayers(
			final Context context, final int teamId, final OnGetPlayers callback) {
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String result = null;
				if (canUpdatePlayersList(context, teamId)) {
					result = Api.getTeamPlayers(context, teamId);
				}
				if (result != null) {
					try {
						JSONArray array = new JSONObject(result).getJSONArray("players");
						final ArrayList<Player> list = Player.parseArray(teamId, array);
						if (list != null && !list.isEmpty()) {
							ComparatorPlayers.sortByJerseyNumber(list);
							DBManager.setPlayersList(list);
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									callback.onSuccess(list);
								}
							});
						}
						else mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Const.ERROR_CODE_RESULT_NULL);
							}
						});
					} catch (JSONException e) {
						L.e(Controller.class, e.toString());
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Const.ERROR_CODE_PARSE_ERROR);
							}
						});
					}
				}
				else {
					final ArrayList<Player> dbList = DBManager.getPlayerList(teamId);
					if (dbList != null) {
						ComparatorPlayers.sortByJerseyNumber(dbList);
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onSuccess(dbList);
							}
						});
					}
					else mHandler.post(new Runnable() {
						@Override
						public void run() {
							callback.onError(Const.ERROR_CODE_RESULT_NULL);
						}
					});
				}
			}
		});
	}

	public void getListOfTeamFixtures(
			final Context context, final int soccerseasonId,
			final int teamId, final OnGetFixtures callback) {
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String result = null;
				if (canUpdateTeamFixturesList(context, soccerseasonId, teamId)) {
					result = Api.getTeamFixtures(context, teamId);
				}
				if (result != null) {
					try {
						JSONArray array = new JSONObject(result).getJSONArray("fixtures");
						final ArrayList<Fixture> list = Fixture.parseArray(array);
						if (list != null && !list.isEmpty()) {
							ComparatorFixtures.sortByDate(list);
							DBManager.setFixturesList(list);
							/*final ArrayList<Fixture> dbList = DBManager.getFixturesListUpcoming(
									soccerseasonId, teamId
							);*/
							if (list != null) {
								ComparatorFixtures.sortByDate(list);
								mHandler.post(new Runnable() {
									@Override
									public void run() {
										callback.onSuccess(list);
									}
								});
							}
							else mHandler.post(new Runnable() {
								@Override
								public void run() {
									callback.onError(Const.ERROR_CODE_RESULT_EMPTY);
								}
							});
						}
						else mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Const.ERROR_CODE_RESULT_EMPTY);
							}
						});
					} catch (JSONException e) {
						L.e(Controller.class, e.toString());
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Const.ERROR_CODE_PARSE_ERROR);
							}
						});
					}
				}
				else {
					final ArrayList<Fixture> dbList = DBManager.getFixturesListUpcoming(
							soccerseasonId, teamId
					);
					if (dbList != null) {
						ComparatorFixtures.sortByDate(dbList);
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onSuccess(dbList);
							}
						});
					}
					else mHandler.post(new Runnable() {
						@Override
						public void run() {
							callback.onError(Const.ERROR_CODE_RESULT_EMPTY);
						}
					});
				}
			}
		});
	}

	public void changeNotification(
			final Context context, final int position, final FixturesAdapter adapter) {
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				Fixture fixture = adapter.getItem(position);
				if (fixture != null
						&& (fixture.getDate()
						> System.currentTimeMillis() +
						1000 * 60 * 60 + 1000
						|| fixture.isNotified())) { // time for execute my code (:
					fixture.setChange();
					DBManager.setFixture(fixture);
					mReceiver.setNotifiedFixture(context, fixture, fixture.isNotified());
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							adapter.notifyItemChanged(position);
						}
					});
				}
			}
		});
	}

	private String getFixturesPref(int soccerseasonId, int matchday) {
		return Const.PREF_LAST_UPDATE_FIXTURES + "|" + soccerseasonId + "|" + matchday;
	}

	private String getScoresPref(int soccerseasonId) {
		return Const.PREF_LAST_UPDATE_SCORES + "|" + soccerseasonId;
	}

	private String getTeamPref(int teamId) {
		return Const.PREF_LAST_UPDATE_TEAM + "|" + teamId;
	}

	private String getPlayersPref(int teamId) {
		return Const.PREF_LAST_UPDATE_PLAYERS + "|" + teamId;
	}

	private String getTeamFixturesPref(int soccerseasonId, int teamId) {
		return Const.PREF_LAST_UPDATE_TEAM_FIXTURES + "|" + soccerseasonId + "|" + teamId;
	}

	private boolean canUpdateLeaguesList(Context context) {
		long lastUpdate = PreferencesManager.from(context)
				.getLong(Const.PREF_LAST_UPDATE_LEAGUES, 0);
		return lastUpdate + Configs.TIME_TO_UPDATE_LEAGUES < System.currentTimeMillis();
	}

	private boolean canUpdateFixturesList(Context context, int soccerseasonId, int matchday) {
		long lastUpdate = PreferencesManager.from(context)
				.getLong(getFixturesPref(soccerseasonId, matchday), 0);
		return lastUpdate + Configs.TIME_TO_UPDATE_FIXTURES < System.currentTimeMillis();
	}

	private boolean canUpdateScoresTable(Context context, int soccerseasonId) {
		long lastUpdate = PreferencesManager.from(context)
				.getLong(getScoresPref(soccerseasonId), 0);
		return lastUpdate + Configs.TIME_TO_UPDATE_SCORES < System.currentTimeMillis();
	}

	private boolean canUpdateTeam(Context context, int teamId) {
		long lastUpdate = PreferencesManager.from(context).getLong(getTeamPref(teamId), 0);
		return lastUpdate + Configs.TIME_TO_UPDATE_TEAM < System.currentTimeMillis();
	}

	private boolean canUpdatePlayersList(Context context, int teamId) {
		long lastUpdate = PreferencesManager.from(context).getLong(getPlayersPref(teamId), 0);
		return lastUpdate + Configs.TIME_TO_UPDATE_PLAYERS < System.currentTimeMillis();
	}

	private boolean canUpdateTeamFixturesList(Context context, int soccerseasonId, int teamId) {
		long lastUpdate = PreferencesManager.from(context)
				.getLong(getTeamFixturesPref(soccerseasonId, teamId), 0);
		return lastUpdate + Configs.TIME_TO_UPDATE_TEAM_FIXTURES < System.currentTimeMillis();
	}

	public interface OnGetLeagues extends OnResultListener<ArrayList<League>> {}

	public interface OnGetFixtures extends OnResultListener<ArrayList<Fixture>> {}

	public interface OnGetFixtureDetails extends OnResultListener<Head2head> {}

	public interface OnGetScores extends OnResultListener<ArrayList<Scores>> {}

	public interface OnGetTeam extends OnResultListener<Team> {}

	public interface OnGetPlayers extends OnResultListener<ArrayList<Player>> {}

}
