package com.daybook.utils;

import android.util.Log;

public class Logger {
    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void d(String msg) {
        Log.d("Logger Log>>", msg);
    }
}