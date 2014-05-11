package se.roshauw.podplay.parse.handler;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import se.roshauw.podplay.parcel.PodcastTrack;

/**
 * Handler for parsing podcast rss.
 * 
 * @author mats
 * 
 */
public class RssPodcastHandler extends DefaultHandler {

    private ArrayList<PodcastTrack> podcastTracks;

    private PodcastTrack currentPodcastTrack;
    private StringBuilder stringBuilder;

    @Override
    public void startDocument() {
        podcastTracks = new ArrayList<PodcastTrack>();
    }

    /**
     * @return The parsed list with tracks
     */
    public ArrayList<PodcastTrack> getResult() {
        return podcastTracks;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        stringBuilder = new StringBuilder();

        if (qName.equals("item")) {
            currentPodcastTrack = new PodcastTrack();
            podcastTracks.add(currentPodcastTrack);
        } else if (qName.equals("enclosure")) {
            currentPodcastTrack.setFileUrl(attributes.getValue("url"));
            String type = attributes.getValue("type");
            if (null != type && type.startsWith("video")) {
                currentPodcastTrack.setType(PodcastTrack.TYPE_VIDEO);
            } else {
                // Default audio type
                currentPodcastTrack.setType(PodcastTrack.TYPE_AUDIO);
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        stringBuilder.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (currentPodcastTrack != null) {
            String value = stringBuilder.toString();
            if ("title".equals(qName)) {
                currentPodcastTrack.setTitle(value);
            } else if ("itunes:summary".equals(qName)) {
                currentPodcastTrack.setDescription(value);
            }
        }
    }
}
