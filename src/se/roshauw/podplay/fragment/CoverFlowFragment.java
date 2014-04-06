package se.roshauw.podplay.fragment;

import se.roshauw.podplay.R;
import se.roshauw.podplay.activity.ViewPodcastActivity;
import se.roshauw.podplay.parcel.Podcast;
import se.roshauw.podplay.task.DownloadImageTask;
import se.roshauw.podplay.util.PodPlayUtil;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Page fragment for displaying podcasts in cover flow carousel.
 * 
 * @author mats
 * 
 */
public class CoverFlowFragment extends Fragment {

    private static String ARG_PODCAST = "podcast";

    /**
     * Factory method to create new view
     * 
     * @param podcast
     *            The {@link Podcast} to create view for
     * @return {@link CoverFlowFragment}
     */
    public static CoverFlowFragment create(Podcast podcast) {
        CoverFlowFragment fragment = new CoverFlowFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PODCAST, podcast);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.podcast_coverflow_item_view, container, false);

        final Podcast podcast = getArguments().getParcelable(ARG_PODCAST);
        rootView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PodPlayUtil.logInfo("Clicking view");
                Intent viewPodcastIntent = new Intent(getActivity().getApplicationContext(), ViewPodcastActivity.class);
                viewPodcastIntent.putExtra(PodPlayUtil.EXTRA_PODCAST, podcast);
                startActivity(viewPodcastIntent);
            }
        });

        TextView tv = (TextView) rootView.findViewById(R.id.podcastTitle);
        tv.setText(podcast.getTitle());
        ImageView iv = (ImageView) rootView.findViewById(R.id.podcastImage);
        new DownloadImageTask(iv).execute(podcast.getImgUrl());
        return rootView;
    }

}
