package lk.learningApp.LocalDatabase;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager { // You can store Boolean, Int, Float, Long and String Datatype Using This local Database.

    private final SharedPreferences sharedPreferences;

    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public Boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }
    public void putFloat(String key, float value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key,value);
        editor.apply();
    }
    public float getFloat(String key){
        return sharedPreferences.getFloat(key,0);
    }
    public void putInt(String key, int value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,value);
        editor.apply();
    }
    public int getInt(String key){
        return sharedPreferences.getInt(key,0);
    }
    public void putLong(String key, long value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key,value);
        editor.apply();
    }
    public long getLong(String key){
        return sharedPreferences.getLong(key,0);
    }
    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}