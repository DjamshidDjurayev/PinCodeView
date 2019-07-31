package co.djurayev.pincodeview;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
  private static final String APP_LANGUAGE = "APP_LANGUAGE";
  private static final String APP_MODE = "APP_MODE";
  private static SharedPreferences securePreferences;

  public Prefs(Context context) {
    securePreferences = context.getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE);
  }

  public static void setAppLanguage(String language) {
    securePreferences.edit().putString(APP_LANGUAGE, language).apply();
  }

  public static String getAppLanguage() {
    return securePreferences.getString(APP_LANGUAGE, "en");
  }

  // 0 = day
  // 1 = night
  public void setAppMode(int mode) {
    securePreferences.edit().putInt(APP_MODE, mode).apply();
  }

  public static int getAppMode() {
    return securePreferences.getInt(APP_MODE, 0);
  }
}
