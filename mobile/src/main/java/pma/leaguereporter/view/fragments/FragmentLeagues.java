package pma.leaguereporter.view.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import pma.leaguereporter.R;
import pma.leaguereporter.controllers.Controller;
import pma.leaguereporter.model.abstractions.AbstractFragment;
import pma.leaguereporter.model.listeners.RecyclerViewItemTouchListener;
import pma.leaguereporter.model.adapters.LeaguesAdapter;
import pma.leaguereporter.model.interfaces.EventListener;
import pma.leaguereporter.model.interfaces.OnItemClickListener;
import pma.leaguereporter.model.objects.EventData;
import pma.leaguereporter.model.objects.League;
import pma.leaguereporter.util.Const;
import pma.leaguereporter.util.UI;

import java.util.ArrayList;
import java.util.Calendar;

public class FragmentLeagues extends AbstractFragment
		implements Controller.OnGetLeagues, OnItemClickListener {

	public static final String TAG = "FragmentLeagues";

	private RecyclerView mRecyclerView;
	private FrameLayout mProgressContent;
	private LinearLayout mErrorContent;
	private LinearLayout mEmptyContent;
	private LeaguesAdapter mAdapter;
	private Controller mController;
	private EventListener mEventListener;
	private Snackbar mSnackbar;
	private boolean mIsShow;

	public static FragmentLeagues getInstance(@Nullable Bundle args) {
		FragmentLeagues fragment = new FragmentLeagues();
		fragment.setArguments(args == null ? new Bundle() : args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		if (mController == null) mController = Controller.getInstance();
		if (savedInstanceState != null) {
			ArrayList<League> list = savedInstanceState.getParcelableArrayList("list");
			if (list != null && !list.isEmpty()) mAdapter = new LeaguesAdapter(getResources(), list);
		}
	}

	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_leagues, container, false);
		setupUI(view);
		if (mEventListener == null) mEventListener = (EventListener) getActivity();
		showContent();
		mIsShow = true;
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mEventListener != null) mEventListener = null;
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
		if (mAdapter != null) {
			ArrayList<League> list = mAdapter.getList();
			if (!list.isEmpty()) outState.putParcelableArrayList("list", list);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mController != null) mController = null;
		if (mAdapter != null) mAdapter = null;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_leagues, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case R.id.action_settings:
				mEventListener.onEvent(new EventData(Const.EVENT_CODE_SHOW_SETTINGS));
				break;

			case R.id.action_about:
				mEventListener.onEvent(new EventData(Const.EVENT_CODE_SHOW_ABOUT));
				break;

			default:
				super.onOptionsItemSelected(item);
		}
		return true;
	}

	@Override
	public void onError(int code) {
		if (mIsShow && (mAdapter == null || mAdapter.getList().isEmpty())) {
			if (code == Const.ERROR_CODE_RESULT_EMPTY) {
				UI.hide(mRecyclerView, mErrorContent, mProgressContent);
				UI.show(mEmptyContent);
			}
			else {
				UI.hide(mRecyclerView, mEmptyContent, mProgressContent);
				UI.show(mErrorContent);
				mSnackbar = Snackbar.make(
						getActivity().findViewById(R.id.main_container),
						R.string.snackbar_result_null_text,
						Snackbar.LENGTH_INDEFINITE
				).setAction(
						R.string.snackbar_result_null_action,
						new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								loadData();
							}
						}
				);
				mSnackbar.show();
			}
		}
	}

	@Override
	public void onSuccess(ArrayList<League> data) {
		if (mIsShow) {
			UI.hide(mErrorContent, mEmptyContent, mProgressContent);
			UI.show(mRecyclerView);
			if (mSnackbar != null) mSnackbar.dismiss();
			if (mAdapter == null) {
				mAdapter = new LeaguesAdapter(getResources(), data);
				mRecyclerView.setAdapter(mAdapter);
				mRecyclerView.startAnimation(
						AnimationUtils.loadAnimation(getActivity(), R.anim.leagues_fade_in)
				);
			} else {
				mAdapter.changeData(data);
			}
		}
	}

	@Override
	public void onItemClick(View view, int position) {
		mEventListener.onEvent(new EventData(Const.EVENT_CODE_SELECT_LEAGUE)
				.setLeague(mAdapter.getItem(position))
		);
	}

	@Override
	public void onItemLongClick(View view, int position) {

	}

	private void setupUI(View view) {
		ActionBar toolbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
		if (toolbar != null) {
			toolbar.setTitle(getString(R.string.leagues_title));
			int year = Calendar.getInstance().get(Calendar.YEAR);
			toolbar.setSubtitle(String.format(getString(R.string.leagues_subtitle), year - 1, year));
		}
		mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			mRecyclerView.setLayoutManager(new GridLayoutManager(
					getActivity(),
					2,
					GridLayout.VERTICAL,
					false)
			);
		}
		else {
			mRecyclerView.setLayoutManager(new LinearLayoutManager(
					getActivity(),
					LinearLayoutManager.VERTICAL,
					false)
			);
		}
		mRecyclerView.addOnItemTouchListener(new RecyclerViewItemTouchListener(
				getActivity(),
				mRecyclerView,
				this)
		);
		mRecyclerView.setHasFixedSize(true);
		mProgressContent = (FrameLayout) view.findViewById(R.id.content_progress);
		mEmptyContent = (LinearLayout) view.findViewById(R.id.content_empty);
		mErrorContent = (LinearLayout) view.findViewById(R.id.content_error);
		UI.hide(mEmptyContent, mErrorContent, mProgressContent, mRecyclerView);
	}

	private void showContent() {
		if (mAdapter == null) loadData();
		else {
			UI.hide(mEmptyContent, mErrorContent, mProgressContent);
			UI.show(mRecyclerView);
			mRecyclerView.setAdapter(mAdapter);
		}
	}

	private void loadData() {
		UI.hide(mRecyclerView, mEmptyContent, mErrorContent);
		UI.show(mProgressContent);
		mController.getListOfLeagues(getActivity(), this);
	}

}
