package se.roshauw.podplay.util;

import android.util.Log;

/**
 * Utility class
 * 
 * @author mats
 * 
 */
public class PodPlayUtil {

    /**
     * Tag to be used for logging
     */
    public static final String LOG_TAG = "PodPlay";

    private PodPlayUtil() {
    }

    /**
     * Log debug message
     */
    public static void logDebug(String message) {
        Log.d(LOG_TAG, message);
    }

    /**
     * Log info message
     */
    public static void logInfo(String message) {
        Log.i(LOG_TAG, message);
    }

    /**
     * Log error message
     */
    public static void logError(String message) {
        Log.e(LOG_TAG, message);
    }

    /**
     * Log an exception
     * 
     * @param e
     *            The exception to log
     */
    public static void logException(Exception e) {
        Log.wtf(LOG_TAG, e);
    }
}
