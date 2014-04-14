package se.roshauw.podplay.activity;

import se.roshauw.podplay.R;
import se.roshauw.podplay.parcel.PodcastTrack;
import se.roshauw.podplay.util.PodPlayUtil;
import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * The activity for playing podcasts
 * 
 * @author mats
 * 
 */
public class PlayPodcastActivity extends Activity {

    /**
     * The MediaPlayer that plays the podcast
     */
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PodcastTrack podcastToPlay = getIntent().getExtras().getParcelable(PodPlayUtil.EXTRA_PODCAST_TRACK);

        setContentView(R.layout.podcast_player);

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            PodPlayUtil.logInfo("Playing media from " + podcastToPlay.getFileUrl());
            mMediaPlayer.setDataSource(podcastToPlay.getFileUrl());
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            PodPlayUtil.logException(e);
        }

        final TextView tv = (TextView) findViewById(R.id.textView1);
        mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {

            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                tv.setText(percent + "%");
            }
        });

        // Disable the Play Button
        final Button playButton = (Button) findViewById(R.id.button3);
        playButton.setEnabled(false);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                playButton.setEnabled(true);
            }
        });

        // Play the sound using a SoundPool
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                } else {
                    mMediaPlayer.start();
                }
            }

        });

    }
}
