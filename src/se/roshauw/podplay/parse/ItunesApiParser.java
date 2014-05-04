package se.roshauw.podplay.parse;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.roshauw.podplay.parcel.Podcast;
import se.roshauw.podplay.util.HttpConnectionHelper;
import se.roshauw.podplay.util.PodPlayUtil;
import android.content.Context;
import android.text.TextUtils;

/**
 * Parses JSON from itunes.apple.com
 * 
 * @author mats
 * 
 */
public class ItunesApiParser {

    // URL to itunes top podcasts the argument ^1 is the country to get the top
    // podcasts for. ^2 is the possible category
    private static final String TOP_PODCAST_URL = "https://itunes.apple.com/^1/rss/toppodcasts/limit=10/^2/explicit=true/json";
    // URL to itunes search api. Argument ^1 is the country. ^2 is the search
    // term
    private static final String SEARCH_PODCAST_URL = "https://itunes.apple.com/search?country=^1&entity=podcast&term=^2";
    // URL to itunes lookup api. Argument ^1 is the id for the podcast
    private static final String LOOKUP_PODCAST_URL = "https://itunes.apple.com/lookup?id=^1";

    private Context mContext;
    private HttpConnectionHelper connectionHelper;

    public ItunesApiParser(Context context) {
        this.mContext = context;
        this.connectionHelper = new HttpConnectionHelper();
    }

    /**
     * Searches the itunes api for podcast with a specific query
     * 
     * @param query
     *            The query string to search for
     * @return List of {@link Podcast}
     */
    public ArrayList<Podcast> searchPodcasts(String query) {
        String[] values = { mContext.getResources().getConfiguration().locale.getCountry(), query };
        // Format the url so country and search query will be in it
        String url = TextUtils.expandTemplate(SEARCH_PODCAST_URL, values).toString();
        return getItunesSearchResultForUrl(url);

    }

    /**
     * Get a podcast by its id
     * 
     * @param id
     *            The podcast id
     * @return {@link Podcast}
     */
    public Podcast getPodcastById(Long id) {
        String url = TextUtils.expandTemplate(LOOKUP_PODCAST_URL, new String[] { String.valueOf(id) }).toString();
        ArrayList<Podcast> result = getItunesSearchResultForUrl(url);
        // There is only one result one searching by id
        return result.get(0);
    }

    /**
     * Search for top podcasts based on category.
     * 
     * @param category
     *            The category to search for, can be null
     * @return List of {@link Podcast}
     */
    public ArrayList<Podcast> getTopPodcasts(Podcast.Category category) {
        ArrayList<Podcast> result = new ArrayList<Podcast>();
        String categoryArg = "";
        // Set possible category to the url
        if (category != null) {
            categoryArg = "genre=" + category.getItunesId();
        }
        String[] values = { mContext.getResources().getConfiguration().locale.getCountry(), categoryArg };
        // Format the url so country and category will be in it
        String url = TextUtils.expandTemplate(TOP_PODCAST_URL, values).toString();

        HttpURLConnection connection = connectionHelper.connectToUrl(url);
        if (connection != null) {
            String json = connectionHelper.readStreamFromConnection(connection);
            try {
                JSONObject jsonObj = new JSONObject(json);
                JSONObject feed = jsonObj.getJSONObject("feed");
                JSONArray entries = feed.getJSONArray("entry");
                // Parse all entries and add to the result
                for (int i = 0; i < entries.length(); i++) {
                    try {
                        JSONObject entry = entries.getJSONObject(i);
                        String name = entry.getJSONObject("im:name").getString("label");
                        String id = entry.getJSONObject("id").getJSONObject("attributes").getString("im:id");
                        Podcast podcast = new Podcast(Long.parseLong(id), name);
                        podcast.setDescription(entry.getJSONObject("summary").getString("label"));

                        // Get the img, we want the img with highest resolution,
                        // so
                        // we loop through the images and set the imgUrl to the
                        // img
                        // with highest height
                        int height = 0;
                        JSONArray imgArray = entry.getJSONArray("im:image");
                        for (int j = 0; j < imgArray.length(); j++) {
                            JSONObject imgJson = imgArray.getJSONObject(j);
                            int imgHeight = Integer.parseInt(imgJson.getJSONObject("attributes").getString("height"));
                            if (imgHeight > height) {
                                height = imgHeight;
                                podcast.setImgUrl(imgJson.getString("label"));
                            }
                        }
                        podcast.getCategoryIds().add(
                                Integer.parseInt(entry.getJSONObject("category").getJSONObject("attributes")
                                        .getString("im:id")));
                        result.add(podcast);
                    } catch (JSONException e) {
                        PodPlayUtil.logException(e);
                    }
                }

            } catch (JSONException e) {
                PodPlayUtil.logException(e);
            } finally {
                connection.disconnect();
            }
        }
        return result;
    }

    private ArrayList<Podcast> getItunesSearchResultForUrl(String url) {
        ArrayList<Podcast> result = new ArrayList<Podcast>();
        HttpURLConnection connection = connectionHelper.connectToUrl(url);
        if (connection != null) {
            String json = connectionHelper.readStreamFromConnection(connection);
            try {
                JSONObject jsonObj = new JSONObject(json);
                JSONArray searchResult = jsonObj.getJSONArray("results");
                // Parse all entries and add to the result
                for (int i = 0; i < searchResult.length(); i++) {
                    JSONObject entry = searchResult.getJSONObject(i);
                    String name = entry.getString("trackName");
                    Long id = entry.getLong("trackId");
                    Podcast podcast = new Podcast(id, name);
                    podcast.setImgUrl(entry.getString("artworkUrl60"));
                    podcast.setFeedUrl(entry.getString("feedUrl"));
                    JSONArray categoryArray = entry.getJSONArray("genreIds");
                    for (int j = 0; j < categoryArray.length(); j++) {
                        podcast.getCategoryIds().add(categoryArray.getInt(j));
                    }
                    result.add(podcast);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                PodPlayUtil.logError(e.getMessage());
            } finally {
                connection.disconnect();
            }
        }
        return result;
    }

}
