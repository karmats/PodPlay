package se.roshauw.podplay.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import se.roshauw.podplay.R;
import se.roshauw.podplay.parcel.Podcast;
import se.roshauw.podplay.task.DownloadImageTask;

/**
 * Adapter for holding subscribed podcasts.
 *
 * @author mats
 */
public class ImagePodcastAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Podcast> mPodcasts;

    public ImagePodcastAdapter(Context c) {
        mContext = c;
        mPodcasts = new ArrayList<Podcast>();
    }

    public void addPodcast(Podcast item) {
        mPodcasts.add(item);
    }

    @Override
    public int getCount() {
        // All podcasts + add new podcast image
        return mPodcasts.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return mPodcasts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        boolean lastElement = position == getCount() - 1;
        // if it's not recycled, initialize some
        // attributes
        if (null == convertView) {
            // Add new podcast as last element
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (lastElement) {
                convertView = inflater.inflate(R.layout.add_podcast_item, parent, false);
            } else {
                // Inflate the text and image view
                convertView = inflater.inflate(R.layout.subscribed_podcast_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.textView = (TextView) convertView.findViewById(R.id.podcast_title);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.podcast_image);
                convertView.setTag(viewHolder);
            }
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (!lastElement && null != viewHolder) {
            // Text and image
            Podcast podcast = mPodcasts.get(position);
            viewHolder.textView.setText(podcast.getTitle());
            new DownloadImageTask(viewHolder.imageView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, podcast.getImgUrl());
        }

        return convertView;
    }

    // View holder
    static class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

}
