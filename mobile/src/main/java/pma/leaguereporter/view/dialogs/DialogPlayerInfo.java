package pma.leaguereporter.view.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pma.leaguereporter.R;
import pma.leaguereporter.model.objects.Player;
import pma.leaguereporter.util.UI;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DialogPlayerInfo extends AppCompatDialogFragment {

	public static final String TAG = "FragmentPlayerInfo";

	private TextView mName;
	private TextView mJersey;
	private TextView mPosition;
	private TextView mNationality;
	private TextView mMarketValue;
	private TextView mDateOfBirth;
	private TextView mContractUntil;
	private Player mPlayer;

	public static DialogPlayerInfo getInstance(@Nullable Bundle data) {
		DialogPlayerInfo fragment = new DialogPlayerInfo();
		fragment.setArguments(data == null ? new Bundle() : data);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogTeam.STYLE_NO_TITLE, R.style.AppTheme_Dialog);
		if (mPlayer == null) mPlayer = getArguments().getParcelable("player");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_player_info, container, false);
		setupUI(view);
		showContent();
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mName != null) mName = null;
		if (mJersey != null) mJersey = null;
		if (mPosition != null) mPosition = null;
		if (mNationality != null) mNationality = null;
		if (mMarketValue != null) mMarketValue = null;
		if (mDateOfBirth != null) mDateOfBirth = null;
		if (mContractUntil != null) mContractUntil = null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mPlayer != null) mPlayer = null;
	}

	private void setupUI(View view) {
		mName = (TextView) view.findViewById(R.id.name);
		mJersey = (TextView) view.findViewById(R.id.jersey);
		mPosition = (TextView) view.findViewById(R.id.position);
		mNationality = (TextView) view.findViewById(R.id.nationality);
		mMarketValue = (TextView) view.findViewById(R.id.market_value);
		mDateOfBirth = (TextView) view.findViewById(R.id.date_of_birth);
		mContractUntil = (TextView) view.findViewById(R.id.contract_until);
	}

	private void showContent() {
		mJersey.setText(String.valueOf(mPlayer.getJerseyNumber()));
		mName.setText(mPlayer.getName());
		mDateOfBirth.setText(
				new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
						.format(new Date(mPlayer.getDateOfBirth()))
		);
		mNationality.setText(String.format(
				getString(R.string.player_info_nationality),
				mPlayer.getNationality())
		);
		mPosition.setText(String.format(
				getString(R.string.player_info_position),
				mPlayer.getPosition())
		);
		if (mPlayer.getMarketValue() != null && !mPlayer.getMarketValue().equals("null")) {
			mMarketValue.setText(String.format(
					getString(R.string.player_info_market_value),
					mPlayer.getMarketValue())
			);
			UI.show(mMarketValue);
		}
		if (mPlayer.getContractUntil() > 0) {
			mContractUntil.setText(String.format(
					getString(R.string.player_info_contract_until),
					new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
							.format(new Date(mPlayer.getContractUntil())))
			);
			UI.show(mContractUntil);
		}
	}

}
