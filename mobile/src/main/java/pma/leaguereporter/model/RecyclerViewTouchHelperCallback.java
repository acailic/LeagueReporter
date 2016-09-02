package pma.leaguereporter.model;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import pma.leaguereporter.model.interfaces.OnItemTouchAdapter;

public class RecyclerViewTouchHelperCallback extends ItemTouchHelper.Callback {

	private final OnItemTouchAdapter mAdapter;
	private boolean mIsLongPressDragEnabled;
	private boolean mIsItemViewSwipeEnabled;

	public RecyclerViewTouchHelperCallback(OnItemTouchAdapter adapter) {
		mAdapter = adapter;
	}

	@Override
	public boolean isLongPressDragEnabled() {
		return mIsLongPressDragEnabled;
	}

	@Override
	public boolean isItemViewSwipeEnabled() {
		return mIsItemViewSwipeEnabled;
	}

	@Override
	public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
		int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
		int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
		return makeMovementFlags(dragFlags, swipeFlags);
	}

	@Override
	public boolean onMove(
			RecyclerView recyclerView,
			RecyclerView.ViewHolder viewHolder,
			RecyclerView.ViewHolder target) {
		mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
		return true;
	}

	@Override
	public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
		mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
	}

	public void setDragEnabled(boolean dragEnabled) {
		mIsLongPressDragEnabled = dragEnabled;
	}

	public void setSwipeEnabled(boolean swipeEnabled) {
		mIsItemViewSwipeEnabled = swipeEnabled;
	}
}
