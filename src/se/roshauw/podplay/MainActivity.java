package se.roshauw.podplay;

import se.roshauw.podplay.fragment.SubscribedFragment;
import se.roshauw.podplay.fragment.ViewPodcastFragment;
import se.roshauw.podplay.parcel.Podcast;
import se.roshauw.podplay.util.PodPlayUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

/**
 * Main activity for the application. All fragments will be created from here
 * 
 * @author mats
 * 
 */
public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Podcast podcastExtra = null;
        if (null != getIntent() && null != getIntent().getExtras()) {
            podcastExtra = getIntent().getExtras().getParcelable(PodPlayUtil.EXTRA_PODCAST);
        }
        PodPlayUtil.logInfo("Got podcast " + podcastExtra + " as extra");
        if (null == podcastExtra) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new SubscribedFragment())
                    .commit();
        } else {
            Bundle bundle = new Bundle();
            bundle.putParcelable(PodPlayUtil.EXTRA_PODCAST, podcastExtra);
            ViewPodcastFragment viewPodcastFragment = new ViewPodcastFragment();
            viewPodcastFragment.setArguments(bundle);

            FragmentManager manager = getSupportFragmentManager();
            Fragment coverFlowFragment = manager.findFragmentByTag(PodPlayUtil.TAG_COVERFLOW_FRAGMENT);
            FragmentTransaction transaction = manager.beginTransaction();
            if (null != coverFlowFragment) {
                transaction.remove(coverFlowFragment);
            }
            transaction.add(R.id.fragment_container, viewPodcastFragment).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
