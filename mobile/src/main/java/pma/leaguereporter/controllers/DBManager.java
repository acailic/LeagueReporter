package pma.leaguereporter.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import pma.leaguereporter.model.SQLHelper;
import pma.leaguereporter.model.objects.Fixture;
import pma.leaguereporter.model.objects.Head2head;
import pma.leaguereporter.model.objects.League;
import pma.leaguereporter.model.objects.Player;
import pma.leaguereporter.model.objects.Scores;
import pma.leaguereporter.model.objects.Team;
import pma.leaguereporter.util.DBConst;

public class DBManager {

    private static SQLHelper sSQLHelper;

    public static void init(Context context) {
        sSQLHelper = SQLHelper.getInstance(context);
    }

    @Nullable
    public static ArrayList<League> getLeaguesList() {
        Cursor cursor = sSQLHelper.getAll(DBConst.TABLE_LEAGUES);
        ArrayList<League> list = League.parseArray(cursor);
        if (cursor != null) cursor.close();
        return list;
    }

    @Nullable
    public static ArrayList<Fixture> getFixturesListByMatchday(int soccerseasonId, int matchday) {
        Cursor cursor = sSQLHelper.getAll(
                DBConst.TABLE_FIXTURES,
                new String[]{DBConst.SOCCER_SEASON_ID, DBConst.MATCHDAY},
                new String[]{String.valueOf(soccerseasonId), String.valueOf(matchday)});// , DBConst.DATE, false
        ArrayList<Fixture> list = Fixture.parseArray(cursor);
        if (cursor != null) cursor.close();
        return list;
    }

    @Nullable
    public static ArrayList<Fixture> getFixturesListUpcoming(int soccerseasonId, int teamId) {

        /*

        ISSUES: TODO: list upcoming mathes don't working fine
         problem: datum je numeric i imam problema sa ovim upiitom

         @a.ilic
         */
        Cursor cursor = sSQLHelper.query(
                DBConst.TABLE_FIXTURES,
                null,
                DBConst.SOCCER_SEASON_ID + "=? AND " +
                        DBConst.DATE + ">? AND (" +
                        DBConst.HOME_TEAM_ID + "=? OR " +
                        DBConst.AWAY_TEAM_ID + "=?)",
                new String[]{String.valueOf(soccerseasonId),
                        String.valueOf(System.currentTimeMillis()),
                        String.valueOf(teamId),
                        String.valueOf(teamId)},
                null, null, null);
        ArrayList<Fixture> list = Fixture.parseArray(cursor);
        if (cursor != null) cursor.close();
        return list;
    }

    @Nullable
    public static ArrayList<Fixture> getFixturesList(int soccerseasonId) {
        Cursor cursor = sSQLHelper.getAll(
                DBConst.TABLE_FIXTURES,
                new String[]{DBConst.SOCCER_SEASON_ID},
                new String[]{String.valueOf(soccerseasonId)}); // , DBConst.DATE, true
        ArrayList<Fixture> list = Fixture.parseArray(cursor);
        if (cursor != null) cursor.close();
        return list;
    }

    @Nullable
    public static ArrayList<Scores> getScoresList(int soccerseasonId) {
        Cursor cursor = sSQLHelper.getAll(
                DBConst.TABLE_SCORES,
                new String[]{DBConst.SOCCER_SEASON_ID},
                new String[]{String.valueOf(soccerseasonId)}); // , DBConst.POINTS, true
        ArrayList<Scores> list = Scores.parseArray(cursor);
        if (cursor != null) cursor.close();
        return list;
    }

    @Nullable
    public static ArrayList<Player> getPlayerList(int teamId) {
        Cursor cursor = sSQLHelper.getAll(
                DBConst.TABLE_PLAYERS,
                new String[]{DBConst.TEAM_ID},
                new String[]{String.valueOf(teamId)});
        ArrayList<Player> list = Player.parseArray(cursor);
        if (cursor != null) cursor.close();
        return list;
    }

    public static League getLeague(int id) {
        Cursor cursor = sSQLHelper.getAll(
                DBConst.TABLE_LEAGUES,
                new String[]{DBConst.ID},
                new String[]{String.valueOf(id)});
        League league = League.parse(cursor);
        if (cursor != null) cursor.close();
        return league;
    }

    public static Fixture getFixture(int id) {
        Cursor cursor = sSQLHelper.getAll(
                DBConst.TABLE_FIXTURES,
                new String[]{DBConst.ID},
                new String[]{String.valueOf(id)});
        Fixture fixture = Fixture.parse(cursor);
        if (cursor != null) cursor.close();
        return fixture;
    }

    public static Head2head getHead2head(int fixture_id) {
        Cursor cursor = sSQLHelper.getAll(
                DBConst.TABLE_HEAD2HEAD,
                new String[]{DBConst.FIXTURE_ID},
                new String[]{String.valueOf(fixture_id)});
        Head2head head2head = Head2head.parse(cursor);
        if (cursor != null) cursor.close();
        return head2head;
    }

