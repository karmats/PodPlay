package se.roshauw.podplay;

import se.roshauw.podplay.activity.AddPodcastActivity;
import se.roshauw.podplay.adapter.ImagePodcastAdapter;
import se.roshauw.podplay.parcel.Podcast;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

/**
 * Main activity for the application. Here all subscribed podcasts will be
 * displayed as images
 * 
 * @author mats
 * 
 */
public class MainActivity extends Activity {

    /**
     * ImageAdapter for the image GridView
     */
    private ImagePodcastAdapter mImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        GridView gridView = (GridView) findViewById(R.id.gridview);
        mImageAdapter = new ImagePodcastAdapter(this);
        gridView.setAdapter(mImageAdapter);
        Podcast testPod = new Podcast(0L, "VŠrvet");
        testPod.setImgUrl("http://a1059.phobos.apple.com/us/r30/Podcasts6/v4/1e/fa/69/1efa6962-c51d-398d-fb81-52d4f13b1dea/mza_2906164569851893566.170x170-75.jpg");
        mImageAdapter.addPodcast(testPod);

        gridView.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                // Check if user clicked the add podcast view
                if (v.getId() == R.id.addNewPodcastView) {
                    Intent addNewIntent = new Intent(MainActivity.this, AddPodcastActivity.class);
                    startActivity(addNewIntent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
