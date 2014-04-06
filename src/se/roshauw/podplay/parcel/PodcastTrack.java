package se.roshauw.podplay.parcel;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class to describe a track
 * 
 * @author mats
 * 
 */
public class PodcastTrack implements Parcelable {

    private String title;
    private String description;
    private String fileUrl;

    public PodcastTrack() {
    }

    public PodcastTrack(Parcel source) {
        Bundle data = source.readBundle();
        title = data.getString("title");
        description = data.getString("description");
        fileUrl = data.getString("fileUrl");
    }

    public static final Parcelable.Creator<PodcastTrack> CREATOR = new Parcelable.Creator<PodcastTrack>() {
        public PodcastTrack createFromParcel(Parcel data) {
            return new PodcastTrack(data);
        }

        public PodcastTrack[] newArray(int size) {
            return new PodcastTrack[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle data = new Bundle();
        data.putString("title", title);
        data.putString("description", description);
        data.putString("fileUrl", fileUrl);
        dest.writeBundle(data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public String toString() {
        return title;
    }

}
