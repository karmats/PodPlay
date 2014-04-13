package se.roshauw.podplay;

import se.roshauw.podplay.fragment.SubscribedFragment;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;

/**
 * Main activity for the application. All fragments will be created from here
 * 
 * @author mats
 * 
 */
public class MainActivity extends Activity {

    private SubscribedFragment mSubscribedFragment = new SubscribedFragment();

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mFragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, mSubscribedFragment);
        fragmentTransaction.commit();
        mFragmentManager.executePendingTransactions();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
