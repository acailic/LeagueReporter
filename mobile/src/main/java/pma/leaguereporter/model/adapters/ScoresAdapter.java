package pma.leaguereporter.model.adapters;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import pma.leaguereporter.R;
import pma.leaguereporter.model.abstractions.AbstractRecyclerAdapter;
import pma.leaguereporter.model.objects.Scores;

public class ScoresAdapter extends AbstractRecyclerAdapter<Scores, ScoresAdapter.ScoresViewHolder> {

	private Resources mResources;

	public ScoresAdapter(Resources resources, ArrayList<Scores> list) {
		super(list);
		mResources = resources;
	}

	@Override
	public ScoresViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_scores, parent, false);
		return new ScoresViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ScoresViewHolder holder, int position) {
		Scores item = mList.get(position);
		ImageLoader.getInstance().displayImage(item.getCrestURI(), holder.mIcon);
		holder.mRank.setText(String.valueOf(item.getRank()));
		holder.mTeam.setText(item.getTeam());
		holder.mPlayedGames.setText(String.valueOf(item.getPlayedGames()));
		holder.mGoals.setText(String.valueOf(item.getGoals()));
		holder.mAgainstDifference.setText(String.format(
				mResources.getString(R.string.scores_against_difference),
				item.getGoalAgainst(),
				item.getGoalDifference()));
		holder.mPoints.setText(String.valueOf(item.getPoints()));
	}

	public static class ScoresViewHolder extends RecyclerView.ViewHolder {

		private ImageView mIcon;
		private TextView mRank,
				mTeam,
				mPlayedGames,
				mGoals,
				mAgainstDifference,
				mPoints;

		public ScoresViewHolder(View itemView) {
			super(itemView);
			mIcon = (ImageView) itemView.findViewById(R.id.icon);
			mRank = (TextView) itemView.findViewById(R.id.rank);
			mTeam = (TextView) itemView.findViewById(R.id.team);
			mPlayedGames = (TextView) itemView.findViewById(R.id.played_games);
			mGoals = (TextView) itemView.findViewById(R.id.goals);
			mAgainstDifference = (TextView) itemView.findViewById(R.id.against_difference);
			mPoints = (TextView) itemView.findViewById(R.id.points);
		}
	}

}
