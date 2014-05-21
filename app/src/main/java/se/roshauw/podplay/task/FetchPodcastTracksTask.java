package se.roshauw.podplay.task;

import java.util.ArrayList;

import se.roshauw.podplay.parcel.Podcast;
import se.roshauw.podplay.parcel.PodcastTrack;
import se.roshauw.podplay.parse.ItunesApiParser;
import se.roshauw.podplay.parse.RssPodcastParser;
import se.roshauw.podplay.util.PodPlayUtil;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

/**
 * Fetches podcasttracks from the podcast feed url. When done, updates the given
 * adapter with the result
 *
 * @author mats
 */
public class FetchPodcastTracksTask extends AsyncTask<Podcast, Void, ArrayList<PodcastTrack>> {

    private ArrayAdapter<PodcastTrack> mAdapter;
    private Context mContext;

    public FetchPodcastTracksTask(Context context, ArrayAdapter<PodcastTrack> adapter) {
        this.mAdapter = adapter;
        this.mContext = context;
    }

    @Override
    protected ArrayList<PodcastTrack> doInBackground(Podcast... podcast) {
        Podcast p = podcast[0];
        if (p.getFeedUrl() == null) {
            // If the feed url is missing, a query to the itunes api is needed
            Podcast tmp = new ItunesApiParser(mContext).getPodcastById(p.getId());
            p.setFeedUrl(tmp.getFeedUrl());
        }
        RssPodcastParser rssParser = new RssPodcastParser();
        return rssParser.getTracksForPodcast(p);
    }

    @Override
    protected void onPostExecute(ArrayList<PodcastTrack> result) {
        PodPlayUtil.logInfo("Fetching podcast tracks done, got " + result.size() + " podcast tracks");
        mAdapter.addAll(result);
        mAdapter.notifyDataSetChanged();
        super.onPostExecute(result);
    }

}
