package pma.leaguereporter.view.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import pma.leaguereporter.R;
import pma.leaguereporter.controllers.PreferencesManager;
import pma.leaguereporter.controllers.SharingManager;
import pma.leaguereporter.model.interfaces.EventListener;
import pma.leaguereporter.model.objects.EventData;
import pma.leaguereporter.model.objects.Fixture;
import pma.leaguereporter.model.objects.League;
import pma.leaguereporter.model.objects.Player;
import pma.leaguereporter.model.objects.Scores;
import pma.leaguereporter.model.objects.Team;
import pma.leaguereporter.util.Configs;
import pma.leaguereporter.util.Const;
import pma.leaguereporter.util.L;
import pma.leaguereporter.view.dialogs.DialogPlayerInfo;
import pma.leaguereporter.view.dialogs.DialogTeam;
import pma.leaguereporter.view.fragments.FragmentAbout;
import pma.leaguereporter.view.fragments.FragmentFixtureInfo;
import pma.leaguereporter.view.fragments.FragmentFixtures;
import pma.leaguereporter.view.fragments.FragmentFixturesTeam;
import pma.leaguereporter.view.fragments.FragmentLeagues;
import pma.leaguereporter.view.fragments.FragmentPlayers;
import pma.leaguereporter.view.fragments.FragmentScores;

public class MainActivity extends AppCompatActivity implements EventListener {

