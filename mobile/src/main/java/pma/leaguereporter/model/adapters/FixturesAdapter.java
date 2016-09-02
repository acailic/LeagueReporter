package pma.leaguereporter.model.adapters;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import pma.leaguereporter.R;
import pma.leaguereporter.model.abstractions.AbstractRecyclerAdapter;
import pma.leaguereporter.model.objects.Fixture;
import pma.leaguereporter.util.Const;
import pma.leaguereporter.util.UI;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FixturesAdapter
		extends AbstractRecyclerAdapter<Fixture, FixturesAdapter.FixturesViewHolder> {

	private Resources mResources;
	private Date mDate;
	private Date mDateTmp;
	private Calendar mCalendar;
	private Calendar mCalendarTmp;

	public FixturesAdapter(Resources resources, ArrayList<Fixture> list) {
		super(list);
		mResources = resources;
		mCalendar = Calendar.getInstance();
		mCalendarTmp = Calendar.getInstance();
		mDate = new Date();
		mDateTmp = new Date();
	}

	@Override
	public FixturesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater
				.from(parent.getContext())
				.inflate(R.layout.item_fixture, parent, false);
		return new FixturesViewHolder(view);
	}

	@Override
	public void onBindViewHolder(FixturesViewHolder holder, int position) {
		Fixture item = mList.get(position);
		holder.mHomeTeamName.setText(item.getHomeTeamName());
		holder.mAwayTeamName.setText(item.getAwayTeamName());
		int goalsHomeTeam = item.getGoalsHomeTeam();
		int goalsAwayTeam = item.getGoalsAwayTeam();
		if (goalsHomeTeam >= 0 && goalsAwayTeam >= 0) {
			UI.hide(holder.mDate, holder.mBell);
			UI.show(holder.mResult);
			holder.mResult.setText(String.format(mResources.getString(
					R.string.fixtures_list_item_result),
					goalsHomeTeam,
					goalsAwayTeam)
			);
			holder.mResult.setBackgroundResource(R.drawable.fixtures_list_item_result_background);
			if (goalsHomeTeam > goalsAwayTeam) {
				holder.mHomeTeamName.setTextColor(
						mResources.getColor(R.color.fixtures_list_item_win_name)
				);
				holder.mAwayTeamName.setTextColor(
						mResources.getColor(R.color.fixtures_list_item_lose_name)
				);
			}
			else if (goalsHomeTeam < goalsAwayTeam) {
				holder.mAwayTeamName.setTextColor(
						mResources.getColor(R.color.fixtures_list_item_win_name)
				);
				holder.mHomeTeamName.setTextColor(
						mResources.getColor(R.color.fixtures_list_item_lose_name)
				);
			}
			else {
				holder.mAwayTeamName.setTextColor(
						mResources.getColor(R.color.fixtures_list_item_draw_name)
				);
				holder.mHomeTeamName.setTextColor(
						mResources.getColor(R.color.fixtures_list_item_draw_name)
				);
			}
		}
		else {
			UI.hide(holder.mResult, holder.mBell);
			UI.show(holder.mDate);
			if (item.isNotified()) UI.show(holder.mBell);
			DateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
			holder.mDate.setText(format.format(new Date(item.getDate())));
			holder.mAwayTeamName.setTextColor(
					mResources.getColor(R.color.fixtures_list_item_name)
			);
			holder.mHomeTeamName.setTextColor(
					mResources.getColor(R.color.fixtures_list_item_name)
			);
		}

		mDate.setTime(item.getDate());
		mCalendar.setTime(mDate);
		if (position > 0) {
			mDateTmp.setTime(mList.get(position - 1).getDate());
			mCalendarTmp.setTime(mDateTmp);
		}
		if (position == 0
				|| (mCalendar.get(Calendar.DAY_OF_YEAR)
				!= mCalendarTmp.get(Calendar.DAY_OF_YEAR))) {
			mDateTmp.setTime(System.currentTimeMillis());
			mCalendarTmp.setTime(mDateTmp);
			if (mCalendar.get(Calendar.YEAR) == mCalendarTmp.get(Calendar.YEAR)
					&& mCalendar.get(Calendar.DAY_OF_YEAR)
					== mCalendarTmp.get(Calendar.DAY_OF_YEAR)) {
				holder.mHeader.setText(
						mResources.getString(R.string.fixtures_list_item_header_today));
			}
			else if (mCalendar.get(Calendar.YEAR) == mCalendarTmp.get(Calendar.YEAR)
					&& mCalendar.get(Calendar.DAY_OF_YEAR)
					== mCalendarTmp.get(Calendar.DAY_OF_YEAR) + 1) {
				holder.mHeader.setText(
						mResources.getString(R.string.fixtures_list_item_header_tomorrow));
			}
			else {
				holder.mHeader.setText(
						new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
								.format(new Date(item.getDate())));
			}
			UI.show(holder.mHeader);
		}
		else UI.hide(holder.mHeader);
	}

	public static class FixturesViewHolder extends RecyclerView.ViewHolder
			implements View.OnCreateContextMenuListener {

		private TextView mHomeTeamName,
				mAwayTeamName,
				mDate,
				mResult,
				mHeader;
		private ImageView mBell;

		public FixturesViewHolder(View itemView) {
			super(itemView);
			mHomeTeamName = (TextView) itemView.findViewById(R.id.home_team_name);
			mAwayTeamName = (TextView) itemView.findViewById(R.id.away_team_name);
			mDate = (TextView) itemView.findViewById(R.id.date);
			mResult = (TextView) itemView.findViewById(R.id.result);
			mHeader = (TextView) itemView.findViewById(R.id.header);
			mBell = (ImageView) itemView.findViewById(R.id.bell);
			itemView.setOnCreateContextMenuListener(this);
		}

		@Override
		public void onCreateContextMenu(
				ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
			menu.add(
					Const.GROUP_FIXTURES, getAdapterPosition(), 0,
					R.string.context_fixture_notification);
		}
	}
}
