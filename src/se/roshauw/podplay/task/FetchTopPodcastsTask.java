package se.roshauw.podplay.task;

import java.util.ArrayList;

import se.roshauw.podplay.adapter.CoverFlowAdapter;
import se.roshauw.podplay.parcel.Podcast;
import se.roshauw.podplay.parse.ItunesApiParser;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Fetches top podcasts from itunes api. When done, updates the given
 * {@link CoverFlowAdapter} with the result
 * 
 * @author mats
 * 
 */
public class FetchTopPodcastsTask extends AsyncTask<Podcast.Category, Void, ArrayList<Podcast>> {

    private Context mContext;
    private CoverFlowAdapter mCoverFlowAdapter;

    public FetchTopPodcastsTask(Context context, CoverFlowAdapter coverFlowAdapter) {
        this.mContext = context;
        this.mCoverFlowAdapter = coverFlowAdapter;
    }

    @Override
    protected ArrayList<Podcast> doInBackground(Podcast.Category... category) {
        return new ItunesApiParser(mContext).getTopPodcasts(category == null || category.length <= 0 ? null
                : category[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<Podcast> result) {
        // Add the podcasts to the coverflow adapter and notify that it has
        // changed
        for (Podcast podcast : result) {
            mCoverFlowAdapter.addPodcast(podcast);
        }
        mCoverFlowAdapter.notifyDataSetChanged();
        super.onPostExecute(result);
    }
}
