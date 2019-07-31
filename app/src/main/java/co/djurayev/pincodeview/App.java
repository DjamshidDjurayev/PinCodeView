package co.djurayev.pincodeview;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;
import android.util.DisplayMetrics;
import java.util.Locale;

public class App extends MultiDexApplication {
  private static App instance;

  @Override public void onCreate() {
    super.onCreate();
    instance = this;
    new Prefs(this);

    if (Prefs.getAppMode() == 1 ) {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    } else {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }
  }

  public static App getInstance() {
    return instance;
  }


  @Override protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
  }

  public void initAppLanguage(Context context) {
    LocaleHelper.initialize(context);
  }
}
