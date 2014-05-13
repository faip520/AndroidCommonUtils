package com.faip.androidcommonutils;

import java.io.File;
import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;

public class MediaPlayerManager {

	private static MediaPlayerManager mInstance;

	private static MediaPlayer mMediaPlayer;

	private MediaPlayerManager() {
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.start();
			}
		});
	}

	/**
	 * Call this when you don't want to play anything. Don't do this if you have any single chance
	 * that you will play something.
	 */
	public void releaseMediaPlayer() {
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	public static MediaPlayerManager getInstance() {
		if (mInstance == null) {
			mInstance = new MediaPlayerManager();
			if (mMediaPlayer == null) {
				mMediaPlayer = new MediaPlayer();
			}
		}
		return mInstance;
	}

	public void pause() {
		if (mMediaPlayer.isPlaying()) {
			mMediaPlayer.pause();
		}
	}
	
	/**
	 * If u want to stop and don't need to resume playing.
	 */
	public void resetMediaPlayer() {
		mMediaPlayer.reset();
	}

	public void playMP3(String absolutePath) {
		mMediaPlayer.reset();

		try {
			mMediaPlayer.setDataSource(absolutePath);
		} catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
			e.printStackTrace();
			return;
		}
		try {
			mMediaPlayer.prepareAsync();
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return;
		}
	}
}