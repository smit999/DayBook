package com.daybook.global;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {

    private static final String SELECTED_LANGUAGE = "SELECTED_LANGUAGE";
    private static String KEY_IS_LOGIN = "IS_LOGIN";
    private static String IS_NOTIFICATION_ENABLE = "IS_NOTIFICATION_ENABLE";
    private static String CURRENCY_SYMBOL = "CURRENCY_SYMBOL";
    private static String ALARMED_SETUP_COMPLETED = "ALARMED_SETUP_COMPLETED";

    private static SharedPreferences getPref(Context context) {
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public static void setIsFirstTime(Context context, boolean flag) {
        getPref(context).edit().putBoolean(KEY_IS_LOGIN, flag).apply();
    }

    public static boolean getIsFirstTime(Context context) {
        return getPref(context).getBoolean(KEY_IS_LOGIN, true);
    }

    public static void setAlarmedSetupCompleted(Context context, boolean flag) {
        getPref(context).edit().putBoolean(ALARMED_SETUP_COMPLETED, flag).apply();
    }

    public static boolean getAlarmSetupCompleted(Context context) {
        return getPref(context).getBoolean(ALARMED_SETUP_COMPLETED, false);
    }

    public static void setCurrencySymbol(Context context, String symbol) {
        getPref(context).edit().putString(CURRENCY_SYMBOL, symbol).apply();
    }

    public static String getCurrencySymbol(Context context) {
        return getPref(context).getString(CURRENCY_SYMBOL, "₹");
    }

    public static int getLanguage(Context context) {
        return getPref(context).getInt(SELECTED_LANGUAGE, 1);
    }

    public static void setIsNotificationEnable(Context context, boolean isEnable) {
        getPref(context).edit().putBoolean(IS_NOTIFICATION_ENABLE, isEnable).apply();
    }

    public static boolean getIsNotificationEnable(Context context) {
        return getPref(context).getBoolean(IS_NOTIFICATION_ENABLE, true);
    }
}
