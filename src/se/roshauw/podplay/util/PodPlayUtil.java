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
     * Intent extras argument key for podcast track
     */
    public static final String EXTRA_PODCAST_TRACK = "extraPodcastTrack";
    /**
     * Tag for AddPodcastFragment
     */
    public static final String TAG_ADD_PODCAST_FRAGMENT = "tagAddPodcastFragment";

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

    public static void logException(Exception e) {
        Log.e(LOG_TAG, e.getClass().getCanonicalName() + " : " + e.getMessage());
    }
}
