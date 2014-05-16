package se.roshauw.podplay.adapter;

import java.util.ArrayList;

import se.roshauw.podplay.R;
import se.roshauw.podplay.parcel.Podcast;
import se.roshauw.podplay.task.DownloadImageTask;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapter for holding subscribed podcasts.
 * 
 * @author mats
 * 
 */
public class ImagePodcastAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Podcast> podcasts;

    public ImagePodcastAdapter(Context c) {
        mContext = c;
        podcasts = new ArrayList<Podcast>();
    }

    public void addPodcast(Podcast item) {
        podcasts.add(item);
    }

    @Override
    public int getCount() {
        // All podcasts + add new podcast image
        return podcasts.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return podcasts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FrameLayout frameLayout;
        // if it's not recycled, initialize some
        // attributes
        if (convertView == null) {
            // Add new podcast as last elemen
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (position == getCount() - 1) {
                frameLayout = (FrameLayout) inflater.inflate(R.layout.add_podcast, null);
            } else {
                // Add the podcast images and titles
                Podcast podcast = podcasts.get(position);
                frameLayout = (FrameLayout) inflater.inflate(R.layout.subscribed_podcast_item, null);

                TextView tv = (TextView) frameLayout.findViewById(R.id.podcast_title);
                tv.setText(podcast.getTitle());

                ImageView imageView = (ImageView) frameLayout.findViewById(R.id.podcast_image);
                new DownloadImageTask(imageView).execute(podcast.getImgUrl());

            }
        } else {
            frameLayout = (FrameLayout) convertView;
        }

        return frameLayout;
    }

    // TODO Remove this and replace with xml-layout-file
    public FrameLayout createImageLayout(Context c, Podcast entry) {

        FrameLayout frameLayout = new FrameLayout(c);
        frameLayout.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT,
                GridView.LayoutParams.MATCH_PARENT));

        ImageView imageView = new ImageView(c);
        imageView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageResource(R.drawable.filipofredrik);
        //new DownloadImageTask(imageView).execute(entry.getImgUrl());

        // Add to the view
        frameLayout.addView(imageView);

        TextView tv = new TextView(c);
        tv.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM));
        tv.setGravity(Gravity.BOTTOM);
        tv.setText(entry.getTitle());
        tv.setPadding(20, 20, 0, 20);
        tv.setBackgroundColor(Color.BLACK);
        tv.setAlpha(0.8f);
        tv.setTextColor(Color.WHITE);

        // Add text view
        frameLayout.addView(tv);
        return frameLayout;
    }

}