    public static Scores getScores(int soccerSeasonId) {
        Cursor cursor = sSQLHelper.getAll(
                DBConst.TABLE_SCORES,
                new String[]{DBConst.SOCCER_SEASON_ID},
                new String[]{String.valueOf(soccerSeasonId)});
        Scores scores = Scores.parse(cursor);
        if (cursor != null) cursor.close();
        return scores;
    }

    public static Team getTeam(int team_id) {
        Cursor cursor = sSQLHelper.getAll(
                DBConst.TABLE_TEAMS,
                new String[]{DBConst.ID},
                new String[]{String.valueOf(team_id)});
        Team team = Team.parse(cursor);
        if (cursor != null) cursor.close();
        return team;
    }

    public static Player getPlayer(int player_id) {
        Cursor cursor = sSQLHelper.getAll(
                DBConst.TABLE_PLAYERS,
                new String[]{DBConst.ID},
                new String[]{String.valueOf(player_id)});
        Player player = Player.parse(cursor);
        if (cursor != null) cursor.close();
        return player;
    }

    public static void setLeaguesList(ArrayList<League> list) {
        if (list != null && !list.isEmpty()) {
            for (League league : list) {
                setLeague(league);
            }
        }
    }

    public static void setFixturesList(ArrayList<Fixture> list) {
        if (list != null && !list.isEmpty()) {
            for (Fixture fixture : list) {
                setFixture(fixture);
            }
        }
    }

    public static void setScoresList(ArrayList<Scores> list) {
        if (list != null && !list.isEmpty()) {
            for (Scores scores : list) {
                setScores(scores);
            }
        }
    }

    public static void setPlayersList(ArrayList<Player> list) {
        if (list != null && !list.isEmpty()) {
            for (Player player : list) {
                setPlayer(player);
            }
        }
    }

    public static void setLeague(League league) {
        if (league != null) {
            ContentValues data = new ContentValues();
            data.put(DBConst.ID, league.getID());
            data.put(DBConst.CAPTION, league.getCaption());
            data.put(DBConst.LEAGUE, league.getLeague());
            data.put(DBConst.YEAR, league.getYear());
            data.put(DBConst.CURRENT_MATCHDAY, league.getCurrentMatchday());
            data.put(DBConst.NUMBER_OF_MATCHDAYS, league.getNumberOfMatchdays());
            data.put(DBConst.NUMBER_OF_TEAMS, league.getNumberOfTeams());
            data.put(DBConst.NUMBER_OF_GAMES, league.getNumberOfGames());
            data.put(DBConst.LAST_UPDATED, league.getLastUpdated());

            Cursor cursor = sSQLHelper.getAll(
                    DBConst.TABLE_LEAGUES,
                    new String[]{DBConst.ID},
                    new String[]{String.valueOf(league.getID())});
            if (cursor.moveToFirst()) {
                sSQLHelper.update(
                        DBConst.TABLE_LEAGUES,
                        data, new String[]{DBConst.ID},
                        new String[]{String.valueOf(league.getID())});
            } else {
                sSQLHelper.insert(DBConst.TABLE_LEAGUES, data);
            }
            cursor.close();
        }
    }

    public static void setFixture(Fixture fixture) {
        if (fixture != null) {
            ContentValues data = new ContentValues();
            if (fixture.isChange()) data.put(DBConst.CAN_NOTIFIED, fixture.isNotified());
            data.put(DBConst.ID, fixture.getID());
            data.put(DBConst.SOCCER_SEASON_ID, fixture.getSoccerSeasonID());
            data.put(DBConst.DATE, fixture.getDate());
            data.put(DBConst.STATUS, fixture.getStatus());
            data.put(DBConst.MATCHDAY, fixture.getMatchday());
            data.put(DBConst.HOME_TEAM_ID, fixture.getHomeTeamID());
            data.put(DBConst.HOME_TEAM_NAME, fixture.getHomeTeamName());
            data.put(DBConst.AWAY_TEAM_ID, fixture.getAwayTeamID());
            data.put(DBConst.AWAY_TEAM_NAME, fixture.getAwayTeamName());
            data.put(DBConst.GOALS_HOME_TEAM, fixture.getGoalsHomeTeam());
            data.put(DBConst.GOALS_AWAY_TEAM, fixture.getGoalsAwayTeam());

            Cursor cursor = sSQLHelper.getAll(
                    DBConst.TABLE_FIXTURES,
                    new String[]{DBConst.ID},
                    new String[]{String.valueOf(fixture.getID())});
            if (cursor.moveToFirst()) {
                sSQLHelper.update(
                        DBConst.TABLE_FIXTURES,
                        data, new String[]{DBConst.ID},
                        new String[]{String.valueOf(fixture.getID())});
            } else {
                sSQLHelper.insert(DBConst.TABLE_FIXTURES, data);
            }
            cursor.close();
        }
    }

