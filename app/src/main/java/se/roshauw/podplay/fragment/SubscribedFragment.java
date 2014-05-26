package se.roshauw.podplay.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import java.util.ArrayList;

import se.roshauw.podplay.R;
import se.roshauw.podplay.adapter.ImagePodcastAdapter;
import se.roshauw.podplay.database.DatabaseHelper;
import se.roshauw.podplay.parcel.Podcast;

/**
 * Fragment to show subscribed podcasts. All subscribed podcasts are show in a
 * gridview
 *
 * @author mats
 */
public class SubscribedFragment extends Fragment {

    /**
     * Creates a new instance of SubscribedFragment.
     *
     * @return SubscribedFragment
     */
    public static SubscribedFragment create() {
        SubscribedFragment fragment = new SubscribedFragment();
        return fragment;
    }

    /**
     * ImageAdapter for the image GridView
     */
    private ImagePodcastAdapter mImageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Gridview for subscribed podcasts
        GridView layout = (GridView) inflater.inflate(R.layout.subscribed, container, false);
        mImageAdapter = new ImagePodcastAdapter(getActivity().getApplicationContext());
        layout.setAdapter(mImageAdapter);
        ArrayList<Podcast> subscribedPodcasts = getPodcastsFromDB();
        for (Podcast p : subscribedPodcasts) {
            mImageAdapter.addPodcast(p);
        }

        layout.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // Check if user clicked the add podcast view
                if (v.getId() == R.id.add_new_podcast_view) {
                    AddPodcastFragment addPodcastFragment = AddPodcastFragment.create();
                    FragmentManager manager = getFragmentManager();
                    manager.beginTransaction().replace(R.id.fragment_container, addPodcastFragment)
                            .addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                } else {
                    // Open the view podcast fragment for the selected podcast
                    Podcast podcast = (Podcast) mImageAdapter.getItem(position);
                    ViewPodcastFragment viewPodcastFragment = ViewPodcastFragment.create(podcast);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, viewPodcastFragment)
                            .addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                }
            }
        });

        return layout;
    }

    private ArrayList<Podcast> getPodcastsFromDB() {
        ArrayList<Podcast> result = new ArrayList<Podcast>();
        SQLiteDatabase db = new DatabaseHelper(getActivity().getApplicationContext()).getReadableDatabase();
        // Read all stored podcasts
        Cursor c = db.query(DatabaseHelper.TABLE_NAME,
                DatabaseHelper.columns, null, new String[]{}, null, null,
                null);
        while (c.moveToNext()) {
            Podcast p = new Podcast(c.getLong(c.getColumnIndex(DatabaseHelper.COLUMN_ITUNES_ID)),
                    c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_TITLE)));
            p.setDbId(c.getLong(c.getColumnIndex(DatabaseHelper.COLUMN_ID)));
            p.setFeedUrl(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_FEED_URL)));
            p.setImgUrl(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_IMAGE_REF)));
            result.add(p);
        }
        db.close();
        return result;
    }
}
