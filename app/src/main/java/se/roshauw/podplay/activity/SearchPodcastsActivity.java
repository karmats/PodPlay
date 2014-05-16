package se.roshauw.podplay.activity;

import se.roshauw.podplay.MainActivity;
import se.roshauw.podplay.R;
import se.roshauw.podplay.parcel.Podcast;
import se.roshauw.podplay.task.SearchPodcastTask;
import se.roshauw.podplay.util.PodPlayUtil;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Activity to search for podcasts and present it in a list
 * 
 * @author mats
 * 
 */
public class SearchPodcastsActivity extends ListActivity {

    private ArrayAdapter<Podcast> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        mAdapter = new ArrayAdapter<Podcast>(this, android.R.layout.simple_list_item_1);
        setListAdapter(mAdapter);

        Intent intent = getIntent();
        PodPlayUtil.logInfo("Creating list view");
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            PodPlayUtil.logInfo("Searching for " + query);
            new SearchPodcastTask(SearchPodcastsActivity.this, mAdapter).execute(query);
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        PodPlayUtil.logInfo("Item " + position + " clicked");
        Intent mainIntent = new Intent(SearchPodcastsActivity.this, MainActivity.class);
        mainIntent.putExtra(PodPlayUtil.EXTRA_PODCAST, mAdapter.getItem(position));
        startActivity(mainIntent);
    }
}