    public static void setHead2head(Head2head head2head) {
        if (head2head != null) {
            ContentValues data = new ContentValues();
            data.put(DBConst.FIXTURE_ID, head2head.getFixtureID());
            data.put(DBConst.COUNT, head2head.getCount());
            data.put(DBConst.TIME_FRAME_START, head2head.getTimeFrameStart());
            data.put(DBConst.TIME_FRAME_END, head2head.getTimeFrameEnd());
            data.put(DBConst.HOME_TEAM_WINS, head2head.getHomeTeamWins());
            data.put(DBConst.AWAY_TEAM_WINS, head2head.getAwayTeamWins());
            data.put(DBConst.DRAWS, head2head.getDraws());

            Cursor cursor = sSQLHelper.getAll(
                    DBConst.TABLE_HEAD2HEAD,
                    new String[]{DBConst.FIXTURE_ID},
                    new String[]{String.valueOf(head2head.getFixtureID())});
            if (cursor.moveToFirst()) {
                sSQLHelper.update(
                        DBConst.TABLE_HEAD2HEAD,
                        data, new String[]{DBConst.FIXTURE_ID},
                        new String[]{String.valueOf(head2head.getFixtureID())});
            } else {
                sSQLHelper.insert(DBConst.TABLE_HEAD2HEAD, data);
            }
            cursor.close();
        }
    }

    public static void setScores(Scores scores) {
        if (scores != null) {
            ContentValues data = new ContentValues();
            data.put(DBConst.SOCCER_SEASON_ID, scores.getSoccerSeasonID());
            data.put(DBConst.RANK, scores.getRank());
            data.put(DBConst.TEAM, scores.getTeam());
            data.put(DBConst.TEAM_ID, scores.getTeamId());
            data.put(DBConst.PLAYED_GAMES, scores.getPlayedGames());
            data.put(DBConst.CREST_URL, scores.getCrestURI());
            data.put(DBConst.POINTS, scores.getPoints());
            data.put(DBConst.GOALS, scores.getGoals());
            data.put(DBConst.GOAL_AGAINST, scores.getGoalAgainst());
            data.put(DBConst.GOAL_DIFFERENCE, scores.getGoalDifference());

            Cursor cursor = sSQLHelper.getAll(
                    DBConst.TABLE_SCORES,
                    new String[]{DBConst.SOCCER_SEASON_ID, DBConst.TEAM_ID},
                    new String[]{String.valueOf(scores.getSoccerSeasonID()), String.valueOf(scores.getTeamId())});
            if (cursor.moveToFirst()) {
                sSQLHelper.update(
                        DBConst.TABLE_SCORES,
                        data, new String[]{DBConst.SOCCER_SEASON_ID, DBConst.TEAM_ID},
                        new String[]{String.valueOf(scores.getSoccerSeasonID()), String.valueOf(scores.getTeamId())});
            } else {
                sSQLHelper.insert(DBConst.TABLE_SCORES, data);
            }
            cursor.close();
        }
    }

    public static void setTeam(Team team) {
        if (team != null) {
            ContentValues data = new ContentValues();
            data.put(DBConst.ID, team.getID());
            if (team.isChanged()) data.put(DBConst.CAN_FAVORITE, team.getIsFavorite());
            data.put(DBConst.NAME, team.getName());
            data.put(DBConst.SHORT_NAME, team.getShortName());
            data.put(DBConst.SQUAD_MARKET_VALUE, team.getSquadMarketValue());
            data.put(DBConst.CREST_URL, team.getCrestURL());

            Cursor cursor = sSQLHelper.getAll(
                    DBConst.TABLE_TEAMS,
                    new String[]{DBConst.ID},
                    new String[]{String.valueOf(team.getID())});
            if (cursor.moveToFirst()) {
                sSQLHelper.update(
                        DBConst.TABLE_TEAMS,
                        data, new String[]{DBConst.ID},
                        new String[]{String.valueOf(team.getID())});
            } else {
                sSQLHelper.insert(DBConst.TABLE_TEAMS, data);
            }
            cursor.close();
        }
    }

    public static void setPlayer(Player player) {
        if (player != null) {
            ContentValues data = new ContentValues();
            data.put(DBConst.TEAM_ID, player.getTeamID());
            data.put(DBConst.ID, player.getID());
            data.put(DBConst.NAME, player.getName());
            data.put(DBConst.POSITION, player.getPosition());
            data.put(DBConst.JERSEY_NUMBER, player.getJerseyNumber());
            data.put(DBConst.DATE_OF_BIRTH, player.getDateOfBirth());
            data.put(DBConst.NATIONALITY, player.getNationality());
            data.put(DBConst.CONTRACT_UNTIL, player.getContractUntil());
            data.put(DBConst.MARKET_VALUE, player.getMarketValue());

            Cursor cursor = sSQLHelper.getAll(
                    DBConst.TABLE_PLAYERS,
                    new String[]{DBConst.ID},
                    new String[]{String.valueOf(player.getID())});
            if (cursor.moveToFirst()) {
                sSQLHelper.update(
                        DBConst.TABLE_PLAYERS,
                        data, new String[]{DBConst.ID},
                        new String[]{String.valueOf(player.getID())});
            } else {
                sSQLHelper.insert(DBConst.TABLE_PLAYERS, data);
            }
            cursor.close();
        }
    }

}
