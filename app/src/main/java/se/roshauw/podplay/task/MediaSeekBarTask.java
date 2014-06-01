package se.roshauw.podplay.task;

import android.media.MediaPlayer;
import android.os.Handler;
import android.text.format.DateUtils;
import android.widget.SeekBar;
import android.widget.TextView;

import se.roshauw.podplay.util.PodPlayUtil;

/**
 * Task for making a SeekBar follow a MediaPlayers duration
 */
public class MediaSeekBarTask implements Runnable {
    private static final long DELAY = 1000;

    private SeekBar mSeekBar;
    private MediaPlayer mMediaPlayer;
    private Handler mHandler;
    private TextView mElapsedTextView;
    private boolean stopped;

    public MediaSeekBarTask(SeekBar seekBar, TextView elapsedTextView, MediaPlayer mediaPlayer, Handler handler) {
        this.mSeekBar = seekBar;
        this.mElapsedTextView = elapsedTextView;
        this.mMediaPlayer = mediaPlayer;
        this.mHandler = handler;
        this.stopped = false;
    }

    @Override
    public void run() {
        if (!stopped && mMediaPlayer.isPlaying()) {
            int duration = mMediaPlayer.getDuration();
            int position = mMediaPlayer.getCurrentPosition();
            if (position <= duration) {
                // Update the seek bar and the elapsed text view
                mSeekBar.setProgress(position);
                mElapsedTextView.setText(DateUtils.formatElapsedTime(position / 1000));
            } else {
                mHandler.removeCallbacks(this);
                stopped = true;
            }
        }
        if (!stopped) {
            mHandler.postDelayed(this, DELAY);
        }
    }

    public void reset() {
        this.stopped = false;
    }

    public void cancel() {
        this.stopped = true;
    }
}
