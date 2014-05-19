package se.roshauw.podplay.task;

import android.media.MediaPlayer;
import android.os.Handler;
import android.widget.SeekBar;

import se.roshauw.podplay.util.PodPlayUtil;

/**
 * Task for making a SeekBar follow a MediaPlayers duration
 */
public class MediaSeekBarTask implements Runnable {
    private static final long DELAY = 1000;
    private SeekBar mSeekBar;
    private MediaPlayer mMediaPlayer;
    private Handler mHandler;

    public MediaSeekBarTask(SeekBar seekBar, MediaPlayer mediaPlayer, Handler handler) {
        this.mSeekBar = seekBar;
        this.mMediaPlayer = mediaPlayer;
        this.mHandler = handler;
    }

    @Override
    public void run() {
        mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
        PodPlayUtil.logDebug("Setting progress to " + mMediaPlayer.getCurrentPosition());
        mHandler.postDelayed(this, DELAY);
    }
}
