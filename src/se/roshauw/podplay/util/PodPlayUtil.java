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
     * Intent extras argument key for podcast
     */
    public static final String EXTRA_PODCAST = "extraPodcast";

    /**
     * Tag to be used for logging
     */
    public static final String LOG_TAG = "PodPlay";

    private PodPlayUtil() {
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
}
