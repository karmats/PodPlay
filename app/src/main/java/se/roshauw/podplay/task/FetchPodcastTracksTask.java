package se.roshauw.podplay.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import se.roshauw.podplay.adapter.ViewTracksAdapter;
import se.roshauw.podplay.parcel.Podcast;
import se.roshauw.podplay.parse.ItunesApiParser;
import se.roshauw.podplay.parse.RssPodcastParser;
import se.roshauw.podplay.util.PodPlayUtil;

/**
 * Fetches podcasttracks from the podcast feed url. When done, updates the given
 * adapter with the result
 *
 * @author mats
 */
public class FetchPodcastTracksTask extends AsyncTask<Podcast, Void, Podcast> {

    private Context mContext;
    private ViewTracksAdapter mAdapter;
    private TextView mDescriptionTextView;

    public FetchPodcastTracksTask(Context context, ViewTracksAdapter adapter, TextView descriptionTextView) {
        this.mContext = context;
        this.mAdapter = adapter;
        this.mDescriptionTextView = descriptionTextView;
    }

    @Override
    protected Podcast doInBackground(Podcast... podcast) {
        // TODO Rewrite this
        Podcast p = podcast[0];
        Podcast tmp;
        if (p.getFeedUrl() == null) {
            // If the feed url is missing, a query to the itunes api is needed
            tmp = new ItunesApiParser(mContext).getPodcastById(p.getId());
            p.setFeedUrl(tmp.getFeedUrl());
        }
        RssPodcastParser rssParser = new RssPodcastParser();
        rssParser.enrichPodcastWithTracks(p);
        PodPlayUtil.logDebug("Podcast has description " + p.getDescription());
        return p;
    }

    @Override
    protected void onPostExecute(Podcast result) {
        mAdapter.addTracks(result.getTracks());
        mAdapter.notifyDataSetChanged();
        mDescriptionTextView.setText(result.getDescription());
        super.onPostExecute(result);
    }

}
