package se.roshauw.podplay.task;

import java.util.ArrayList;

import se.roshauw.podplay.adapter.CoverFlowAdapter;
import se.roshauw.podplay.parcel.Podcast;
import se.roshauw.podplay.parse.ItunesApiParser;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;

/**
 * Fetches top podcasts from itunes api. When done, updates the given
 * {@link ViewPager} with the result
 * 
 * @author mats
 * 
 */
public class FetchTopPodcastsTask extends AsyncTask<Podcast.Category, Void, ArrayList<Podcast>> {

    private Context mContext;
    private ViewPager mViewPager;

    public FetchTopPodcastsTask(Context context, ViewPager viewPager) {
        this.mContext = context;
        this.mViewPager = viewPager;
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
        CoverFlowAdapter adapter = (CoverFlowAdapter) mViewPager.getAdapter();
        adapter.addPodcasts(result);
        adapter.notifyDataSetChanged();

        // Set current item to the one in the middle so a cool transition shows,
        // and the carousel effect will kick in
        if (!result.isEmpty()) {
            mViewPager.setCurrentItem((result.size() * CoverFlowAdapter.CAROUSEL_LOOPS) / 2);
        }
        super.onPostExecute(result);
    }
}
