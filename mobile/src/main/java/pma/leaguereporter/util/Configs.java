package pma.leaguereporter.util;

import android.content.Context;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.L;
import com.nostra13.universalimageloader.utils.StorageUtils;
import pma.leaguereporter.R;

public class Configs {

	public static final boolean IS_DEBUG_MODE = true;
	public static final String APP_TAG = "TAG";

	public static final long TIME_TO_EXIT = 400; //ms
	public static final long TIME_TO_UPDATE_LEAGUES = 21600000; //6 hours
	public static final long TIME_TO_UPDATE_FIXTURES = 3600000; //1 hours
	public static final long TIME_TO_UPDATE_SCORES = 3600000; //1 hours
	public static final long TIME_TO_UPDATE_TEAM = 43200000; //12 hours
	public static final long TIME_TO_UPDATE_PLAYERS = 21600000; //6 hours
	public static final long TIME_TO_UPDATE_TEAM_FIXTURES = 3600000; //1 hours

	public static ImageLoaderConfiguration getImageLoaderConfig(Context context) {
		L.writeLogs(false);
		return new ImageLoaderConfiguration.Builder(context)
				.threadPoolSize(3)
				.threadPriority(Thread.MIN_PRIORITY + 2)
				.writeDebugLogs()
				.defaultDisplayImageOptions(getDefaultImageOption())
				.diskCache(new UnlimitedDiskCache(StorageUtils.getCacheDirectory(context, true)))
				.build();
	}

	private static DisplayImageOptions getDefaultImageOption() {
		return new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.showImageOnLoading(R.drawable.ic_cup)
				.build();
	}
}
