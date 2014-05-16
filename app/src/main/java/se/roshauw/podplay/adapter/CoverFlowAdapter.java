package se.roshauw.podplay.adapter;

import java.util.ArrayList;

import se.roshauw.podplay.fragment.CoverFlowFragment;
import se.roshauw.podplay.parcel.Podcast;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Adapter for holding podcasts. Used to view podcasts in a cover flow carousel.
 * This adapter fools the associated ViewPager that it has more items than it
 * actually have
 * 
 * @author mats
 * 
 */
public class CoverFlowAdapter extends FragmentPagerAdapter {

    /**
     * The number of loops for the carousel to spin. This to fool the associated
     * view pager that we have more items than we actually have. We get a
     * carousel effect
     */
    public static final int CAROUSEL_LOOPS = 100;

    private ArrayList<Podcast> podcasts;

    public CoverFlowAdapter(FragmentManager fm) {
        super(fm);
        podcasts = new ArrayList<Podcast>();
    }

    public void addPodcast(Podcast entry) {
        podcasts.add(entry);
    }

    public void addPodcasts(ArrayList<Podcast> entries) {
        podcasts.addAll(entries);
    }

    public ArrayList<Podcast> getPodcasts() {
        return podcasts;
    }

    public Podcast getPodcast(int position) {
        // Rest of index / size will get us the correct podcast in the list
        return podcasts.get(position % podcasts.size());
    }

    @Override
    public Fragment getItem(int index) {
        // Rest of index / size will get us the correct item in the list
        return CoverFlowFragment.create(podcasts.get(index % podcasts.size()));
    }

    @Override
    public int getCount() {
        // Times the size with CAROUSE_LOOPS, i.e. the number of spins we can do
        // with the view pager
        return podcasts.size() * CAROUSEL_LOOPS;
    }

}
