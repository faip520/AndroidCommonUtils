package com.faip.androidcommonutils;

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;

public class WYSMediaPlayerManager {

	private static WYSMediaPlayerManager mInstance;

	private static MediaPlayer mMediaPlayer;
	
	private static MediaPlayer mBGMediaPlayer;

	private WYSMediaPlayerManager() {
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.start();
			}
		});
		
		mBGMediaPlayer = new MediaPlayer();
		mBGMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mBGMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
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
	
	/**
	 * Call this when you don't want to play anything. Don't do this if you have any single chance
	 * that you will play something.
	 */
	public void releaseBGMediaPlayer() {
		if (mBGMediaPlayer != null) {
			mBGMediaPlayer.release();
			mBGMediaPlayer = null;
		}
	}

	public static WYSMediaPlayerManager getInstance() {
		if (mInstance == null) {
			mInstance = new WYSMediaPlayerManager();
			if (mMediaPlayer == null) {
				mMediaPlayer = new MediaPlayer();
			}
			if (mBGMediaPlayer == null) {
				mBGMediaPlayer = new MediaPlayer();
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
	
	/**
	 * If u want to stop and don't need to resume playing.
	 */
	public void resetBGMediaPlayer() {
		mBGMediaPlayer.reset();
	}

	private void playMP3(String absolutePath) {
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
	
	public void resetBoth() {
		mMediaPlayer.reset();
		mBGMediaPlayer.reset();
	}
	
	public void playWYSMP3(String relativePath) {
		//playMP3(WYSConstants.WYSResourcesPath + File.separator + relativePath);
	}
	
	private void playBGMP3(String absolutePath) {
		mBGMediaPlayer.reset();

		try {
			mBGMediaPlayer.setDataSource(absolutePath);
		} catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
			e.printStackTrace();
			return;
		}
		try {
			mBGMediaPlayer.prepareAsync();
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public void playWYSBGMP3(String relativePath) {
		//playBGMP3(WYSConstants.WYSResourcesPath + File.separator + relativePath);
	}
}
