package com.A1w0n.androidcommonutils.DebugUtils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 把log写入到本地文件
 *
 * Created by A1w0n on 15/3/4.
 */
public class FileLogger {

    StringBuilder mBuilder = new StringBuilder();

    File mFile;

    private int mNumInBuffer = 0;
    private int mNumFlushes = 0;

    private boolean mTracking = false;

    public FileLogger() {
        File root = Environment.getExternalStorageDirectory();
        mFile = new File(root, "dslv_state.txt");

        if (!mFile.exists()) {
            try {
                mFile.createNewFile();
                Log.d("mobeta", "file created");
            } catch (IOException e) {
                Log.w("mobeta", "Could not create dslv_state.txt");
                Log.d("mobeta", e.getMessage());
            }
        }

    }

    public void startTracking() {
        mBuilder.append("<DSLVStates>\n");
        mNumFlushes = 0;
        mTracking = true;
    }

    public void appendState() {
        if (!mTracking) {
            return;
        }

        mBuilder.append("<DSLVState>\n");
        mBuilder.append("</DSLVState>\n");
        mNumInBuffer++;

        if (mNumInBuffer > 1000) {
            flush();
            mNumInBuffer = 0;
        }
    }

    public void flush() {
        if (!mTracking) {
            return;
        }

        // save to file on sdcard
        try {
            boolean append = true;
            if (mNumFlushes == 0) {
                append = false;
            }
            FileWriter writer = new FileWriter(mFile, append);

            writer.write(mBuilder.toString());
            mBuilder.delete(0, mBuilder.length());

            writer.flush();
            writer.close();

            mNumFlushes++;
        } catch (IOException e) {
            // do nothing
        }
    }

    public void stopTracking() {
        if (mTracking) {
            mBuilder.append("</DSLVStates>\n");
            flush();
            mTracking = false;
        }
    }

}
