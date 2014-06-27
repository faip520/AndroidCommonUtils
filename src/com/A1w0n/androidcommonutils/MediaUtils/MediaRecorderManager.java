package com.A1w0n.androidcommonutils.MediaUtils;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.media.MediaRecorder.AudioEncoder;
import android.media.MediaRecorder.AudioSource;
import android.media.MediaRecorder.OutputFormat;
import android.util.Log;

public class MediaRecorderManager {
	
	// ===========State==============
	public static final int STATE_RECORDING = 1;
	
	public static final int STATE_IDLE = 0;
	
	public static int CurrentState = -1;
	
	// ============================
	

	private static MediaRecorderManager mInstance;

	private MediaRecorder mMediaRecorder;

	private MediaRecorderManager() {
	}

	public static MediaRecorderManager getInstance() {
		if (mInstance == null) {
			mInstance = new MediaRecorderManager();
		}

		return mInstance;
	}

	public void startRecording(File file) {
		if (mMediaRecorder != null) {
			mMediaRecorder.release();
		}

		mMediaRecorder = new MediaRecorder();
		mMediaRecorder = new MediaRecorder();
		mMediaRecorder.setAudioSource(AudioSource.MIC);
		mMediaRecorder.setMaxDuration(30 * 1000);
		mMediaRecorder.setOutputFormat(OutputFormat.THREE_GPP);
		mMediaRecorder.setAudioEncoder(AudioEncoder.AMR_WB);
		mMediaRecorder.setOutputFile(file.getAbsolutePath());

		try {
			mMediaRecorder.prepare();
			mMediaRecorder.start();
			CurrentState = STATE_RECORDING;
		} catch (IOException e) {
			Log.e("MediaRecorderManager", "io problems while preparing [" + file.getAbsolutePath() + "]: " + e.getMessage());
			mMediaRecorder.reset();
			mMediaRecorder.release();
			mMediaRecorder = null;
			CurrentState = STATE_IDLE;
		}
	}
	
	public void stopRecording() {
		if (mMediaRecorder != null) {
			mMediaRecorder.stop();
			mMediaRecorder.release();
			mMediaRecorder = null;
			CurrentState = STATE_IDLE;
		}
	}
	
	/**
	 * Must be call in the caller activity's onPause method.
	 */
	public void onActivityPause() {
		if (mMediaRecorder != null) {
			mMediaRecorder.release();
			mMediaRecorder = null;
		}
	}
	
	public int getMaxAmplitude() {
		int result = 0;
		if (mMediaRecorder != null && CurrentState == STATE_RECORDING) {
			result = mMediaRecorder.getMaxAmplitude();
		}
		
		return result;
	}

}
