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

import static se.roshauw.podplay.parcel.Podcast.Category;

/**
 * Adapter for displaying Podcast search results
 */
public class SearchResultAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Podcast> mPodcasts;

    public SearchResultAdapter(Context c) {
        this.mContext = c;
        this.mPodcasts = new ArrayList<Podcast>();
    }

    public void addPodcasts(ArrayList<Podcast> podcasts) {
        mPodcasts.addAll(podcasts);
    }

    @Override
    public int getCount() {
        return mPodcasts.size();
    }

    @Override
    public Object getItem(int i) {
        return mPodcasts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.search_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.search_item_thumbnail);
            viewHolder.titleText = (TextView) convertView.findViewById(R.id.search_item_title);
            viewHolder.categoryText = (TextView) convertView.findViewById(R.id.search_item_category);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Podcast podcast = mPodcasts.get(position);
        viewHolder.titleText.setText(podcast.getTitle());
        // The first category is enough
        Category category = Category.getCategoryForItunesId(
                podcast.getCategoryIds().get(0));
        if (Category.UNKNOWN != category) {
            viewHolder.categoryText.setText(mContext.getString(category.getStringId()));
        }
        new DownloadImageTask(viewHolder.imageView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, podcast.getImgUrl());
        return convertView;
    }

    static class ViewHolder {
        private ImageView imageView;
        private TextView titleText;
        private TextView categoryText;
    }
}