	private Toolbar mToolbar;
	private FragmentManager mFragmentManager;
	private long mLastBack;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupUI();
		mFragmentManager = getSupportFragmentManager();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (PreferencesManager.from(this).getBoolean(Const.SUBMITTED, false)) {
			showContent();
		}
		else PreferencesManager.from(this).setBoolean(Const.SUBMITTED, true).commit();;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mToolbar != null) mToolbar = null;
		if (mFragmentManager != null) mFragmentManager = null;
	}

	@Override
	public void onBackPressed() {
		if (mLastBack + Configs.TIME_TO_EXIT > System.currentTimeMillis()) {
			showFinishDialog();
		}
		else if (!mFragmentManager.popBackStackImmediate())	showFinishDialog();
		mLastBack = System.currentTimeMillis();
	}

	@Override
	public void onEvent(EventData event) {
		int code = event.gecCode();
		switch (code) {
			case Const.EVENT_CODE_SELECT_LEAGUE:
				if (mFragmentManager.findFragmentByTag(FragmentFixtures.TAG) == null) {
					Bundle data1 = new Bundle();
					data1.putParcelable("league", event.getLeague());
					mFragmentManager.beginTransaction()
							.setCustomAnimations(
									R.anim.fragment_fade_in,
									R.anim.fragment_fade_out,
									R.anim.fragment_fade_pop_in,
									R.anim.fragment_fade_pop_out)
							.replace(R.id.main_container, FragmentFixtures.getInstance(data1))
							.addToBackStack(FragmentFixtures.TAG)
							.commit();
				}
				break;

			case Const.EVENT_CODE_SELECT_FIXTURE_INFO:
				if (mFragmentManager.findFragmentByTag(FragmentFixtureInfo.TAG) == null) {
					Bundle data2 = new Bundle();
					data2.putParcelable("fixture", event.getFixture());
					mFragmentManager.beginTransaction()
							.setCustomAnimations(
									R.anim.fragment_fade_in,
									R.anim.fragment_fade_out,
									R.anim.fragment_fade_pop_in,
									R.anim.fragment_fade_pop_out)
							.replace(R.id.main_container, FragmentFixtureInfo.getInstance(data2))
							.addToBackStack(FragmentFixtureInfo.TAG)
							.commit();
				}
				break;

			case Const.EVENT_CODE_SHOW_SCORES_TABLE:
				if (mFragmentManager.findFragmentByTag(FragmentScores.TAG) == null) {
					Bundle data3 = new Bundle();
					data3.putParcelable("league", event.getLeague());
					mFragmentManager.beginTransaction()
							.setCustomAnimations(
									R.anim.fragment_fade_in,
									R.anim.fragment_fade_out,
									R.anim.fragment_fade_pop_in,
									R.anim.fragment_fade_pop_out)
							.replace(R.id.main_container, FragmentScores.getInstance(data3))
							.addToBackStack(FragmentScores.TAG)
							.commit();
				}
				break;

			case Const.EVENT_CODE_SELECT_SCORES:
				if (mFragmentManager.findFragmentByTag(DialogTeam.TAG) == null) {
					Bundle data4 = new Bundle();
					data4.putParcelable("scores", event.getScores());
					DialogTeam dialogTeam = DialogTeam.getInstance(data4);
					dialogTeam.show(mFragmentManager, DialogTeam.TAG);
				}
				break;

			case Const.EVENT_CODE_SELECT_PLAYERS:
				if (mFragmentManager.findFragmentByTag(FragmentPlayers.TAG) == null) {
					Bundle data5 = new Bundle();
					data5.putParcelable("team", event.getTeam());
					mFragmentManager.beginTransaction()
							.setCustomAnimations(
									R.anim.fragment_fade_in,
									R.anim.fragment_fade_out,
									R.anim.fragment_fade_pop_in,
									R.anim.fragment_fade_pop_out)
							.replace(R.id.main_container, FragmentPlayers.getInstance(data5))
							.addToBackStack(FragmentPlayers.TAG)
							.commit();
				}
				break;

			case Const.EVENT_CODE_SELECT_FIXTURES:
				if (mFragmentManager.findFragmentByTag(FragmentFixturesTeam.TAG) == null) {
					Bundle data6 = new Bundle();
					data6.putParcelable("team", event.getTeam());
					data6.putParcelable("score", event.getScores());
					mFragmentManager.beginTransaction()
							.setCustomAnimations(
									R.anim.fragment_fade_in,
									R.anim.fragment_fade_out,
									R.anim.fragment_fade_pop_in,
									R.anim.fragment_fade_pop_out)
							.replace(R.id.main_container, FragmentFixturesTeam.getInstance(data6))
							.addToBackStack(FragmentFixturesTeam.TAG)
							.commit();
				}
				break;

			case Const.EVENT_CODE_SELECT_PLAYER_INFO:
				if (mFragmentManager.findFragmentByTag(DialogPlayerInfo.TAG) == null) {
					Bundle data7 = new Bundle();
					data7.putParcelable("player", event.getPlayer());
					DialogPlayerInfo dialogPlayerInfo = DialogPlayerInfo.getInstance(data7);
					dialogPlayerInfo.show(mFragmentManager, DialogPlayerInfo.TAG);
				}
				break;

			case Const.EVENT_CODE_SHOW_SETTINGS:
				 startActivity(new Intent(this, SettingsActivity.class));

				break;

			case Const.EVENT_CODE_SHOW_ABOUT:
				if (mFragmentManager.findFragmentByTag(FragmentAbout.TAG) == null) {
					mFragmentManager.beginTransaction()
							.setCustomAnimations(
									R.anim.fragment_fade_in,
									R.anim.fragment_fade_out,
									R.anim.fragment_fade_pop_in,
									R.anim.fragment_fade_pop_out)
							.replace(R.id.main_container, FragmentAbout.getInstance(null))
							.addToBackStack(FragmentAbout.TAG)
							.commit();
				}
				break;

			case Const.EVENT_CODE_SHARE_FIXTURE:
				Fixture fixture = event.getFixture();
				if (fixture != null) {
					Intent sendIntent = new Intent(Intent.ACTION_SEND);
					sendIntent.setType("text/plain");
					sendIntent.putExtra(
							Intent.EXTRA_TEXT,
							SharingManager.getShareFixture(getResources(), fixture)
					);
					startActivity(sendIntent);
				}
				break;

			case Const.EVENT_CODE_SHARE_FIXTURES_LIST:
				ArrayList<Fixture> fixturesList = event.getFixturesList();
				League league1 = event.getLeague();
				if (league1 != null && fixturesList != null && !fixturesList.isEmpty()) {
					Intent sendIntent = new Intent(Intent.ACTION_SEND);
					sendIntent.setType("text/plain");
					sendIntent.putExtra(
							Intent.EXTRA_TEXT,
							SharingManager.getShareFixturesList(getResources(), league1, fixturesList)
					);
					startActivity(sendIntent);
				}
				break;

			case Const.EVENT_CODE_SHARE_SCORES_TABLE:
				ArrayList<Scores> scoresList = event.getScoresList();
				League league2 = event.getLeague();
				if (league2 != null && scoresList != null) {
					Intent sendIntent = new Intent(Intent.ACTION_SEND);
					sendIntent.setType("text/plain");
					sendIntent.putExtra(
							Intent.EXTRA_TEXT,
							SharingManager.getShareScoresTable(getResources(), league2, scoresList));
					startActivity(sendIntent);
				}
				break;

			case Const.EVENT_CODE_SHARE_PLAYERS_LIST:
				ArrayList<Player> playersList = event.getPlayersList();
				Team team1 = event.getTeam();
				if (team1 != null && playersList != null) {
					Intent sendIntent = new Intent(Intent.ACTION_SEND );
					sendIntent.setType("text/plain");
					sendIntent.putExtra(
							Intent.EXTRA_TEXT,
							SharingManager.getSharePlayersList(getResources(), team1, playersList));
					startActivity(sendIntent);
				}
				break;

			case Const.EVENT_CODE_SHARE_TEAM_FIXTURES_LIST:
				ArrayList<Fixture> teamFixturesList = event.getFixturesList();
				Team team2 = event.getTeam();
				if (team2 != null && teamFixturesList != null) {
					Intent sendIntent = new Intent(Intent.ACTION_SEND);
					sendIntent.setType("text/plain");
					sendIntent.putExtra(
							Intent.EXTRA_TEXT,
							SharingManager.getShareTeamFixturesList(
									getResources(), team2, teamFixturesList)
					);
					startActivity(sendIntent);
				}
				break;

			default:
				L.e(MainActivity.class, "new event code->" + code);
		}
	}

	private void setupUI() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
	}

	private void showFinishDialog() {
		AlertDialog dialog = new AlertDialog.Builder(this, R.style.AppTheme_Dialog)
				.setTitle(R.string.dialog_exit_title)
				.setPositiveButton(R.string.dialog_exit_positive, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						supportFinishAfterTransition();
					}
				})
				.setNegativeButton(R.string.dialog_exit_negative, null)
				.create();
		dialog.show();
	}

	private void showContent() {
		if (mFragmentManager.findFragmentByTag(FragmentLeagues.TAG) == null) {
			mFragmentManager.beginTransaction()
					.replace(
							R.id.main_container,
							FragmentLeagues.getInstance(null),
							FragmentLeagues.TAG
					).commit();
		}
	}

}
