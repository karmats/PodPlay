package se.roshauw.podplay.fragment;

import se.roshauw.podplay.R;
import se.roshauw.podplay.activity.SearchPodcastsActivity;
import se.roshauw.podplay.adapter.CoverFlowAdapter;
import se.roshauw.podplay.parcel.Podcast;
import se.roshauw.podplay.task.FetchTopPodcastsTask;
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
import android.widget.LinearLayout;
import android.widget.SearchView;

/**
 * Fragment for adding podcasts
 * 
 * @author mats
 * 
 */
public class AddPodcastFragment extends Fragment {

    CoverFlowAdapter mCoverFlowAdapter1;
    CoverFlowAdapter mCoverFlowAdapter2;
    CoverFlowAdapter mCoverFlowAdapter3;

    private LinearLayout mMainLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainLayout = (LinearLayout) inflater.inflate(R.layout.podcast_coverflow, container, false);
        mCoverFlowAdapter1 = createPodcastPageAdapter(null, R.id.coverflowPager1);
        mCoverFlowAdapter2 = createPodcastPageAdapter(Podcast.Category.COMEDY, R.id.coverflowPager2);
        mCoverFlowAdapter3 = createPodcastPageAdapter(Podcast.Category.TV_FILM, R.id.coverflowPager3);
        return mMainLayout;
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

    private CoverFlowAdapter createPodcastPageAdapter(Podcast.Category category, int viewPagerId) {
        final CoverFlowAdapter result = new CoverFlowAdapter(getFragmentManager());
        final ViewPager pager = (ViewPager) mMainLayout.findViewById(viewPagerId);
        pager.setAdapter(result);
        pager.setOffscreenPageLimit(3);

        // The margin as dip
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(-margin);

        // Fetch top podcasts
        new FetchTopPodcastsTask(getActivity().getApplicationContext(), result).execute(category);

        pager.setPageTransformer(false, new ViewPager.PageTransformer() {

            @Override
            public void transformPage(View page, float position) {
                page.setRotationY(position * -50);
                float scale = position < 0 ? position * -1 : position;
                page.setScaleX(1.2f - scale);
                page.setScaleY(1.2f - scale);
            }
        });

        return result;
    }

}
