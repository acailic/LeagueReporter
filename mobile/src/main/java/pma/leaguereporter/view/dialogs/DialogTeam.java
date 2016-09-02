package pma.leaguereporter.view.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import pma.leaguereporter.R;
import pma.leaguereporter.controllers.Controller;
import pma.leaguereporter.model.interfaces.EventListener;
import pma.leaguereporter.model.objects.EventData;
import pma.leaguereporter.model.objects.Scores;
import pma.leaguereporter.model.objects.Team;
import pma.leaguereporter.util.Const;
import pma.leaguereporter.util.L;
import pma.leaguereporter.util.UI;

public class DialogTeam extends AppCompatDialogFragment implements Controller.OnGetTeam, View.OnClickListener {

	public static final String TAG = "DialogTeam";

	private FrameLayout mProgressContent;
	private LinearLayout mErrorContent;
	private LinearLayout mEmptyContent;
	private LinearLayout mContent;
	private ImageView mIcon;
	private TextView mName;
	private TextView mShortName;
	private TextView mSquadMarketValue;
	private Button mFixtures;
	private Button mPlayers;
	private Controller mController;
	private Scores mScores;
	private Team mTeam;
	private EventListener mListener;
	private boolean mIsShow;

	public static DialogTeam getInstance(@Nullable Bundle data) {
		DialogTeam fragment = new DialogTeam();
		fragment.setArguments(data == null ? new Bundle() : data);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogTeam.STYLE_NO_TITLE, R.style.AppTheme_Dialog);
		if (mController == null) mController = Controller.getInstance();
		if (mScores == null) mScores = getArguments().getParcelable("scores");
		if (mTeam == null && savedInstanceState != null) {
			mTeam = savedInstanceState.getParcelable("team");
		}
	}

	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_team, container, false);
		setupUI(view);
		if (mListener == null) mListener = (EventListener) getActivity();
		showContent();
		mIsShow = true;
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mErrorContent != null) mErrorContent = null;
		if (mEmptyContent != null) mEmptyContent = null;
		if (mProgressContent != null) mProgressContent = null;
		if (mContent != null) mContent = null;
		if (mIcon != null) mIcon = null;
		if (mName != null) mName = null;
		if (mShortName != null) mShortName = null;
		if (mSquadMarketValue != null) mSquadMarketValue = null;
		if (mFixtures != null) mFixtures = null;
		if (mPlayers != null) mPlayers = null;
		mIsShow = false;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mTeam != null) outState.putParcelable("team", mTeam);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mController != null) mController = null;
		if (mListener != null) mListener = null;
		if (mScores != null) mScores = null;
		if (mTeam != null) mTeam = null;
	}

	@Override
	public void onError(int code) {
		if (mIsShow && mTeam == null) {
			UI.hide(mEmptyContent, mProgressContent, mContent);
			UI.show(mErrorContent);
		}
	}

	@Override
	public void onSuccess(Team data) {
		if (mIsShow) {
			mTeam = data;
			UI.hide(mErrorContent, mEmptyContent, mProgressContent);
			UI.show(mContent);
			ImageLoader.getInstance().displayImage(
					mTeam.getCrestURL(),
					mIcon,
					new DisplayImageOptions.Builder()
							.showImageOnFail(R.drawable.ic_player)
							.build()
			);
			if (mTeam.getName() != null && !mTeam.getName().equals("null")) {
				mName.setText(mTeam.getName());
			}
			if (mTeam.getShortName() != null && !mTeam.getShortName().equals("null")) {
				mShortName.setText(mTeam.getShortName());
			}
			if (mTeam.getSquadMarketValue() != null && !mTeam.getSquadMarketValue().equals("null")) {
				mSquadMarketValue.setText(mTeam.getSquadMarketValue());
			}
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.button_players:
				if (mListener != null) {
					mListener.onEvent(
							new EventData(Const.EVENT_CODE_SELECT_PLAYERS).setTeam(mTeam)
					);
				}
				dismiss();
				break;

			case R.id.button_fixtures:
				if (mListener != null) {
					mListener.onEvent(
							new EventData(Const.EVENT_CODE_SELECT_FIXTURES)
									.setTeam(mTeam)
									.setScores(mScores)
					);
				}
				dismiss();
				break;

			default:
				L.e(DialogTeam.class, "default value");
		}
	}

	private void setupUI(View view) {
		mProgressContent = (FrameLayout) view.findViewById(R.id.content_progress);
		mEmptyContent = (LinearLayout) view.findViewById(R.id.content_empty);
		mErrorContent = (LinearLayout) view.findViewById(R.id.content_error);
		mContent = (LinearLayout) view.findViewById(R.id.content);
		mIcon = (ImageView) view.findViewById(R.id.icon);
		mName = (TextView) view.findViewById(R.id.name);
		mShortName = (TextView) view.findViewById(R.id.short_name);
		mSquadMarketValue = (TextView) view.findViewById(R.id.squad_market_value);
		mFixtures = (Button) view.findViewById(R.id.button_fixtures);
		mPlayers = (Button) view.findViewById(R.id.button_players);
		mFixtures.setOnClickListener(this);
		mPlayers.setOnClickListener(this);
		UI.hide(mErrorContent, mEmptyContent, mProgressContent, mContent);
	}

	private void showContent() {
		if (mTeam == null) loadData();
		else {
			UI.hide(mErrorContent, mEmptyContent, mProgressContent);
			UI.show(mContent);
			ImageLoader.getInstance().displayImage(
					mTeam.getCrestURL(),
					mIcon,
					new DisplayImageOptions.Builder()
							.showImageOnFail(R.drawable.ic_player)
							.build()
			);
			if (mTeam.getName() != null && !mTeam.getName().equals("null")) mName.setText(mTeam.getName());
			if (mTeam.getShortName() != null && !mTeam.getShortName().equals("null")) mShortName.setText(mTeam.getShortName());
			if (mTeam.getSquadMarketValue() != null && !mTeam.getSquadMarketValue().equals("null")) {
				mSquadMarketValue.setText(mTeam.getSquadMarketValue());
			}
		}
	}

	private void loadData() {
		UI.hide(mErrorContent, mEmptyContent, mContent);
		UI.show(mProgressContent);
		if (mController != null) mController.getTeam(getActivity(), mScores.getTeamId(), this);
	}

}
