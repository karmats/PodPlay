package se.roshauw.podplay.activity;

import java.util.ArrayList;
import java.util.List;

import se.roshauw.podplay.R;
import se.roshauw.podplay.adapter.CoverFlowAdapter;
import se.roshauw.podplay.parcel.Podcast;
import se.roshauw.podplay.task.FetchTopPodcastsTask;
import se.roshauw.podplay.util.PodPlayUtil;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

/**
 * Activity for adding a new podcast. Searchbar and toplists are displayed here
 * 
 * @author mats
 * 
 */
public class AddPodcastActivity extends FragmentActivity {

    CoverFlowAdapter mCoverFlowAdapter1;
    CoverFlowAdapter mCoverFlowAdapter2;
    CoverFlowAdapter mCoverFlowAdapter3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.podcast_coverflow);
        setTitle("Add new podcast");

        mCoverFlowAdapter1 = createPodcastPageAdapter(null, R.id.coverflowPager1);
        mCoverFlowAdapter2 = createPodcastPageAdapter(Podcast.Category.COMEDY, R.id.coverflowPager2);
        mCoverFlowAdapter3 = createPodcastPageAdapter(Podcast.Category.TV_FILM, R.id.coverflowPager3);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            PodPlayUtil.logInfo("Searching in onCreate for " + query);
            TextView tv = (TextView) findViewById(R.id.queryText);
            tv.setText(query);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            PodPlayUtil.logInfo("Searching for in onNewIntent for  " + query);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        getMenuInflater().inflate(R.menu.add_podcast_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // Our search activity is the SearchPodcastActivity
        SearchableInfo info = searchManager.getSearchableInfo(new ComponentName(getApplicationContext(),
                SearchPodcastsActivity.class));
        searchView.setSearchableInfo(info);

        return true;
    }

    private CoverFlowAdapter createPodcastPageAdapter(Podcast.Category category, int viewPagerId) {
        final CoverFlowAdapter result = new CoverFlowAdapter(getSupportFragmentManager());
        final ViewPager pager = (ViewPager) findViewById(viewPagerId);
        pager.setAdapter(result);
        pager.setOffscreenPageLimit(3);

        // The margin as dip
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(-margin);

        /*
         * List<Podcast> podcasts = createPodcasts(); for (Podcast p : podcasts)
         * { result.addPodcast(p); } result.notifyDataSetChanged();
         */

        // Fetch top podcasts
        new FetchTopPodcastsTask(getApplicationContext(), result).execute(category);

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

    private List<Podcast> createPodcasts() {
        List<Podcast> result = new ArrayList<Podcast>();

        Podcast p1 = new Podcast(1L, "Institutet");
        p1.setImgUrl("http://a1985.phobos.apple.com/us/r30/Podcasts/v4/62/fe/70/62fe70ac-24ac-e32e-afa1-e918b2635c96/mza_254300554511776881.170x170-75.jpg");
        result.add(p1);

        Podcast p2 = new Podcast(2L, "Genier");
        p2.setImgUrl("http://a725.phobos.apple.com/us/r30/Podcasts/v4/48/6d/d9/486dd981-4147-ce9d-02c8-5a2ab1e0457d/mza_6114829249998854242.170x170-75.jpg");
        result.add(p2);

        Podcast p3 = new Podcast(3L, "Fantasipanelen");
        p3.setImgUrl("http://a1269.phobos.apple.com/us/r30/Podcasts6/v4/db/33/2d/db332d80-d439-cccb-ee47-0680ee9f0c1f/mza_2025878838078251253.170x170-75.jpg");
        result.add(p3);

        Podcast p4 = new Podcast(4L, "51% Fotboll");
        p4.setImgUrl("http://a176.phobos.apple.com/us/r30/Podcasts/v4/9c/14/3a/9c143aa4-e91f-4a94-5682-d355c25ba2cc/mza_3244111744190541961.170x170-75.jpg");
        result.add(p4);

        return result;
    }
}
