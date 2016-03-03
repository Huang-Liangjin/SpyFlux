package jp.co.drecom.spyflux.util;

import android.util.Log;

/**
 * Created by huang_liangjin on 2016/03/03.
 */
public class SpyLog {
    private SpyLog() {}

    private static boolean mDebug = true;

    public static boolean isDebug() {
        return mDebug;
    }

    public static void setDebug(boolean debug) {
        SpyLog.mDebug = debug;
    }

    public static void printLog(String tag, String info) {
        if (mDebug) {
            Log.d(tag, info);
        }
    }
}
