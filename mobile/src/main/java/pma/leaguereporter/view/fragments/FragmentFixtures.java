package pma.leaguereporter.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import pma.leaguereporter.R;
import pma.leaguereporter.controllers.Controller;
import pma.leaguereporter.model.abstractions.AbstractFragment;
import pma.leaguereporter.model.adapters.FixturesAdapter;
import pma.leaguereporter.model.interfaces.EventListener;
import pma.leaguereporter.model.interfaces.OnItemClickListener;
import pma.leaguereporter.model.listeners.RecyclerViewItemTouchListener;
import pma.leaguereporter.model.objects.EventData;
import pma.leaguereporter.model.objects.Fixture;
import pma.leaguereporter.model.objects.League;
import pma.leaguereporter.util.Const;
import pma.leaguereporter.util.L;
import pma.leaguereporter.util.UI;

public class FragmentFixtures extends AbstractFragment
		implements Controller.OnGetFixtures, OnItemClickListener {

	public static final String TAG = "FragmentFixtures";

	private RecyclerView mRecyclerView;
	private FrameLayout mProgressContent;
	private LinearLayout mErrorContent;
	private LinearLayout mEmptyContent;
	private TextView mMatchdayField;
	private FixturesAdapter mAdapter;
	private Controller mController;
	private EventListener mEventListener;
	private Snackbar mSnackbar;
	private League mLeague;
	private int mMatchday;
	private boolean mIsShow;

	public static FragmentFixtures getInstance(@Nullable Bundle data) {
		FragmentFixtures fragment = new FragmentFixtures();
		fragment.setArguments(data == null ? new Bundle() : data);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		if (mLeague == null) mLeague = getArguments().getParcelable("league");
		if (savedInstanceState != null) {
			ArrayList<Fixture> list = savedInstanceState.getParcelableArrayList("list");
			if (list != null) mAdapter = new FixturesAdapter(getResources(), list);
			int matchday = savedInstanceState.getInt("matchday", 0);
			if (matchday > 0) mMatchday = matchday;
		}
		else if (mLeague != null) mMatchday = mLeague.getCurrentMatchday();

	}

	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_fixtures, container, false);
		setupUI(view);
		if (mEventListener == null) mEventListener = (EventListener) getActivity();
		if (mController == null) mController = Controller.getInstance();
		showContent();
		mIsShow = true;
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mRecyclerView != null) mRecyclerView = null;
		if (mEmptyContent != null) mEmptyContent = null;
		if (mErrorContent != null) mErrorContent = null;
		if (mProgressContent != null) mProgressContent = null;
		if (mSnackbar != null) {
			mSnackbar.dismiss();
			mSnackbar = null;
		}
		mIsShow = false;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("matchday", mMatchday);
		if (mAdapter != null) {
			ArrayList<Fixture> list = mAdapter.getList();
			if (!list.isEmpty()) outState.putParcelableArrayList("list", list);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mEventListener != null) mEventListener = null;
		if (mMatchdayField != null) mMatchdayField = null;
		if (mController != null) mController = null;
		if (mAdapter != null) mAdapter = null;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_fixtures, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case R.id.action_share:
				if (!mAdapter.getList().isEmpty()) {
					mEventListener.onEvent(
							new EventData(Const.EVENT_CODE_SHARE_FIXTURES_LIST)
									.setLeague(mLeague)
									.setFixturesList(mAdapter.getList())
					);
				}
				break;

			case R.id.action_current:
				mMatchday = mLeague.getCurrentMatchday();
				loadData(mMatchday);
				break;

			case R.id.action_scores_table:
				mEventListener.onEvent(
						new EventData(Const.EVENT_CODE_SHOW_SCORES_TABLE)
								.setLeague(mLeague)
				);
				break;

			 case R.id.action_settings:
				mEventListener.onEvent(new EventData(Const.EVENT_CODE_SHOW_SETTINGS));
				break;

			case R.id.action_about:
				mEventListener.onEvent(new EventData(Const.EVENT_CODE_SHOW_ABOUT));
				break;

			default:
				return super.onOptionsItemSelected(item);
		}
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		int id = v.getId();
		switch (id) {
			case R.id.matchday:
				menu.setHeaderTitle(getString(R.string.context_head_select_matchday));
				for (int i = 1; i <= mLeague.getNumberOfMatchdays(); i++) {
					menu.add(Const.GROUP_MATCHDAYS, i, 0, String.valueOf(i));
				}
				break;

			default:
				super.onCreateContextMenu(menu, v, menuInfo);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		int group = item.getGroupId();
		switch (group) {
			case Const.GROUP_MATCHDAYS:
				mMatchday = item.getItemId();
				loadData(mMatchday);
				break;

			case Const.GROUP_FIXTURES:
				mController.changeNotification(getActivity(), item.getItemId(), mAdapter);
				break;

			default:
				L.e(FragmentFixtures.class, "default group->" + group);
				return super.onContextItemSelected(item);
		}
		return true;
	}

	@Override
	public void onError(int code) {
		if (mIsShow && (mAdapter == null || mAdapter.getList().isEmpty())) {
			if (code == Const.ERROR_CODE_RESULT_EMPTY) {
				UI.hide(mRecyclerView, mErrorContent, mProgressContent);
				UI.show(mEmptyContent);
			} else {
				UI.hide(mRecyclerView, mEmptyContent, mProgressContent);
				UI.show(mErrorContent);
				mSnackbar = Snackbar.make(
						getActivity().findViewById(R.id.main_container),
						R.string.snackbar_result_null_text,
						Snackbar.LENGTH_INDEFINITE)
						.setAction(
								R.string.snackbar_result_null_action,
								new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										loadData(mMatchday);
									}
								}
						);
				mSnackbar.show();
			}
		}
	}

	@Override
	public void onSuccess(ArrayList<Fixture> data) {
		if (mIsShow) {
			UI.hide(mErrorContent, mEmptyContent, mProgressContent);
			UI.show(mRecyclerView);
			if (mSnackbar != null) mSnackbar.dismiss();
			if (mAdapter == null) {
				mAdapter = new FixturesAdapter(getResources(), data);
				mRecyclerView.setAdapter(mAdapter);
			} else {
				mAdapter.changeData(data);
			}
		}
	}

	@Override
	public void onItemClick(View view, int position) {
		mEventListener.onEvent(new EventData(Const.EVENT_CODE_SELECT_FIXTURE_INFO)
				.setFixture(mAdapter.getItem(position)));
	}

	@Override
	public void onItemLongClick(View view, int position) {
		Fixture fixture = mAdapter.getItem(position);
		if (fixture != null) {
			if (fixture.getDate() > System.currentTimeMillis() + 1000 * 60 * 60
					|| fixture.isNotified()) { // one day
				view.showContextMenu();
			}
		}
	}

	private void setupUI(View view) {
		ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
		if (toolbar != null) {
			toolbar.setTitle(mLeague.getCaption());
			toolbar.setSubtitle(null);
		}
		mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(
				getActivity(),
				LinearLayoutManager.VERTICAL,
				false));
		mRecyclerView.addOnItemTouchListener(new RecyclerViewItemTouchListener(
				getActivity(),
				mRecyclerView,
				this));
		mRecyclerView.setHasFixedSize(true);
		mMatchdayField = (TextView) view.findViewById(R.id.matchday);
		mMatchdayField.setText(String.format(
				getString(R.string.fixtures_matchday),
				mMatchday));
		mMatchdayField.setOnCreateContextMenuListener(this);
		mMatchdayField.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().openContextMenu(v);
			}
		});
		mProgressContent = (FrameLayout) view.findViewById(R.id.content_progress);
		mEmptyContent = (LinearLayout) view.findViewById(R.id.content_empty);
		mErrorContent = (LinearLayout) view.findViewById(R.id.content_error);
		UI.hide(mEmptyContent, mErrorContent, mProgressContent, mRecyclerView);
	}

	private void showContent() {
		if (mAdapter == null) loadData(mMatchday);
		else {
			UI.hide(mEmptyContent, mErrorContent, mProgressContent);
			UI.show(mRecyclerView);
			mRecyclerView.setAdapter(mAdapter);
		}
	}

	private void loadData(int matchday) {
		UI.hide(mRecyclerView, mEmptyContent, mErrorContent);
		UI.show(mProgressContent);
		if (mAdapter != null) mAdapter = null;
		setMatchday(matchday);
		mController.getListOfFixtures(getActivity(), mLeague.getID(), matchday, this);
	}

	private void setMatchday(int matchday) {
		mMatchdayField.setText(String.format(getString(R.string.fixtures_matchday), matchday));
	}

}
