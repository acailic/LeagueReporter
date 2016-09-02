package pma.leaguereporter.model.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import pma.leaguereporter.R;
import pma.leaguereporter.model.abstractions.AbstractRecyclerAdapter;
import pma.leaguereporter.model.objects.Player;

public class PlayersAdapter
		extends AbstractRecyclerAdapter<Player, PlayersAdapter.PlayersViewHolder> {

	public PlayersAdapter(ArrayList<Player> list) {
		super(list);
	}

	@Override
	public PlayersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater
				.from(parent.getContext())
				.inflate(R.layout.item_player, parent, false);
		return new PlayersViewHolder(view);
	}

	@Override
	public void onBindViewHolder(PlayersViewHolder holder, int position) {
		Player item = mList.get(position);
		holder.mName.setText(item.getName());
		holder.mNumber.setText(String.valueOf(item.getJerseyNumber()));
	}

	public static class PlayersViewHolder extends RecyclerView.ViewHolder {

		private TextView mName, mNumber;

		public PlayersViewHolder(View itemView) {
			super(itemView);
			mName = (TextView) itemView.findViewById(R.id.name);
			mNumber = (TextView) itemView.findViewById(R.id.number);
		}
	}

}
