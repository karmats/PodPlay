package se.roshauw.podplay.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import se.roshauw.podplay.R;
import se.roshauw.podplay.parcel.PodcastTrack;

/**
 * Adapter for displaying podcast tracks
 */
public class ViewTracksAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<PodcastTrack> mTracks;

    public ViewTracksAdapter(Context c) {
        this.mContext = c;
        mTracks = new ArrayList<PodcastTrack>();
    }

    @Override
    public int getCount() {
        return mTracks.size();
    }

    @Override
    public Object getItem(int i) {
        return mTracks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void addTracks(ArrayList<PodcastTrack> tracks) {
        mTracks.addAll(tracks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.view_podcast_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.titleText = (TextView) convertView.findViewById(R.id.view_podcast_item_title);
            viewHolder.descriptionText = (TextView) convertView.findViewById(R.id.view_podcast_item_description);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PodcastTrack podcastTrack = mTracks.get(position);
        viewHolder.titleText.setText(podcastTrack.getTitle());
        // The description is gotten as html, The Html.fromHtml renders it correctly
        if (null != podcastTrack.getDescription()) {
            viewHolder.descriptionText.setText(Html.fromHtml(podcastTrack.getDescription()));
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView titleText;
        private TextView descriptionText;
    }
}
