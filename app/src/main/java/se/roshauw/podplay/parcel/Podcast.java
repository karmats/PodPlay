package se.roshauw.podplay.parcel;

import java.util.ArrayList;
import java.util.List;

import se.roshauw.podplay.R;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class to describe a podcast. The id and title fields are final, and must be
 * set in constructor
 * 
 * @author mats
 * 
 */
public class Podcast implements Parcelable {

    private final Long id;
    private final String title;
    private String description;
    private String imgUrl;
    private String feedUrl;
    private final ArrayList<Integer> categoryIds;
    private final ArrayList<PodcastTrack> tracks;

    public Podcast(Long id, String title) {
        this.id = id;
        this.title = title;
        categoryIds = new ArrayList<Integer>();
        tracks = new ArrayList<PodcastTrack>();
    }

    public Podcast(Parcel source) {
        Bundle data = source.readBundle();
        id = data.getLong("id");
        title = data.getString("title");
        description = data.getString("description");
        imgUrl = data.getString("imgUrl");
        feedUrl = data.getString("feedUrl");
        categoryIds = data.getIntegerArrayList("categoryIds");
        tracks = data.getParcelableArrayList("tracks");
    }

    public static final Parcelable.Creator<Podcast> CREATOR = new Parcelable.Creator<Podcast>() {
        public Podcast createFromParcel(Parcel data) {
            return new Podcast(data);
        }

        public Podcast[] newArray(int size) {
            return new Podcast[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle data = new Bundle();
        data.putLong("id", id);
        data.putString("title", title);
        data.putString("description", description);
        data.putString("imgUrl", imgUrl);
        data.putString("feedUrl", feedUrl);
        data.putIntegerArrayList("categoryIds", categoryIds);
        data.putParcelableArrayList("tracks", tracks);
        dest.writeBundle(data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getFeedUrl() {
        return feedUrl;
    }

    public void setFeedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public List<PodcastTrack> getTracks() {
        return tracks;
    }
    
    @Override
    public String toString() {
        return title;
    }

    /**
     * Used to describe podcast categories. The <code>itunesId</code> represents the category
     * id from itunes, and the <code>stringId</code> represents the id defined in strings.xml
     * 
     * @author mats
     * 
     */
    public enum Category {

        ARTS(1301, R.string.arts),
        COMEDY(1303, R.string.comedy),
        EDUCATION(1304, R.string.education),
        KIDS_FAMILY(1305, R.string.kids_family),
        HEALTH(1307, R.string.health),
        TV_FILM(1309, R.string.tv_film),
        MUSIC(1310, R.string.music),
        NEWS_POLITICS(1311, R.string.news_politics),
        RELIGION_SPIRITUALITY(1314, R.string.religion_spirituality),
        SCIENCE_MEDICINE(1315, R.string.science_medicine),
        SPORTS_RECREATION(1316, R.string.sports_recreation),
        TECHNOLOGY(1318, R.string.technology),
        BUSINESS(1321, R.string.business),
        GAMES_HOBBIES(1323, R.string.games_hobbies),
        SOCIETY_CULTURE(1324, R.string.society_culture),
        GOVERNMENT_ORGANIZATIONS(1325, R.string.government_organizations),
        UNKNOWN(-1, -1);

        private int itunesId;
        private int stringId;

        private Category(int itunesId, int stringId) {
            this.itunesId = itunesId;
            this.stringId = stringId;
        }

        /**
         * @return an integer representing a string defined in strings.xml
         */
        public int getStringId() {
            return stringId;
        }

        /**
         * @return an integer representing the category id in itunes
         */
        public int getItunesId() {
            return itunesId;
        }
        
        /**
         * Gets a {@link Category} for a specific itunes id
         * 
         * @param id
         *            The category id in itunes
         * @return {@link Category}
         */
        static Category getCategoryForItunesId(int id) {
            for (Category c : values()) {
                if (c.getItunesId() == id) {
                    return c;
                }
            }
            return UNKNOWN;
        }
    }

}
