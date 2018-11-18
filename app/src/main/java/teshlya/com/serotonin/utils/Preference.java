package teshlya.com.serotonin.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class Preference {
    public static ArrayList<String> starList;

    public static void saveStarToSharedPrefs(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(starList);
        prefsEditor.putString("starList", json);
        prefsEditor.commit();
    }

    public static void getStarFromSharedPrefs(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("starList", "");
        starList = gson.fromJson(json, new TypeToken<ArrayList<String>>(){}.getType());
        if (starList == null)
            starList = new ArrayList<>();
    }
}
