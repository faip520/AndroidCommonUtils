package com.A1w0n.androidcommonutils.sharepreferenceutils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * For writing and getting SharePreference. U can use to this encapsulate your own SP function
 * class, like: getting and setting user info.
 */
public class SharePreferenceAgent {

	private static SharedPreferences.Editor mSPEditor = null;
	private static SharedPreferences mSPInstance = null;

	// Prevent users to initiate this class.
	private SharePreferenceAgent() {
	}

	private static SharedPreferences getSPInstance(Context paramContext) {
		if (mSPInstance == null)
			mSPInstance = PreferenceManager.getDefaultSharedPreferences(paramContext);
		return mSPInstance;
	}

	/**
	 * Get sharePreference editor.
	 */
	private static SharedPreferences.Editor getSPEditorInstance(Context paramContext) {
		if (mSPEditor == null)
			mSPEditor = getSPInstance(paramContext).edit();
		return mSPEditor;
	}

	// ==========================Read============================
	/**
	 * Get int value in sharePreference.
	 */
	public static int getValueFromSP(Context paramContext, String paramString, int paramInt) {
		return getSPInstance(paramContext).getInt(paramString, paramInt);
	}

	/**
	 * Get long value in sharePreference.
	 */
	public static long getValueFromSP(Context paramContext, String paramString, long paramLong) {
		return getSPInstance(paramContext).getLong(paramString, paramLong);
	}

	/**
	 * Get boolean value in sharePreference.
	 */
	public static Boolean getValueFromSP(Context paramContext, String paramString, Boolean paramBoolean) {
		return getSPInstance(paramContext).getBoolean(paramString, paramBoolean);
	}

	/**
	 * Get string value in sharePreference.
	 */
	public static String getValueFromSP(Context paramContext, String paramString1, String paramString2) {
		return getSPInstance(paramContext).getString(paramString1, paramString2);
	}

	// ========================================================================

	// ===========================Write==========================================
	/**
	 * Put int value into sharePreference.
	 */
	public static void putValueToSP(Context paramContext, String paramString, int paramInt) {
		getSPEditorInstance(paramContext).putInt(paramString, paramInt).apply();
	}

	/**
	 * Put long value into sharePreference.
	 */
	public static void putValueToSP(Context paramContext, String paramString, long paramLong) {
		getSPEditorInstance(paramContext).putLong(paramString, paramLong).apply();
	}

	/**
	 * Put boolean value into sharePreference.
	 */
	public static void putValueToSP(Context paramContext, String paramString, Boolean paramBoolean) {
		getSPEditorInstance(paramContext).putBoolean(paramString, paramBoolean).apply();
	}

	/**
	 * Put string value into sharePreference.
	 */
	public static void putValueToSP(Context paramContext, String paramString1, String paramString2) {
		getSPEditorInstance(paramContext).putString(paramString1, paramString2).apply();
	}

	// ===============================================================

	// =============================Clear===============================
	public static void clearValueFromSP(Context paramContext, String key) {
		getSPEditorInstance(paramContext).remove(key).apply();
	}
	// ===============================================================
}
