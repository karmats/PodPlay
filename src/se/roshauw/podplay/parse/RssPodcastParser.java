package se.roshauw.podplay.parse;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import se.roshauw.podplay.parcel.Podcast;
import se.roshauw.podplay.parcel.PodcastTrack;
import se.roshauw.podplay.parse.handler.RssPodcastHandler;
import se.roshauw.podplay.util.HttpConnectionHelper;

/**
 * Parses a podcast rss feed
 * 
 * @author mats
 * 
 */
public class RssPodcastParser {

    private HttpConnectionHelper connectionHelper;

    public RssPodcastParser() {
        this.connectionHelper = new HttpConnectionHelper();
    }

    /**
     * Get the track list for a specific podcast
     * 
     * @param podcast
     *            The podcast to get the tracklist for
     * @return List of {@link PodcastTrack}
     */
    public ArrayList<PodcastTrack> getTracksForPodcast(Podcast podcast) {
        try {
            HttpURLConnection connection = connectionHelper.connectToUrl(podcast.getFeedUrl());
            SAXParser rssSaxParser = SAXParserFactory.newInstance().newSAXParser();
            RssPodcastHandler rssHandler = new RssPodcastHandler();
            rssSaxParser.parse(connection.getInputStream(), rssHandler);
            return rssHandler.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<PodcastTrack>();
    }
}
