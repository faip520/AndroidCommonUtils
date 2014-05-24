package com.A1w0n.androidcommonutils;

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * @author Aiwan
 * 
 *  Just use this class's api to play music, passing in a absolute path or a url.
 *  It will dealing all things about MediaPlayer, u just need to call a simple function.
 * 
 * Probally will a print a log in logcat which says :"Should have subtitle controller already set".
 * You can ignore this safely.
 */
public class MediaPlayerManager {

	private static MediaPlayerManager mInstance;

	private static MediaPlayer mMediaPlayer;
	
	private static MediaPlayer mBGMediaPlayer;
	
	private int mMediaPlayerPlayLocation;
	
	private int mBGmMediaPlayerPlayLocation;

	private MediaPlayerManager() {
		initMediaPlayer();
		initBGMediaPlayer();
	}
	
	private static void initMediaPlayer() {
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.start();
			}
		});
	}
	
	private static void initBGMediaPlayer() {
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
	 * Note : MediaPlayer is no longer usable after released.
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
	 * Note : MediaPlayer is no longer usable after released.
	 */
	public void releaseBGMediaPlayer() {
		if (mBGMediaPlayer != null) {
			mBGMediaPlayer.release();
			mBGMediaPlayer = null;
		}
	}

	public static MediaPlayerManager getInstance() {
		if (mInstance == null) {
			mInstance = new MediaPlayerManager();
		}
		if (mMediaPlayer == null) {
			initMediaPlayer();
		}
		if (mBGMediaPlayer == null) {
			initBGMediaPlayer();
		}
		return mInstance;
	}

	private void pause() {
		try {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.pause();
				mMediaPlayerPlayLocation = mMediaPlayer.getCurrentPosition();
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}
	
	private void pauseBG() {
		try {
			if (mBGMediaPlayer.isPlaying()) {
				mBGMediaPlayer.pause();
				mBGmMediaPlayerPlayLocation = mBGMediaPlayer.getCurrentPosition();
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}
	
	private void resume() {
		if (mMediaPlayerPlayLocation == 0) return;
		
		try {
			mMediaPlayer.seekTo(mMediaPlayerPlayLocation);
			mMediaPlayer.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}
	
	private void resumeBG() {
		if (mBGmMediaPlayerPlayLocation == 0) return;
		
		try {
			mBGMediaPlayer.seekTo(mBGmMediaPlayerPlayLocation);
			mBGMediaPlayer.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}
	
	public void pauseBoth() {
		pause();
		pauseBG();
	}
	
	public void resumeBoth() {
		resume();
		resumeBG();
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
		if (mMediaPlayer == null) return;
		
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
		if (mMediaPlayer != null) {
			mMediaPlayer.reset();
		}
		if (mBGMediaPlayer != null) {
			mBGMediaPlayer.reset();
		}
	}
	
	private void playBGMP3(String absolutePath) {
		if (mBGMediaPlayer == null) return;
		
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
	
	// =====================API for outer use=====================
	public void playByAbsolutePathOrUrl(String pathOrUrl) {
		playMP3(pathOrUrl);
	}
	
	public void playBGMByAbsoluteOrUrl(String pathOrUrl) {
		playBGMP3(pathOrUrl);
	}
	
	// ======================================================
	
}
