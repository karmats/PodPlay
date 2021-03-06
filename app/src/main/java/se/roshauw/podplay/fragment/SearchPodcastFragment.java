package se.roshauw.podplay.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import se.roshauw.podplay.R;
import se.roshauw.podplay.adapter.SearchResultAdapter;
import se.roshauw.podplay.parcel.Podcast;
import se.roshauw.podplay.task.SearchPodcastTask;
import se.roshauw.podplay.util.PodPlayUtil;

/**
 * Fragment to search for podcasts and present it in a list
 */
public class SearchPodcastFragment extends ListFragment {

    private static final String ARG_QUERY = "query";

    private SearchResultAdapter mAdapter;

    /**
     * Creates a new instance of the fragment.
     *
     * @param query The query to search for
     * @return SearchPodcastFragment
     */
    public static SearchPodcastFragment create(String query) {
        SearchPodcastFragment fragment = new SearchPodcastFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUERY, query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_results, container, false);
        mAdapter = new SearchResultAdapter(getActivity().getApplicationContext());
        setListAdapter(mAdapter);
        String query = getArguments().getString(ARG_QUERY);
        PodPlayUtil.logInfo("In searchfragment and searching for " + query);
        new SearchPodcastTask(getActivity().getApplicationContext(), mAdapter).execute(query);
        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Open the view podcast framgent
        Podcast podcast = (Podcast) mAdapter.getItem(position);
        ViewPodcastFragment viewPodcastFragment = ViewPodcastFragment.create(podcast);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, viewPodcastFragment)
                .addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

}
