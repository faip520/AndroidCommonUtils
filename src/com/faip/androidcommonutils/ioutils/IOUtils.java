package com.faip.androidcommonutils.ioutils;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.net.Uri;

/**
 * IO utilities.
 */
public class IOUtils {

	/**
	 * The default size of the buffer.
	 */
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	private IOUtils() {
	}

	/**
	 * Copy bytes from an InputStream to an OutputStream. Reference from apache common io package.
	 */
	public static int copy(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	/**
	 * Reads an InputStream and converts it to a String.
	 */
	public String inputStreamToString(InputStream stream, int len) throws IOException {
		Reader reader = null;
		reader = new InputStreamReader(stream, "UTF-8");
		char[] buffer = new char[len];
		reader.read(buffer);
		return new String(buffer);
	}

	/**
	 * Usually used in try/catch block's finally block.
	 */
	public static void closeSilently(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException ignored) {

			}
		}
	}

	public static int string2Int(String num) {
		int result;
		try {
			result = Integer.parseInt(num);
		} catch (NumberFormatException nfe) {
			return -1;
		}
		return result;
	}

	public static InputStream getInputStreamByUri(Context context, Uri uri) {
		if (uri == null)
			return null;
		String scheme = uri.getScheme();
		InputStream stream = null;
		if ((scheme == null) || ("file".equals(scheme))) {
			if (("file".equals(scheme)) && (uri.getPath().startsWith("/android_asset/"))) {
				try {
					stream = context.getAssets().open(uri.getPath().substring("/android_asset/".length()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else
				stream = getFileInputStream(uri.getPath());
		} else if ("content".equals(scheme)) {
			stream = getContentInputStreamByUri(context, uri);
		} else if (("http".equals(scheme)) || ("https".equals(scheme))) {
			stream = openRemoteInputStream(uri);
		}
		return stream;
	}

	public static InputStream getFileInputStream(String path) {
		try {
			return new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static InputStream getContentInputStreamByUri(Context context, Uri uri) {
		try {
			return context.getContentResolver().openInputStream(uri);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static InputStream openRemoteInputStream(Uri uri) {
		URL finalUrl;
		HttpURLConnection connection;
		
		try {
			finalUrl = new URL(uri.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
		
		try {
			connection = (HttpURLConnection) finalUrl.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		connection.setInstanceFollowRedirects(false);
		
		int code;
		try {
			code = connection.getResponseCode();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		if ((code == 301) || (code == 302) || (code == 303)) {
			String newLocation = connection.getHeaderField("Location");
			return openRemoteInputStream(Uri.parse(newLocation));
		}
		
		try {
			return (InputStream) finalUrl.getContent();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
