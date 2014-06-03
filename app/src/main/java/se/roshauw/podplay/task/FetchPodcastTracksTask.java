package se.roshauw.podplay.task;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

import se.roshauw.podplay.adapter.ViewTracksAdapter;
import se.roshauw.podplay.parcel.Podcast;
import se.roshauw.podplay.parcel.PodcastTrack;
import se.roshauw.podplay.parse.ItunesApiParser;
import se.roshauw.podplay.parse.RssPodcastParser;
import se.roshauw.podplay.util.PodPlayUtil;

/**
 * Fetches podcasttracks from the podcast feed url. When done, updates the given
 * adapter with the result
 *
 * @author mats
 */
public class FetchPodcastTracksTask extends AsyncTask<Podcast, Void, ArrayList<PodcastTrack>> {

    private ViewTracksAdapter mAdapter;
    private Context mContext;

    public FetchPodcastTracksTask(Context context, ViewTracksAdapter adapter) {
        this.mAdapter = adapter;
        this.mContext = context;
    }

    @Override
    protected ArrayList<PodcastTrack> doInBackground(Podcast... podcast) {
        // TODO Rewrite this
        Podcast p = podcast[0];
        Podcast tmp;
        if (p.getFeedUrl() == null) {
            // If the feed url is missing, a query to the itunes api is needed
            tmp = new ItunesApiParser(mContext).getPodcastById(p.getId());
            p.setFeedUrl(tmp.getFeedUrl());
        }
        RssPodcastParser rssParser = new RssPodcastParser();
        tmp = rssParser.enrichPodcastWithTracks(p);
        p.setDescription(tmp.getDescription());
        return p.getTracks();
    }

    @Override
    protected void onPostExecute(ArrayList<PodcastTrack> result) {
        PodPlayUtil.logInfo("Fetching podcast tracks done, got " + result.size() + " podcast tracks");
        mAdapter.addTracks(result);
        mAdapter.notifyDataSetChanged();
        super.onPostExecute(result);
    }

}
