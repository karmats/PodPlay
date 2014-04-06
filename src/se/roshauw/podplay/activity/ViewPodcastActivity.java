package se.roshauw.podplay.activity;

import se.roshauw.podplay.R;
import se.roshauw.podplay.parcel.Podcast;
import se.roshauw.podplay.parcel.PodcastTrack;
import se.roshauw.podplay.task.FetchPodcastTracksTask;
import se.roshauw.podplay.util.PodPlayUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Activity to view a podcast and its tracks.
 * 
 * @author mats
 * 
 */
public class ViewPodcastActivity extends Activity {

    private ArrayAdapter<PodcastTrack> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_podcast);

        Podcast podcast = getIntent().getExtras().getParcelable(PodPlayUtil.EXTRA_PODCAST);

        if (podcast != null) {
            TextView titleView = (TextView) findViewById(R.id.viewPodcastTitle);
            titleView.setText(podcast.getTitle());

            mAdapter = new ArrayAdapter<PodcastTrack>(ViewPodcastActivity.this, android.R.layout.simple_list_item_1);

            ListView trackListView = (ListView) findViewById(R.id.viewPodcastTracks);
            trackListView.setAdapter(mAdapter);

            trackListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    PodcastTrack trackToPlay = mAdapter.getItem(position);
                    Intent playIntent = new Intent(ViewPodcastActivity.this, PlayPodcastActivity.class);
                    playIntent.putExtra(PodPlayUtil.EXTRA_PODCAST_TRACK, trackToPlay);
                    startActivity(playIntent);
                }

            });

            // Fetch the tracks
            new FetchPodcastTracksTask(ViewPodcastActivity.this, mAdapter).execute(podcast);
        }
    }
}
