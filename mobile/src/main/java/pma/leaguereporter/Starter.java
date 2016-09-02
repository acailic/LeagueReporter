package pma.leaguereporter;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import pma.leaguereporter.util.Configs;
import pma.leaguereporter.controllers.Controller;

public class Starter extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Context context = getApplicationContext();
		Controller.init(context);
		ImageLoader.getInstance().init(Configs.getImageLoaderConfig(context));
	}

}
