package se.roshauw.podplay.adapter;

import java.util.ArrayList;

import se.roshauw.podplay.fragment.CoverFlowFragment;
import se.roshauw.podplay.parcel.Podcast;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Adapter for holding podcasts. Used to view podcasts in a cover flow carousel
 * 
 * @author mats
 * 
 */
public class CoverFlowAdapter extends FragmentPagerAdapter {

    private ArrayList<Podcast> podcasts;

    public CoverFlowAdapter(FragmentManager fm) {
        super(fm);
        podcasts = new ArrayList<Podcast>();
    }

    public void addPodcast(Podcast entry) {
        podcasts.add(entry);
    }

    public Podcast getPodcast(int position) {
        return podcasts.get(position);
    }

    @Override
    public Fragment getItem(int index) {
        return CoverFlowFragment.create(podcasts.get(index));
    }

    @Override
    public int getCount() {
        return podcasts.size();
    }

}
