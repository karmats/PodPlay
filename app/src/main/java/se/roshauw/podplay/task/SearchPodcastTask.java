package se.roshauw.podplay.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import se.roshauw.podplay.parcel.Podcast;
import se.roshauw.podplay.parse.ItunesApiParser;

/**
 * Task to search for podcasts. When done updates the
 * adapter with the result
 *
 * @author mats
 */
public class SearchPodcastTask extends AsyncTask<String, Void, ArrayList<Podcast>> {

    private Context mContext;
    private ArrayAdapter<Podcast> mAdapter;

    public SearchPodcastTask(Context context, ArrayAdapter<Podcast> adapter) {
        this.mContext = context;
        this.mAdapter = adapter;
    }

    @Override
    protected ArrayList<Podcast> doInBackground(String... query) {
        return new ItunesApiParser(mContext).searchPodcasts(query[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<Podcast> result) {
        // Add the result to the adapter and notify that it has changed
        for (Podcast podcast : result) {
            mAdapter.add(podcast);
        }
        mAdapter.notifyDataSetChanged();
        super.onPostExecute(result);
    }
}
