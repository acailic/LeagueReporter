package pma.leaguereporter.model.listeners;

import android.animation.ArgbEvaluator;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import pma.leaguereporter.R;
import pma.leaguereporter.model.adapters.IntroPagerAdapter;

public abstract class OnIntroSwipeListener implements ViewPager.OnPageChangeListener {

	private ViewPager mViewPager;
	private ImageView[] mIndicators;
	private AppCompatActivity mActivity;
	private IntroPagerAdapter mAdapter;
	private ArgbEvaluator mEvaluator;
	private int mCount;

	public OnIntroSwipeListener(
			AppCompatActivity activity,
			ViewPager viewPager,
			IntroPagerAdapter adapter,
			ImageView[] indicators) {
		mViewPager = viewPager;
		mIndicators = indicators;
		mActivity = activity;
		mAdapter = adapter;
		mEvaluator = new ArgbEvaluator();
		mCount = mAdapter.getCount();
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		int colorUpdate = (Integer) mEvaluator.evaluate(
				positionOffset,
				ContextCompat.getColor(mActivity, mAdapter.getColor(position)),
				ContextCompat.getColor(
						mActivity,
						mAdapter.getColor(position == mCount - 1 ? position : position + 1))
		);
		mViewPager.setBackgroundColor(colorUpdate);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			mActivity.getWindow().setStatusBarColor(colorUpdate);
			mActivity.getWindow().setNavigationBarColor(colorUpdate);
		}
	}

	@Override
	public void onPageSelected(int position) {
		updateIndicators(position);
		mViewPager.setBackgroundColor(
				ContextCompat.getColor(mActivity, mAdapter.getColor(position))
		);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			mActivity.getWindow().setStatusBarColor(
					ContextCompat.getColor(mActivity, mAdapter.getColor(position))
			);
			mActivity.getWindow().setNavigationBarColor(
					ContextCompat.getColor(mActivity, mAdapter.getColor(position))
			);
		}
		pageChanged(position, mCount);
	}

	@Override
	public void onPageScrollStateChanged(int state) {}

	abstract public void pageChanged(int position, int count);

	void updateIndicators(int position) {
		for (int i = 0; i < mIndicators.length; i++) {
			mIndicators[i].setBackgroundResource(
					i == position ? R.drawable.indicator_selected : R.drawable.indicator_unselected
			);
		}
	}
}
