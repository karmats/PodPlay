package se.roshauw.podplay.fragment;

import se.roshauw.podplay.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment for adding podcasts
 * 
 * @author mats
 * 
 */
public class AddPodcastFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_podcast, container, false);
    }
}
