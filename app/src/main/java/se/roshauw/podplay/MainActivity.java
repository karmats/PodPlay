package se.roshauw.podplay;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import se.roshauw.podplay.fragment.SubscribedFragment;
import se.roshauw.podplay.fragment.ViewPodcastFragment;
import se.roshauw.podplay.parcel.Podcast;
import se.roshauw.podplay.parcel.PodcastTrack;
import se.roshauw.podplay.task.DownloadImageTask;
import se.roshauw.podplay.task.MediaSeekBarTask;
import se.roshauw.podplay.util.PodPlayUtil;

/**
 * Main activity for the application. All fragments will be created from here
 *
 * @author mats
 */
public class MainActivity extends FragmentActivity {

    // The sliding up panel for viewing media controller
    private SlidingUpPanelLayout mSlidingUpPanelLayout;
    // The MediaPlayer that plays the podcast
    private MediaPlayer mMediaPlayer;
    // Play button
    private ImageButton mPlayButton;
    // Podcast title
    private TextView mPlayingText;
    // Podcast image display view
    private ImageView mPodcastImgView;
    // View for video display
    private SurfaceView mPodcastVideoView;
    private SurfaceHolder mSurfaceHolder;
    // Linear layout that holds media controller buttons
    private LinearLayout mMediaControllerLayout;
    // Variables needed for the seek bar
    private LinearLayout mSeekBarController;
    private SeekBar mSeekBar;
    private TextView mDurationTextView;
    private TextView mElapsedTextView;
    private Handler mSeekBarHandler = new Handler();
    private Runnable mMediaSeekBarTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mSlidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mPlayButton = (ImageButton) findViewById(R.id.media_play_button);
        mPlayingText = (TextView) findViewById(R.id.media_playing_text);
        mPodcastImgView = (ImageView) findViewById(R.id.media_podcast_img);
        mMediaControllerLayout = (LinearLayout) findViewById(R.id.media_controller);
        mSeekBarController = (LinearLayout) findViewById(R.id.media_seek_bar_controller);
        mSeekBar = (SeekBar) findViewById(R.id.media_seek);
        mSeekBar.setEnabled(false);
        mDurationTextView = (TextView) findViewById(R.id.media_time_duration);
        mElapsedTextView = (TextView) findViewById(R.id.media_time_elapsed);
        mPodcastVideoView = (SurfaceView) findViewById(R.id.media_podcast_video);
        // So we don't create a black whole when sliding up/down the
        // SlidingUpPanel
        mPodcastVideoView.setZOrderOnTop(true);

        // Setup the media player and surface holder for video playback
        mMediaPlayer = new MediaPlayer();
        mSurfaceHolder = mPodcastVideoView.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mMediaPlayer.setDisplay(mSurfaceHolder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
        });

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
        // Then we need to start the ViewPodcastFragment
        Podcast podcastExtra = null;
        if (null != getIntent() && null != getIntent().getExtras()) {
            podcastExtra = getIntent().getExtras().getParcelable(PodPlayUtil.EXTRA_PODCAST);
        }
        PodPlayUtil.logInfo("Got podcast " + podcastExtra + " as extra");
        if (null == podcastExtra) {
            SubscribedFragment subscribedFragment = SubscribedFragment.create();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, subscribedFragment)
                    .commit();
        } else {
            ViewPodcastFragment viewPodcastFragment = ViewPodcastFragment.create(podcastExtra);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, viewPodcastFragment)
                    .commit();
        }

    }

    @Override
    protected void onPause() {
        stopSeekBarTask();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // Stop the seek bar task and null it
        stopSeekBarTask();
        mMediaSeekBarTask = null;
        // Release the media player
        mMediaPlayer.release();
        mMediaPlayer = null;
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        startSeekBarTask();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mSlidingUpPanelLayout.isExpanded()) {
            mSlidingUpPanelLayout.collapsePane();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Plays a podcast track. Sets up the video view if it's a video.
     *
     * @param podcast      The podcast the track belongs to
     * @param podcastTrack The track to play
     */
    public void playPodcastTrack(final Podcast podcast, final PodcastTrack podcastTrack) {
        // Setup the podcast image or video if this is a video podcast
        if (podcastTrack.getType() == PodcastTrack.TYPE_VIDEO) {
            mPodcastVideoView.setVisibility(View.VISIBLE);
            mPodcastImgView.setVisibility(View.GONE);
        } else { // Audio
            mPodcastImgView.setVisibility(View.VISIBLE);
            // Sets the podcasts image to the image view
            new DownloadImageTask(mPodcastImgView).execute(podcast.getImgUrl());
            mPodcastVideoView.setVisibility(View.GONE);
        }
        mMediaControllerLayout.setVisibility(View.VISIBLE);
        mSeekBarController.setVisibility(View.VISIBLE);

        // Reset the seek bar runnable
        stopSeekBarTask();

        // Reset the podast in case it's already prepared
        mMediaPlayer.reset();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            PodPlayUtil.logInfo("Playing media from " + podcastTrack.getFileUrl());
            mMediaPlayer.setDataSource(podcastTrack.getFileUrl());
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            mPlayingText.setText(e.getMessage());
            // Recreate the mediaplayer
            mMediaPlayer.release();
            mMediaPlayer = new MediaPlayer();
            PodPlayUtil.logException(e);
        }

        // Start the podcast when it's been prepared
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // Set podcast track text and start playing
                mPlayingText.setText(podcastTrack.getTitle());
                mMediaPlayer.start();

                // Enable the play button and set the correct image for it
                mPlayButton.setEnabled(true);
                mPlayButton.setImageResource(R.drawable.ic_action_pause_over_video);

                // Enable the seek bar and start new runnable to post media duration to it
                mSeekBar.setEnabled(true);
                mSeekBar.setMax(mMediaPlayer.getDuration());
                startSeekBarTask();

                String durationString = DateUtils.formatElapsedTime(mMediaPlayer.getDuration() / 1000);
                mDurationTextView.setText(durationString);
            }
        });

        mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int percentage) {
                int duration = mSeekBar.getMax();
                mSeekBar.setSecondaryProgress((duration * percentage) / 100);
            }
        });


        // Error listener if something went wrong with prepareAsync
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mPlayingText.setText("Got error " + what + " Extra " + extra);
                // Recreate the mediaplayer
                mMediaPlayer.release();
                mMediaPlayer = new MediaPlayer();
                return true;
            }
        });

        // Seek bar setup
        mSeekBar.setMax(120);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mMediaPlayer.seekTo(progress);
                    mSeekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


    }

    // private function
    private void stopSeekBarTask() {
        if (null != mMediaSeekBarTask) {
            mSeekBarHandler.removeCallbacks(mMediaSeekBarTask);
        }
    }

    private void startSeekBarTask() {
        if (mMediaPlayer.isPlaying()) {
            if (null == mMediaSeekBarTask) {
                mMediaSeekBarTask = new MediaSeekBarTask(mSeekBar, mElapsedTextView, mMediaPlayer, mSeekBarHandler);
            }
            mSeekBarHandler.post(mMediaSeekBarTask);
        }
    }
}
