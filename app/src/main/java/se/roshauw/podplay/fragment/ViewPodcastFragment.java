package se.roshauw.podplay.fragment;

import android.app.ActionBar;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;

import se.roshauw.podplay.MainActivity;
import se.roshauw.podplay.R;
import se.roshauw.podplay.adapter.ViewTracksAdapter;
import se.roshauw.podplay.database.DatabaseHelper;
import se.roshauw.podplay.parcel.Podcast;
import se.roshauw.podplay.parcel.PodcastTrack;
import se.roshauw.podplay.task.DownloadImageTask;
import se.roshauw.podplay.task.FetchPodcastTracksTask;
import se.roshauw.podplay.util.PodPlayUtil;

/**
 * Fragment to view a podcast and its tracks.
 *
 * @author mats
 */
public class ViewPodcastFragment extends ListFragment {

    private static String ARG_PODCAST = "podcast";

    // The podcast to view
    private Podcast mPodcast;
    private ViewTracksAdapter mAdapter;

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
        View layout = inflater.inflate(R.layout.view_podcast, container, false);

        mPodcast = getArguments().getParcelable(ARG_PODCAST);
        PodPlayUtil.logInfo("Got podcast in fragment " + mPodcast);

        if (null != mPodcast) {
            // Set action bar title to be the podcast title
            ActionBar actionBar = getActivity().getActionBar();
            if (null != actionBar) {
                actionBar.setTitle(mPodcast.getTitle());
            }

            mAdapter = new ViewTracksAdapter(getActivity().getApplicationContext());
        }
        return layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Add header and set the adapter
        View headerView = getLayoutInflater(savedInstanceState).inflate(R.layout.view_podcast_header, getListView(), false);
        final TextView descriptionText = (TextView) headerView.findViewById(R.id.view_podcast_description);
        descriptionText.setText(mPodcast.getDescription());
        final ImageView toggleImg = (ImageView) headerView.findViewById(R.id.view_podcast_toggle_description);
        KenBurnsView coverImage = (KenBurnsView) headerView.findViewById(R.id.view_podcast_cover);
        coverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (descriptionText.getVisibility() == View.GONE) {
                    toggleImg.setImageResource(R.drawable.ic_action_collapse);
                    descriptionText.setVisibility(View.VISIBLE);
                } else {
                    descriptionText.setVisibility(View.GONE);
                    toggleImg.setImageResource(R.drawable.ic_action_expand);
                }
            }
        });
        new DownloadImageTask(coverImage).execute(mPodcast.getImgUrl());
        // Fetch the tracks
        new FetchPodcastTracksTask(getActivity().getApplicationContext(), mAdapter, descriptionText).execute(mPodcast);

        getListView().addHeaderView(headerView, null, false);
        // Important that the list adapter is set AFTER we added the header see
        // http://developer.android.com/reference/android/widget/ListView.html#addHeaderView(android.view.View)
        setListAdapter(mAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Start playing the selected track
        // The header has position 0, so we need to decrease position by 1
        PodcastTrack trackToPlay = (PodcastTrack) mAdapter.getItem(position - 1);
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.playPodcastTrack(mPodcast, trackToPlay);
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
