package com.app.auscope;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesManager 
{
	private static SharedPreferences mSharedPreferences;
	private static Editor mPreferencesEditor;
	
	@SuppressLint("CommitPrefEdits")
	public static void init(Context ApplicationContext)
	{
		mSharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(ApplicationContext);
		mPreferencesEditor = mSharedPreferences.edit();
	}
	
	public static void putBoolean(String KEY, boolean NewBoolean)
	{
		mPreferencesEditor.putBoolean(KEY, NewBoolean);
		mPreferencesEditor.commit();
	}
	public static boolean getBoolean(String KEY)
	{
		return mSharedPreferences.getBoolean(KEY, false);
	}
	
}
