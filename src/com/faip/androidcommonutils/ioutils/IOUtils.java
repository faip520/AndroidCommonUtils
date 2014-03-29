package com.faip.androidcommonutils.ioutils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
	 * Copy bytes from an InputStream to an OutputStream.
	 * Reference from apache common io package.
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
}
