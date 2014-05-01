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
import android.widget.LinearLayout;
import android.widget.SearchView;

/**
 * Fragment for adding podcasts
 * 
 * @author mats
 * 
 */
public class AddPodcastFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Search options menu
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PodPlayUtil.logInfo("Creating add podcast fragment");
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.podcast_coverflow, container, false);
        createPodcastPageAdapter(null, layout, R.id.coverflowPager1);
        createPodcastPageAdapter(Podcast.Category.COMEDY, layout, R.id.coverflowPager2);
        createPodcastPageAdapter(Podcast.Category.TV_FILM, layout, R.id.coverflowPager3);
        return layout;
    }
    
    @Override
    public void onDestroyView() {
        PodPlayUtil.logInfo("Destroying view addpodcastfragment");
        super.onDestroyView();
    }
    
    @Override
    public void onDestroy() {
        PodPlayUtil.logInfo("Destroying thefragment");
        super.onDestroy();
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // TODO Save array of already fetched podcasts and get them in onCreateView
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

    // Creates a view pager and adds it to the view
    private void createPodcastPageAdapter(Podcast.Category category, View container, int viewPagerId) {
        final CoverFlowAdapter adapter = new CoverFlowAdapter(getFragmentManager(), category);
        final ViewPager pager = (ViewPager) container.findViewById(viewPagerId);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(3);

        // The margin as dip
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(-margin);

        // Fetch top podcasts
        new FetchTopPodcastsTask(getActivity().getApplicationContext(), pager).execute(category);

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

}
