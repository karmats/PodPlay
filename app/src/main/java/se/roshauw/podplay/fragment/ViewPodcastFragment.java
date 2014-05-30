package se.roshauw.podplay.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import se.roshauw.podplay.MainActivity;
import se.roshauw.podplay.R;
import se.roshauw.podplay.database.DatabaseHelper;
import se.roshauw.podplay.parcel.Podcast;
import se.roshauw.podplay.parcel.PodcastTrack;
import se.roshauw.podplay.task.FetchPodcastTracksTask;
import se.roshauw.podplay.util.PodPlayUtil;

/**
 * Fragment to view a podcast and its tracks.
 *
 * @author mats
 */
public class ViewPodcastFragment extends Fragment {

    private static String ARG_PODCAST = "podcast";

    // The podcast to view
    private Podcast mPodcast;
    private ArrayAdapter<PodcastTrack> mAdapter;

    /**
     * Creates a new instance of ViewPodcastFragment.
     *
     * @param podcast The podcast to view
     * @return ViewPodcastFragment
     */
    public static ViewPodcastFragment create(Podcast podcast) {
        ViewPodcastFragment fragment = new ViewPodcastFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PODCAST, podcast);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.view_podcast, container, false);

        mPodcast = getArguments().getParcelable(ARG_PODCAST);
        PodPlayUtil.logInfo("Got podcast in fragment " + mPodcast);

        if (null != mPodcast) {
            TextView titleView = (TextView) layout.findViewById(R.id.viewPodcastTitle);
            titleView.setText(mPodcast.getTitle());

            mAdapter = new ArrayAdapter<PodcastTrack>(getActivity().getApplicationContext(),
                    R.layout.view_podcast_list_item);

            ListView trackListView = (ListView) layout.findViewById(R.id.viewPodcastTracks);
            trackListView.setAdapter(mAdapter);

            trackListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    // Start playing the selected track
                    PodcastTrack trackToPlay = mAdapter.getItem(position);
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.playPodcastTrack(mPodcast, trackToPlay);
                }

            });

            // Fetch the tracks
            new FetchPodcastTracksTask(getActivity().getApplicationContext(), mAdapter).execute(mPodcast);
        }
        return layout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the options menu from XML
        inflater.inflate(R.menu.view_podcast_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_subscribe:
                // If the podcast is subscribed, we unsubscribe to it
                if (mPodcast.getDbId() >= 0 && unsubscribeToPodcast()) {
                    PodPlayUtil.logDebug("Unsubscribed to podcast");
                } else {
                    long dbId = subscribeToPodcast();
                    mPodcast.setDbId(dbId);
                    PodPlayUtil.logDebug("Subscribing to podcast, got db id " + dbId);
                }
                return true;
            default:
                PodPlayUtil.logInfo(item.getTitle() + " is not a menu");
                return false;
        }
    }

    // Stores the current podcast to the database. Returns the row generated id
    private long subscribeToPodcast() {
        SQLiteDatabase db = new DatabaseHelper(getActivity().getApplicationContext()).getWritableDatabase();
        long id = db.insert(DatabaseHelper.TABLE_NAME, null, mPodcast.toContentValues());
        db.close();
        return id;
    }

    // Unseubscribe to the current podcast. Deletes all data from the database.
    private boolean unsubscribeToPodcast() {
        SQLiteDatabase db = new DatabaseHelper(getActivity().getApplicationContext()).getWritableDatabase();
        long result = db.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(mPodcast.getDbId())});
        db.close();
        return result > 0;
    }
}
