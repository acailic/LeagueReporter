package pma.leaguereporter.model;

import android.support.v4.view.ViewPager;
import android.view.View;

import pma.leaguereporter.R;

public class IntroPageTransformer implements ViewPager.PageTransformer {

	View mSubtitle, mImage;
	int mPagePosition, mPageWidth, mPageHeight;
	float mPageWidthTimesPosition, mPageHeightTimesPosition, mAbsPosition;

	@Override
	public void transformPage(View page, float position) {
		setVars(page, position);
		if (position <= -1.0f || position >= 1.0f) {
			//page not visible
		} else if (position == 0.0f) {
			//page selected
		} else {
			//transition
			mSubtitle.setAlpha(1.0f - mAbsPosition * 1.5f);
			mSubtitle.setTranslationY(0.2f - mAbsPosition);

			if (mImage != null) {
				if (mPagePosition == 0) {
					mImage.setAlpha(1.0f - mAbsPosition * 1.5f);
					mImage.setTranslationX(mPageWidthTimesPosition * 0.8f);
				}

				if (mPagePosition == 1) {
					mImage.setAlpha(1.0f - mAbsPosition * 1.5f);
					mImage.setTranslationX(-mPageWidthTimesPosition * 0.8f);
				}

				if (mPagePosition == 2) {
					mImage.setAlpha(1.0f - mAbsPosition * 1.5f);
					mImage.setTranslationX(-mPageWidthTimesPosition * 0.8f);
				}

				if (position < 0) {
					//out
				} else {
					//in
					mImage.setTranslationY(mPageHeightTimesPosition * 0.1f);
				}
			}
		}
	}

	private void setVars(View page, float position) {
		mPagePosition = (int) page.getTag();
		mPageWidth = page.getWidth();
		mPageHeight = page.getHeight();
		mPageWidthTimesPosition = mPageWidth * position;
		mPageHeightTimesPosition = mPageHeight * position;
		mAbsPosition = Math.abs(position);

		mImage = page.findViewById(R.id.image);
		mSubtitle = page.findViewById(R.id.subtitle);
	}

}
