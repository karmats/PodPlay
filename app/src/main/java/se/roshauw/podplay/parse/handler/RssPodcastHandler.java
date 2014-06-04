package se.roshauw.podplay.parse.handler;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

import se.roshauw.podplay.parcel.Podcast;
import se.roshauw.podplay.parcel.PodcastTrack;

/**
 * Handler for parsing podcast rss.
 *
 * @author mats
 */
public class RssPodcastHandler extends DefaultHandler {

    private Podcast podcast;
    private ArrayList<PodcastTrack> podcastTracks;

    private PodcastTrack currentPodcastTrack;
    private StringBuilder stringBuilder;

    /**
     * Creates a new instance of RssPodcastHandler
     *
     * @param podcast The Podcast to get the tracks for
     */
    public RssPodcastHandler(Podcast podcast) {
        this.podcast = podcast;
    }

    @Override
    public void startDocument() {
        podcastTracks = new ArrayList<PodcastTrack>();
    }

    /**
     * @return Enriched podcast with tracks and description
     */
    public Podcast getResult() {
        podcast.getTracks().addAll(podcastTracks);
        return podcast;
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
        String value = stringBuilder.toString();
        if (currentPodcastTrack != null) {
            if ("title".equals(qName)) {
                currentPodcastTrack.setTitle(value);
            } else if ("description".equals(qName) || "itunes:summary".equals(qName)) {
                currentPodcastTrack.setDescription(value);
            }
        } else {
            if ("description".equals(qName)) {
                podcast.setDescription(value);
            }
        }
    }
}
