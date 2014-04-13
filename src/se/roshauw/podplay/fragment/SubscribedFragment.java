package se.roshauw.podplay.fragment;

import se.roshauw.podplay.R;
import se.roshauw.podplay.activity.AddPodcastActivity;
import se.roshauw.podplay.adapter.ImagePodcastAdapter;
import se.roshauw.podplay.parcel.Podcast;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/**
 * Fragment to show subscribed podcasts. All subscribed podcasts are show in a
 * gridview
 * 
 * @author mats
 * 
 */
public class SubscribedFragment extends Fragment {

    /**
     * ImageAdapter for the image GridView
     */
    private ImagePodcastAdapter mImageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Gridview for subscribed podcasts
        GridView gridView = (GridView) inflater.inflate(R.layout.subscribed, container, false);
        mImageAdapter = new ImagePodcastAdapter(getActivity().getApplicationContext());
        gridView.setAdapter(mImageAdapter);
        Podcast testPod = new Podcast(0L, "V�rvet");
        testPod.setImgUrl("http://a1059.phobos.apple.com/us/r30/Podcasts6/v4/1e/fa/69/1efa6962-c51d-398d-fb81-52d4f13b1dea/mza_2906164569851893566.170x170-75.jpg");
        mImageAdapter.addPodcast(testPod);

        gridView.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // Check if user clicked the add podcast view
                if (v.getId() == R.id.addNewPodcastView) {
                    Intent addNewIntent = new Intent(getActivity().getApplicationContext(), AddPodcastActivity.class);
                    startActivity(addNewIntent);
                }
            }
        });

        return gridView;
    }
}
