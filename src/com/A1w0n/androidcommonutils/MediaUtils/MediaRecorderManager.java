package com.A1w0n.androidcommonutils.MediaUtils;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.media.MediaRecorder.AudioEncoder;
import android.media.MediaRecorder.AudioSource;
import android.media.MediaRecorder.OutputFormat;
import android.util.Log;

public class MediaRecorderManager {

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
		} catch (IOException e) {
			Log.e("MediaRecorderManager", "io problems while preparing [" + file.getAbsolutePath() + "]: " + e.getMessage());
		}
	}
	
	public void stopRecording() {
		if (mMediaRecorder != null) {
			mMediaRecorder.stop();
			mMediaRecorder.release();
			mMediaRecorder = null;
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

}
