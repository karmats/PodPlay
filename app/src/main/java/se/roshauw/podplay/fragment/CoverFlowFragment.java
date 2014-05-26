package se.roshauw.podplay.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import se.roshauw.podplay.R;
import se.roshauw.podplay.parcel.Podcast;
import se.roshauw.podplay.task.DownloadImageTask;

/**
 * Page fragment for displaying podcasts in cover flow carousel.
 *
 * @author mats
 */
public class CoverFlowFragment extends Fragment {

    private static String ARG_PODCAST = "podcast";

    // The image view for the image to show
    private ImageView mImageView;
    // The podcast associated with this fragment
    private Podcast mPodcast;
    // The download image task that starts in onResume, and stops in onPause
    private DownloadImageTask mDownloadImageTask;

    /**
     * Factory method to create new view
     *
     * @param podcast The {@link Podcast} to create view for
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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.podcast_coverflow_item_view, container, false);

        mPodcast = getArguments().getParcelable(ARG_PODCAST);

        TextView tv = (TextView) rootView.findViewById(R.id.podcast_title);
        tv.setText(mPodcast.getTitle());
        mImageView = (ImageView) rootView.findViewById(R.id.podcast_image);

        rootView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // New view podcast fragment, with the selected podcast as
                // argument
                ViewPodcastFragment viewPodcastFragment = ViewPodcastFragment.create(mPodcast);
                // Get the parent fragments fragmentmanager since that has the
                // fragment_container
                getParentFragment().getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, viewPodcastFragment).addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            }
        });

        return rootView;
    }

    @Override
    public void onPause() {
        // If the mDowloadImageTask is running, cancel it so it doesn't do an
        // unnecessarily download
        if (null != mDownloadImageTask && !mDownloadImageTask.isCancelled()) {
            mDownloadImageTask.cancel(true);
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        // Start the mDownloadImageTask
        if (null != mImageView && null != mPodcast) {
            mDownloadImageTask = new DownloadImageTask(mImageView);
            mDownloadImageTask.execute(mPodcast.getImgUrl());
        }
        super.onResume();
    }

}
