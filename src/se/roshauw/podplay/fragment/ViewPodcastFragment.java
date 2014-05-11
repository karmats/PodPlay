package se.roshauw.podplay.fragment;

import se.roshauw.podplay.MainActivity;
import se.roshauw.podplay.R;
import se.roshauw.podplay.parcel.Podcast;
import se.roshauw.podplay.parcel.PodcastTrack;
import se.roshauw.podplay.task.FetchPodcastTracksTask;
import se.roshauw.podplay.util.PodPlayUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Fragment to view a podcast and its tracks.
 * 
 * @author mats
 * 
 */
public class ViewPodcastFragment extends Fragment {

    private ArrayAdapter<PodcastTrack> mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.view_podcast, container, false);

        final Podcast podcast = getArguments().getParcelable(PodPlayUtil.EXTRA_PODCAST);
        PodPlayUtil.logInfo("Got podcast in fragment " + podcast);

        if (podcast != null) {
            TextView titleView = (TextView) layout.findViewById(R.id.viewPodcastTitle);
            titleView.setText(podcast.getTitle());

            mAdapter = new ArrayAdapter<PodcastTrack>(getActivity().getApplicationContext(),
                    R.layout.view_podcast_list_item);

            ListView trackListView = (ListView) layout.findViewById(R.id.viewPodcastTracks);
            trackListView.setAdapter(mAdapter);

            trackListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    PodcastTrack trackToPlay = mAdapter.getItem(position);
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.playPodcastTrack(podcast, trackToPlay);
                }

            });

            // Fetch the tracks
            new FetchPodcastTracksTask(getActivity().getApplicationContext(), mAdapter).execute(podcast);
        }
        return layout;
    }
}
