package co.djurayev.pincodeview;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.StringDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

public class LocaleHelper {

    @Retention(RetentionPolicy.SOURCE) @StringDef({ ENGLISH, RUSSIAN })
    public @interface LocaleDef {
        String[] SUPPORTED_LOCALES = { ENGLISH, RUSSIAN };
    }

    public static final String ENGLISH = "en";
    public static final String RUSSIAN = "ru";

    public static void initialize(Context context) {
        setLocale(context, RUSSIAN);
    }

    public static void initialize(Context context, @LocaleDef String defaultLanguage) {
        setLocale(context, defaultLanguage);
    }

    public static void initialize(Context context, Locale defaultLocale) {
        updateResourcesWithLocale(context, defaultLocale);
    }

    public static boolean setLocale(Context context, @LocaleDef String language) {
        return updateResources(context, language);
    }

    public static Context setLocale(Context context) {
        Locale locale = new Locale(Prefs.getAppLanguage());
        updateResourcesWithLocale(context, locale);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        context.createConfigurationContext(configuration);
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }

    private static boolean updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        updateResourcesWithLocale(context, locale);
        Locale.setDefault(locale);
        return true;
    }

    private static void updateResourcesWithLocale(Context context, Locale locale) {
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        context.createConfigurationContext(configuration);
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    public static String getDisplayLanguage(String languageCode) {
        String language = "English";

        switch (languageCode) {
            case "ru":
            case "rus":
                language = "Russian";
                break;
            case "en":
            case "eng":
                language = "English";
                break;
        }

        return language;
    }

    public static String getDisplayLanguageByCode(String languageCode) {
        Locale locale = new Locale(languageCode);
        return locale.getDisplayLanguage();
    }
}