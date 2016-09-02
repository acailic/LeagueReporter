package pma.leaguereporter.controllers;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import pma.leaguereporter.R;
import pma.leaguereporter.model.objects.Fixture;
import pma.leaguereporter.model.objects.League;
import pma.leaguereporter.model.objects.Player;
import pma.leaguereporter.model.objects.Scores;
import pma.leaguereporter.model.objects.Team;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SharingManager {

	@NonNull
	public static String getShareFixture(
			@NonNull Resources resources, @NonNull Fixture fixture) {
		int goalsHomeTeam = fixture.getGoalsHomeTeam(), goalsAwayTeam = fixture.getGoalsAwayTeam();
		if (goalsHomeTeam >= 0 && goalsAwayTeam >= 0) {
			String date = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())
					.format(new Date(fixture.getDate()));
			if (goalsHomeTeam > goalsAwayTeam) {
				return String.format(
						resources.getString(R.string.share_fixture_home_win), date,
						fixture.getHomeTeamName(),
						fixture.getAwayTeamName(),
						goalsHomeTeam, goalsAwayTeam
				);
			}
			else if (goalsHomeTeam < goalsAwayTeam){
				return String.format(
						resources.getString(R.string.share_fixture_away_win), date,
						fixture.getHomeTeamName(),
						fixture.getAwayTeamName(),
						goalsHomeTeam, goalsAwayTeam
				);
			}
			else {
				return String.format(
						resources.getString(R.string.share_fixture_draw), date,
						fixture.getHomeTeamName(),
						fixture.getAwayTeamName(),
						goalsHomeTeam, goalsAwayTeam
				);
			}
		}
		else {
			String date = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())
					.format(new Date(fixture.getDate()));
			return String.format(resources.getString(R.string.share_fixture_timed), date,
					fixture.getHomeTeamName(), fixture.getAwayTeamName());

		}
	}

	@NonNull
	public static String getShareFixturesList(
			@NonNull Resources resources, @NonNull League league, @NonNull ArrayList<Fixture> list) {
		StringBuilder builder = new StringBuilder();
		int goalsHomeTeam, goalsAwayTeam, count;
		Fixture fixture;
		Calendar calendar = Calendar.getInstance(), calendarTmp = Calendar.getInstance();
		Date date = new Date(), dateTmp = new Date();
		if (!list.isEmpty()) {
			builder.append(String.format(
							resources.getString(R.string.share_fixtures_header),
							league.getCaption(), list.get(0).getMatchday()))
					.append("\n");
			count = list.size();
			for (int i = 0; i < count; i++) {
				fixture = list.get(i);
				goalsHomeTeam = fixture.getGoalsHomeTeam();
				goalsAwayTeam = fixture.getGoalsAwayTeam();
				date.setTime(fixture.getDate());
				calendar.setTime(date);

				if (i > 0) {
					dateTmp.setTime(list.get(i - 1).getDate());
					calendarTmp.setTime(dateTmp);
				}
				if (i == 0
						|| (calendar.get(Calendar.DAY_OF_YEAR)
						!= calendarTmp.get(Calendar.DAY_OF_YEAR))) {
					dateTmp.setTime(System.currentTimeMillis());
					calendarTmp.setTime(dateTmp);
					if (calendar.get(Calendar.YEAR)
							== calendarTmp.get(Calendar.YEAR)
							&&calendar.get(Calendar.DAY_OF_YEAR)
							== calendarTmp.get(Calendar.DAY_OF_YEAR)) {
						builder.append(
								resources.getString(R.string.share_fixtures_separator_today));
					}
					else if (calendar.get(Calendar.YEAR)
							== calendarTmp.get(Calendar.YEAR)
							&& calendar.get(Calendar.DAY_OF_YEAR)
							== calendarTmp.get(Calendar.DAY_OF_YEAR) + 1) {
						builder.append(
								resources.getString(R.string.share_fixtures_separator_tomorrow));
					}
					else {
						builder.append(String.format(
								resources.getString(R.string.share_fixtures_separator_with_date),
								new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
										.format(new Date(fixture.getDate())))
						);
					}
					builder.append("\n");
				}

				if (goalsHomeTeam >= 0 && goalsAwayTeam >= 0) {
					builder.append(
							String.format(resources.getString(R.string.share_fixtures_finished),
							fixture.getHomeTeamName(), fixture.getAwayTeamName(),
							goalsHomeTeam, goalsAwayTeam));

				}
				else {
					String dateStr = new SimpleDateFormat("HH:mm", Locale.getDefault())
							.format(new Date(fixture.getDate()));
					builder.append(
							String.format(resources.getString(R.string.share_fixtures_timed),
							fixture.getHomeTeamName(), fixture.getAwayTeamName(), dateStr));
				}
				builder.append("\n");
			}
		}
		return builder.toString();
	}

	@NonNull
	public static String getShareScoresTable(
			@NonNull Resources resources, @NonNull League league, @NonNull ArrayList<Scores> list) {
		StringBuilder builder = new StringBuilder();
		builder.append(String.format(
				resources.getString(R.string.share_scores_header),
				league.getCaption(),
				league.getCurrentMatchday()))
				.append("\n");
		for (Scores scores : list) {
			builder.append(String.format(
					resources.getString(R.string.share_scores_item),
					scores.getRank(),
					scores.getPoints(),
					scores.getTeam()))
					.append("\n");
		}
		return builder.toString();
	}

	public static String getSharePlayersList(
			@NonNull Resources resources, @NonNull Team team, @NonNull ArrayList<Player> list) {
		StringBuilder builder = new StringBuilder();
		builder.append(String.format(
				resources.getString(R.string.share_players_header),
				team.getName(),
				team.getShortName()))
				.append("\n");
		for (Player player : list) {
			builder.append(String.format(resources.getString(R.string.share_players_item),
					player.getJerseyNumber(),
					player.getName()))
					.append("\n");
		}
		return builder.toString();
	}

	public static String getShareTeamFixturesList(
			@NonNull Resources resources, @NonNull Team team, @NonNull ArrayList<Fixture> list) {
		StringBuilder builder = new StringBuilder();
		builder.append(String.format(
				resources.getString(R.string.share_team_fixtures_header),
				team.getName(),
				team.getShortName()))
				.append("\n");
		for (Fixture fixture: list) {
			String dateStr = new SimpleDateFormat("HH:mm", Locale.getDefault())
					.format(new Date(fixture.getDate()));
			builder.append(String.format(resources.getString(R.string.share_team_fixtures_item),
					fixture.getHomeTeamName(),
					fixture.getAwayTeamName(),
					dateStr))
					.append("\n");
		}
		return builder.toString();
	}

}
