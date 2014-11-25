package com.A1w0n.androidcommonutils.sharepreferenceutils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

/**
 * For writing and getting SharePreference. U can use to this encapsulate your own SP function
 * class, like: getting and setting user info.
 * 
 * Note : Editor.apply() 在 level 9（2.3）增加的
 */
public class SharePreferenceAgent {

	private static SharedPreferences.Editor mSPEditor = null;
	private static SharedPreferences mSPInstance = null;

	// Prevent users to initiate this class.
	public SharePreferenceAgent() {
	}

    /**
     * Get default SharePreferences instance
     *
     * @param paramContext
     * @return
     */
	private SharedPreferences.Editor getSPEditorInstance(Context paramContext) {
        return getSPInstance(paramContext).edit();
	}

    /**
     * Return the SharePreferences by spName
     *
     * @param spName Name of the SharePreferences
     * @return
     */
    private SharedPreferences.Editor getSPEditorInstance(Context context, String spName) {
        return getSPInstance(context, spName).edit();
    }

    /**
     * Get default SharePreferences instance
     *
     * @param context
     * @return
     */
    private SharedPreferences getSPInstance(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Get SharePreferences instance by spName
     *
     * @param context
     * @param spName Name of the SharePreferences
     * @return
     */
    private SharedPreferences getSPInstance(Context context, String spName) {
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    /**
     * Handling committing of a Editor
     *
     * @param editor
     */
    private void commitEditor(SharedPreferences.Editor editor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

	// ==========================Read============================
	/**
	 * Get int value in default SharePreferences.
	 */
	public int getValueFromSP(Context paramContext, String key, int defaultValue) {
		return getSPInstance(paramContext).getInt(key, defaultValue);
	}

    /**
     * Get int value in SharePreferences specified by spName.
     */
    public int getValueFromSP(Context paramContext, String spName, String key, int defaultValue) {
        return getSPInstance(paramContext, spName).getInt(key, defaultValue);
    }

	/**
	 * Get long value in sharePreference.
	 */
	public long getValueFromSP(Context paramContext, String key, long defaultValue) {
		return getSPInstance(paramContext).getLong(key, defaultValue);
	}

    /**
     * Get long value in sharePreference specified by spName.
     */
    public long getValueFromSP(Context paramContext, String spName, String key, long defaultValue) {
        return getSPInstance(paramContext, spName).getLong(key, defaultValue);
    }

	/**
	 * Get boolean value in sharePreference.
	 */
	public Boolean getValueFromSP(Context paramContext, String key, Boolean defaultValue) {
		return getSPInstance(paramContext).getBoolean(key, defaultValue);
	}

    /**
     * Get boolean value in sharePreference specified by spName.
     */
    public Boolean getValueFromSP(Context paramContext, String spName, String key, Boolean defaultValue) {
        return getSPInstance(paramContext, spName).getBoolean(key, defaultValue);
    }

	/**
	 * Get string value in sharePreference.
	 */
	public String getValueFromSP(Context paramContext, String key, String defaultValue) {
		return getSPInstance(paramContext).getString(key, defaultValue);
	}

    /**
     * Get string value in sharePreference specified by spName.
     */
    public String getValueFromSP(Context paramContext, String spName, String key, String defaultValue) {
        return getSPInstance(paramContext, spName).getString(key, defaultValue);
    }

	// ========================================================================

	// ===========================Write==========================================
	/**
	 * Put int value into sharePreference.
	 */
	public void putValueToSP(Context paramContext, String key, int value) {
		commitEditor(getSPEditorInstance(paramContext).putInt(key, value));
	}

    /**
     * Put int value into sharePreference specified by spName.
     */
    public void putValueToSP(Context paramContext, String spName, String key, int value) {
        commitEditor(getSPEditorInstance(paramContext, spName).putInt(key, value));
    }

	/**
	 * Put long value into sharePreference.
	 */
	public void putValueToSP(Context paramContext, String key, long value) {
        commitEditor(getSPEditorInstance(paramContext).putLong(key, value));
	}

    /**
     * Put long value into sharePreference specified by spName.
     */
    public void putValueToSP(Context paramContext, String spName, String key, long value) {
        commitEditor(getSPEditorInstance(paramContext, spName).putLong(key, value));
    }

	/**
	 * Put boolean value into sharePreference.
	 */
	public void putValueToSP(Context paramContext, String key, boolean value) {
        commitEditor(getSPEditorInstance(paramContext).putBoolean(key, value));
	}

    /**
     * Put boolean value into sharePreference specified by spName.
     */
    public void putValueToSP(Context paramContext, String spName, String key, boolean value) {
        commitEditor(getSPEditorInstance(paramContext, spName).putBoolean(key, value));
    }

	/**
	 * Put string value into sharePreference.
	 */
	public void putValueToSP(Context paramContext, String key, String value) {
        commitEditor(getSPEditorInstance(paramContext).putString(key, value));
	}

    /**
     * Put string value into sharePreference specified by spName.
     */
    public void putValueToSP(Context paramContext, String spName, String key, String value) {
        commitEditor(getSPEditorInstance(paramContext, spName).putString(key, value));
    }

	// ===============================================================


	// =============================Clear===============================
    /**
     * Clear value by key in default SharePreferences.
     *
     * @param paramContext
     * @param key
     */
	public void clearValueFromSP(Context paramContext, String key) {
		getSPEditorInstance(paramContext).remove(key).apply();
	}

    /**
     * Clear value by key in SharePreferences specified by spName.
     *
     * @param paramContext
     * @param key
     */
    public void clearValueFromSP(Context paramContext, String spName, String key) {
        getSPEditorInstance(paramContext, spName).remove(key).apply();
    }
	// ===============================================================

}
