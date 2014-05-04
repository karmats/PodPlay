package se.roshauw.podplay.fragment;

import se.roshauw.podplay.R;
import se.roshauw.podplay.activity.SearchPodcastsActivity;
import se.roshauw.podplay.adapter.CoverFlowAdapter;
import se.roshauw.podplay.parcel.Podcast;
import se.roshauw.podplay.task.FetchTopPodcastsTask;
import se.roshauw.podplay.util.PodPlayUtil;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

/**
 * Fragment for adding podcasts
 * 
 * @author mats
 * 
 */
public class AddPodcastFragment extends Fragment {

    private CoverFlowAdapter mToplistAdapter;
    private CoverFlowAdapter mRecommendedAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Search options menu
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PodPlayUtil.logInfo("onCreateView");
        View layout = inflater.inflate(R.layout.podcast_coverflow, container, false);

        // Toplist podcasts
        final ViewPager toplistPager = (ViewPager) layout.findViewById(R.id.toplist_coverflow);
        if (null == mToplistAdapter) {
            mToplistAdapter = createPodcastPageAdapter(null, toplistPager);
        }
        setupViewPager(toplistPager, mToplistAdapter);

        // Recommended podcasts
        final ViewPager recommendedPager = (ViewPager) layout.findViewById(R.id.recommended_coverflow);
        if (null == mRecommendedAdapter) {
            mRecommendedAdapter = createPodcastPageAdapter(Podcast.Category.COMEDY, recommendedPager);
        }
        setupViewPager(recommendedPager, mRecommendedAdapter);
        return layout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the options menu from XML
        inflater.inflate(R.menu.add_podcast_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        // Our search activity is the SearchPodcastActivity
        SearchableInfo info = searchManager.getSearchableInfo(new ComponentName(getActivity().getApplicationContext(),
                SearchPodcastsActivity.class));
        searchView.setSearchableInfo(info);

        super.onCreateOptionsMenu(menu, inflater);

    }

    private void setupViewPager(final ViewPager pager, final CoverFlowAdapter adapter) {
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(3);

        // The margin as dip
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(-margin);

        // Transform page so it looks like a cover flow
        pager.setPageTransformer(false, new ViewPager.PageTransformer() {

            @Override
            public void transformPage(View page, float position) {
                page.setRotationY(position * -50);
                float scale = position < 0 ? position * -1 : position;
                page.setScaleX(1.2f - scale);
                page.setScaleY(1.2f - scale);
            }
        });

    }

    // Creates a view pager and adds it to the view
    private CoverFlowAdapter createPodcastPageAdapter(Podcast.Category category, final ViewPager pager) {
        // The childs fragment manager is used because the view pager is inside
        // a fragment.
        final CoverFlowAdapter adapter = new CoverFlowAdapter(getChildFragmentManager());

        // Fetch top podcasts
        new FetchTopPodcastsTask(getActivity().getApplicationContext(), pager).execute(category);

        return adapter;
    }

}
