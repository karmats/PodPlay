package se.roshauw.podplay.adapter;

import java.util.ArrayList;

import se.roshauw.podplay.R;
import se.roshauw.podplay.parcel.Podcast;
import se.roshauw.podplay.task.DownloadImageTask;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
            // Add new podcast as last element
            if (position == getCount() - 1) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                frameLayout = (FrameLayout) inflater.inflate(R.layout.add_podcast, null);
                frameLayout.setLayoutParams(new AbsListView.LayoutParams(350, 350));
            } else {
                // Add the podcast images
                frameLayout = createImageLayout(mContext, podcasts.get(position));
            }
        } else {
            frameLayout = (FrameLayout) convertView;
        }
        Point size = new Point();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getSize(size);
        int screenWidth = size.x;

        frameLayout
                .setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, screenWidth / 2));

        return frameLayout;
    }

    public FrameLayout createImageLayout(Context c, Podcast entry) {

        FrameLayout frameLayout = new FrameLayout(c);
        frameLayout.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT,
                GridView.LayoutParams.MATCH_PARENT));

        ImageView imageView = new ImageView(c);
        imageView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        new DownloadImageTask(imageView).execute(entry.getImgUrl());

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