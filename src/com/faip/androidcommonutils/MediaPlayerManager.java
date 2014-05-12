package com.faip.androidcommonutils;

import java.io.IOException;

import android.media.MediaPlayer;

public class MediaPlayerManager {
	
	private static MediaPlayerManager mInstance;
	
	private MediaPlayer mMediaPlayer;

	private MediaPlayerManager() {
		mMediaPlayer = new MediaPlayer();
	}
	
	public static MediaPlayerManager getInstance() {
		if (mInstance == null) {
			mInstance = new MediaPlayerManager();
		}
		return mInstance;
	}
	
	public void pause() {
		if (mMediaPlayer.isPlaying()) {
			mMediaPlayer.pause();
		}
	}
	
	public void playMP3(String absolutePath) {
		if (mMediaPlayer.isPlaying()) {
			mMediaPlayer.pause();
			mMediaPlayer.reset();
		}
		
		try {
			mMediaPlayer.setDataSource(absolutePath);
		} catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
			e.printStackTrace();
			
		}
		try {
			mMediaPlayer.prepare();
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		mMediaPlayer.start();
	}
}
