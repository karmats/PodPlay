package se.roshauw.podplay;

import se.roshauw.podplay.fragment.SubscribedFragment;
import se.roshauw.podplay.fragment.ViewPodcastFragment;
import se.roshauw.podplay.parcel.Podcast;
import se.roshauw.podplay.parcel.PodcastTrack;
import se.roshauw.podplay.util.PodPlayUtil;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Main activity for the application. All fragments will be created from here
 * 
 * @author mats
 * 
 */
public class MainActivity extends FragmentActivity {

    // The MediaPlayer that plays the podcast
    private MediaPlayer mMediaPlayer;
    // Play button
    private ImageButton mPlayButton;
    // Podcast title
    private TextView mPlayingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mPlayButton = (ImageButton) findViewById(R.id.media_play_button);
        mPlayingText = (TextView) findViewById(R.id.media_playing_text);

        // Setup the media player
        mMediaPlayer = new MediaPlayer();

        // Disable the Play Button until the media player is ready
        mPlayButton.setEnabled(false);
        // Play/Pause when hitting play button
        mPlayButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    mPlayButton.setImageResource(R.drawable.ic_action_play_over_video);
                } else {
                    mMediaPlayer.start();
                    mPlayButton.setImageResource(R.drawable.ic_action_pause_over_video);
                }
            }
        });

        // When a podcast extra is set, this is a result from the
        // SearchPodcastActivity.
        // The we need to start the ViewPodcastFragment
        Podcast podcastExtra = null;
        if (null != getIntent() && null != getIntent().getExtras()) {
            podcastExtra = getIntent().getExtras().getParcelable(PodPlayUtil.EXTRA_PODCAST);
        }
        PodPlayUtil.logInfo("Got podcast " + podcastExtra + " as extra");
        if (null == podcastExtra) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SubscribedFragment())
                    .commit();
        } else {
            Bundle bundle = new Bundle();
            bundle.putParcelable(PodPlayUtil.EXTRA_PODCAST, podcastExtra);
            ViewPodcastFragment viewPodcastFragment = new ViewPodcastFragment();
            viewPodcastFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, viewPodcastFragment)
                    .commit();
        }

    }

    @Override
    protected void onDestroy() {
        // Reset the media player
        mMediaPlayer.release();
        mMediaPlayer = null;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Plays a podcast track.
     * 
     * @param podcastTrack
     *            The track to play
     */
    public void playPodcastTrack(final PodcastTrack podcastTrack) {
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        // Stop and reset the podast in case it's already prepared
        mMediaPlayer.stop();
        mMediaPlayer.reset();

        try {
            PodPlayUtil.logInfo("Playing media from " + podcastTrack.getFileUrl());
            mMediaPlayer.setDataSource(podcastTrack.getFileUrl());
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            mPlayingText.setText(e.getMessage());
            PodPlayUtil.logException(e);
        }

        // Start the podcast when it's been prepared
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // Set podcast track text and start playing
                mPlayingText.setText(podcastTrack.getTitle());
                mMediaPlayer.start(); 
                mPlayButton.setEnabled(true);
            }
        });

        // Error listener if something went wrong with prepareAsync
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mPlayingText.setText("Got error " + what + " Extra " + extra);
                mp.reset();
                return true;
            }
        });

    }
}
