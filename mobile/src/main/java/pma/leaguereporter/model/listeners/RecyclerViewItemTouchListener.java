package pma.leaguereporter.model.listeners;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import pma.leaguereporter.model.interfaces.OnItemClickListener;

public class RecyclerViewItemTouchListener implements RecyclerView.OnItemTouchListener {

	private OnItemClickListener mListener;
	private GestureDetector mGestureDetector;

	public RecyclerViewItemTouchListener(
			Context context,
			final RecyclerView recyclerView,
			final OnItemClickListener listener) {
		this.mListener = listener;
		mGestureDetector = new GestureDetector(
				context,
				new GestureDetector.SimpleOnGestureListener() {
					@Override
					public boolean onSingleTapUp(MotionEvent e) {
						return true;
					}

					@Override
					public void onLongPress(MotionEvent e) {
						View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
						if(childView != null && listener != null) {
							listener.onItemLongClick(
									childView,
									recyclerView.getChildAdapterPosition(childView));
						}
					}
		});
	}

	@Override
	public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
		View childView = view.findChildViewUnder(e.getX(), e.getY());
		if(childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
			mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
		}
		return false;
	}

	@Override
	public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }

	@Override
	public void onRequestDisallowInterceptTouchEvent (boolean disallowIntercept){}
}
