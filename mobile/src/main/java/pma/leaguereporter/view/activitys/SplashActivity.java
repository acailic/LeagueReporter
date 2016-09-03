package pma.leaguereporter.view.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import pma.leaguereporter.R;
import pma.leaguereporter.R;
/**
 * Created by a.ilic on 8/25/2016.
 */
public class SplashActivity extends Activity {

    private static int SPLASH_TIME_OUT = 4000; // splash ce biti vidljiv minimum SPLASH_TIME_OUT milisekundi
    private boolean splashScreenShow;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        splashScreenShow = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(getString(R.string.pref_splash),true);
        // uradi inicijalizaciju u pozadinksom threadu
        new InitTask().execute();

    }

    private class InitTask extends AsyncTask<Void, Void, Void>
    {
        private long startTime;

        @Override
        protected void onPreExecute()
        {
            startTime = System.currentTimeMillis();
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            if( splashScreenShow ){
                continueLogin();
            }
            startMainActivity();

            return null;
        }

        private void continueLogin()
        {
            // sacekaj tako da splash bude vidljiv minimum SPLASH_TIME_OUT milisekundi
            long timeLeft = SPLASH_TIME_OUT - (System.currentTimeMillis() - startTime);
            if(timeLeft < 0) timeLeft = 0;
            SystemClock.sleep(timeLeft);
        }
    }

    private void startMainActivity()
    {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish(); // da ne bi mogao da ode back na splash
    }
}