package com.faip.androidcommonutils.ioutils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

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
		} catch(NumberFormatException nfe) {
			return -1;
		}
		return result;
	}
}
