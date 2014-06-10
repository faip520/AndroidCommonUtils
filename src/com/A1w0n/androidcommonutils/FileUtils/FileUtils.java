package com.A1w0n.androidcommonutils.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.A1w0n.androidcommonutils.ioutils.IOUtils;

public class FileUtils {
	
    /**
     * The file copy buffer size (30 MB)
     */
    private static final long FILE_COPY_BUFFER_SIZE = FileSizeUtils.SIZE_MB * 30;

	private FileUtils() {
	}

	/**
	 * If externalStorage mounted.
	 */
	public static boolean isExternalStorageMounted() {
		boolean canRead = Environment.getExternalStorageDirectory().canRead();
		boolean onlyRead = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
		boolean unMounted = Environment.getExternalStorageState().equals(Environment.MEDIA_UNMOUNTED);

		return !(!canRead || onlyRead || unMounted);
	}
	
	public static String getExternalStorageFolder() {
		String result = null;
		if (isExternalStorageMounted()) {
			result = Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		
		return result;
	}
	
    public static void copyInputStreamToFile(InputStream source, File destination) throws IOException {
        try {
            FileOutputStream output = openOutputStream(destination);
            try {
                IOUtils.copy(source, output);
                output.close(); // don't swallow close Exception if copy completes normally
            } finally {
                IOUtils.closeSilently(output);
            }
        } finally {
            IOUtils.closeSilently(source);
        }
    }
    
    public static FileOutputStream openOutputStream(File file) throws IOException {
        return openOutputStream(file, false);
    }
    
    public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canWrite() == false) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null) {
                if (!parent.mkdirs() && !parent.isDirectory()) {
                    throw new IOException("Directory '" + parent + "' could not be created");
                }
            }
        }
        return new FileOutputStream(file, append);
    }
    
    /**
     * Copies a directory to within another directory preserving the file dates.
     * <p>
     * This method copies the source directory and all its contents to a
     * directory of the same name in the specified destination directory.
     * <p>
     * The destination directory is created if it does not exist.
     * If the destination directory did exist, then this method merges
     * the source with the destination, with the source taking precedence.
     * <p>
     * <strong>Note:</strong> This method tries to preserve the files' last
     * modified date/times using {@link File#setLastModified(long)}, however
     * it is not guaranteed that those operations will succeed.
     * If the modification operation fails, no indication is provided.
     *
     * @param srcDir  an existing directory to copy, must not be {@code null}
     * @param destDir  the directory to place the copy in, must not be {@code null}
     *
     * @throws NullPointerException if source or destination is {@code null}
     * @throws IOException if source or destination is invalid
     * @throws IOException if an IO error occurs during copying
     * @since 1.2
     */
    public static void copyDirectoryToDirectory(File srcDir, File destDir) throws IOException {
        if (srcDir == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (srcDir.exists() && srcDir.isDirectory() == false) {
            throw new IllegalArgumentException("Source '" + destDir + "' is not a directory");
        }
        if (destDir == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (destDir.exists() && destDir.isDirectory() == false) {
            throw new IllegalArgumentException("Destination '" + destDir + "' is not a directory");
        }
        copyDirectory(srcDir, new File(destDir, srcDir.getName()), true);
    }
    
    /**
     * Copies a whole directory to a new location.
     * <p>
     * This method copies the contents of the specified source directory
     * to within the specified destination directory.
     * <p>
     * The destination directory is created if it does not exist.
     * If the destination directory did exist, then this method merges
     * the source with the destination, with the source taking precedence.
     * <p>
     * <strong>Note:</strong> Setting <code>preserveFileDate</code> to
     * {@code true} tries to preserve the files' last modified
     * date/times using {@link File#setLastModified(long)}, however it is
     * not guaranteed that those operations will succeed.
     * If the modification operation fails, no indication is provided.
     *
     * @param srcDir  an existing directory to copy, must not be {@code null}
     * @param destDir  the new directory, must not be {@code null}
     * @param preserveFileDate  true if the file date of the copy
     *  should be the same as the original
     *
     * @throws NullPointerException if source or destination is {@code null}
     * @throws IOException if source or destination is invalid
     * @throws IOException if an IO error occurs during copying
     * @since 1.1
     */
    public static void copyDirectory(File srcDir, File destDir,
            boolean preserveFileDate) throws IOException {
        copyDirectory(srcDir, destDir, null, preserveFileDate);
    }
    
    /**
     * Copies a filtered directory to a new location.
     * <p>
     * This method copies the contents of the specified source directory
     * to within the specified destination directory.
     * <p>
     * The destination directory is created if it does not exist.
     * If the destination directory did exist, then this method merges
     * the source with the destination, with the source taking precedence.
     * <p>
     * <strong>Note:</strong> Setting <code>preserveFileDate</code> to
     * {@code true} tries to preserve the files' last modified
     * date/times using {@link File#setLastModified(long)}, however it is
     * not guaranteed that those operations will succeed.
     * If the modification operation fails, no indication is provided.
     *
     * <h4>Example: Copy directories only</h4> 
     *  <pre>
     *  // only copy the directory structure
     *  FileUtils.copyDirectory(srcDir, destDir, DirectoryFileFilter.DIRECTORY, false);
     *  </pre>
     *
     * <h4>Example: Copy directories and txt files</h4>
     *  <pre>
     *  // Create a filter for ".txt" files
     *  IOFileFilter txtSuffixFilter = FileFilterUtils.suffixFileFilter(".txt");
     *  IOFileFilter txtFiles = FileFilterUtils.andFileFilter(FileFileFilter.FILE, txtSuffixFilter);
     *
     *  // Create a filter for either directories or ".txt" files
     *  FileFilter filter = FileFilterUtils.orFileFilter(DirectoryFileFilter.DIRECTORY, txtFiles);
     *
     *  // Copy using the filter
     *  FileUtils.copyDirectory(srcDir, destDir, filter, false);
     *  </pre>
     * 
     * @param srcDir  an existing directory to copy, must not be {@code null}
     * @param destDir  the new directory, must not be {@code null}
     * @param filter  the filter to apply, null means copy all directories and files
     * @param preserveFileDate  true if the file date of the copy
     *  should be the same as the original
     *
     * @throws NullPointerException if source or destination is {@code null}
     * @throws IOException if source or destination is invalid
     * @throws IOException if an IO error occurs during copying
     * @since 1.4
     */
    public static void copyDirectory(File srcDir, File destDir,
            FileFilter filter, boolean preserveFileDate) throws IOException {
        if (srcDir == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (destDir == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (srcDir.exists() == false) {
            throw new FileNotFoundException("Source '" + srcDir + "' does not exist");
        }
        if (srcDir.isDirectory() == false) {
            throw new IOException("Source '" + srcDir + "' exists but is not a directory");
        }
        if (srcDir.getCanonicalPath().equals(destDir.getCanonicalPath())) {
            throw new IOException("Source '" + srcDir + "' and destination '" + destDir + "' are the same");
        }

        // Cater for destination being directory within the source directory (see IO-141)
        List<String> exclusionList = null;
        if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath())) {
            File[] srcFiles = filter == null ? srcDir.listFiles() : srcDir.listFiles(filter);
            if (srcFiles != null && srcFiles.length > 0) {
                exclusionList = new ArrayList<String>(srcFiles.length);
                for (File srcFile : srcFiles) {
                    File copiedFile = new File(destDir, srcFile.getName());
                    exclusionList.add(copiedFile.getCanonicalPath());
                }
            }
        }
        doCopyDirectory(srcDir, destDir, filter, preserveFileDate, exclusionList);
    }
    
    /**
     * Internal copy directory method.
     * 
     * @param srcDir  the validated source directory, must not be {@code null}
     * @param destDir  the validated destination directory, must not be {@code null}
     * @param filter  the filter to apply, null means copy all directories and files
     * @param preserveFileDate  whether to preserve the file date
     * @param exclusionList  List of files and directories to exclude from the copy, may be null
     * @throws IOException if an error occurs
     * @since 1.1
     */
    private static void doCopyDirectory(File srcDir, File destDir, FileFilter filter,
            boolean preserveFileDate, List<String> exclusionList) throws IOException {
        // recurse
        File[] srcFiles = filter == null ? srcDir.listFiles() : srcDir.listFiles(filter);
        if (srcFiles == null) {  // null if abstract pathname does not denote a directory, or if an I/O error occurs
            throw new IOException("Failed to list contents of " + srcDir);
        }
        if (destDir.exists()) {
            if (destDir.isDirectory() == false) {
                throw new IOException("Destination '" + destDir + "' exists but is not a directory");
            }
        } else {
            if (!destDir.mkdirs() && !destDir.isDirectory()) {
                throw new IOException("Destination '" + destDir + "' directory cannot be created");
            }
        }
        if (destDir.canWrite() == false) {
            throw new IOException("Destination '" + destDir + "' cannot be written to");
        }
        for (File srcFile : srcFiles) {
            File dstFile = new File(destDir, srcFile.getName());
            if (exclusionList == null || !exclusionList.contains(srcFile.getCanonicalPath())) {
                if (srcFile.isDirectory()) {
                    doCopyDirectory(srcFile, dstFile, filter, preserveFileDate, exclusionList);
                } else {
                    doCopyFile(srcFile, dstFile, preserveFileDate);
                }
            }
        }

        // Do this last, as the above has probably affected directory metadata
        if (preserveFileDate) {
            destDir.setLastModified(srcDir.lastModified());
        }
    }
    
    /**
     * Internal copy file method.
     * 
     * @param srcFile  the validated source file, must not be {@code null}
     * @param destFile  the validated destination file, must not be {@code null}
     * @param preserveFileDate  whether to preserve the file date
     * @throws IOException if an error occurs
     */
    private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
        if (destFile.exists() && destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' exists but is a directory");
        }

        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel input = null;
        FileChannel output = null;
        try {
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(destFile);
            input  = fis.getChannel();
            output = fos.getChannel();
            long size = input.size();
            long pos = 0;
            long count = 0;
            while (pos < size) {
                count = size - pos > FILE_COPY_BUFFER_SIZE ? FILE_COPY_BUFFER_SIZE : size - pos;
                pos += output.transferFrom(input, pos, count);
            }
        } finally {
            IOUtils.closeSilently(output);
            IOUtils.closeSilently(fos);
            IOUtils.closeSilently(input);
            IOUtils.closeSilently(fis);
        }

        if (srcFile.length() != destFile.length()) {
            throw new IOException("Failed to copy full contents from '" +
                    srcFile + "' to '" + destFile + "'");
        }
        if (preserveFileDate) {
            destFile.setLastModified(srcFile.lastModified());
        }
    }
    
    /**
     * Copies a file to a new location preserving the file date.
     * <p>
     * This method copies the contents of the specified source file to the
     * specified destination file. The directory holding the destination file is
     * created if it does not exist. If the destination file exists, then this
     * method will overwrite it.
     * <p>
     * <strong>Note:</strong> This method tries to preserve the file's last
     * modified date/times using {@link File#setLastModified(long)}, however
     * it is not guaranteed that the operation will succeed.
     * If the modification operation fails, no indication is provided.
     * 
     * @param srcFile  an existing file to copy, must not be {@code null}
     * @param destFile  the new file, must not be {@code null}
     * 
     * @throws NullPointerException if source or destination is {@code null}
     * @throws IOException if source or destination is invalid
     * @throws IOException if an IO error occurs during copying
     * @see #copyFileToDirectory(File, File)
     */
    public static void copyFile(File srcFile, File destFile) throws IOException {
        copyFile(srcFile, destFile, true);
    }
    
    /**
     * Copies a file to a new location.
     * <p>
     * This method copies the contents of the specified source file
     * to the specified destination file.
     * The directory holding the destination file is created if it does not exist.
     * If the destination file exists, then this method will overwrite it.
     * <p>
     * <strong>Note:</strong> Setting <code>preserveFileDate</code> to
     * {@code true} tries to preserve the file's last modified
     * date/times using {@link File#setLastModified(long)}, however it is
     * not guaranteed that the operation will succeed.
     * If the modification operation fails, no indication is provided.
     *
     * @param srcFile  an existing file to copy, must not be {@code null}
     * @param destFile  the new file, must not be {@code null}
     * @param preserveFileDate  true if the file date of the copy
     *  should be the same as the original
     *
     * @throws NullPointerException if source or destination is {@code null}
     * @throws IOException if source or destination is invalid
     * @throws IOException if an IO error occurs during copying
     * @see #copyFileToDirectory(File, File, boolean)
     */
    public static void copyFile(File srcFile, File destFile,
            boolean preserveFileDate) throws IOException {
        if (srcFile == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (destFile == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (srcFile.exists() == false) {
            throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
        }
        if (srcFile.isDirectory()) {
            throw new IOException("Source '" + srcFile + "' exists but is a directory");
        }
        if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
            throw new IOException("Source '" + srcFile + "' and destination '" + destFile + "' are the same");
        }
        File parentFile = destFile.getParentFile();
        if (parentFile != null) {
            if (!parentFile.mkdirs() && !parentFile.isDirectory()) {
                throw new IOException("Destination '" + parentFile + "' directory cannot be created");
            }
        }
        if (destFile.exists() && destFile.canWrite() == false) {
            throw new IOException("Destination '" + destFile + "' exists but is read-only");
        }
        doCopyFile(srcFile, destFile, preserveFileDate);
    }
    
    /**
     * Writes a String to a file creating the file if it does not exist using the default encoding for the VM.
     * 
     * @param file  the file to write
     * @param data  the content to write to the file
     * @throws IOException in case of an I/O error
     */
    public static void writeStringToFile(File file, String data) throws IOException {
        writeStringToFile(file, data, Charset.defaultCharset(), false);
    }
    
    /**
     * Writes a String to a file creating the file if it does not exist.
     *
     * @param file  the file to write
     * @param data  the content to write to the file
     * @param encoding  the encoding to use, {@code null} means platform default
     * @param append if {@code true}, then the String will be added to the
     * end of the file rather than overwriting
     * @throws IOException in case of an I/O error
     * @since 2.3
     */
    public static void writeStringToFile(File file, String data, Charset encoding, boolean append) throws IOException {
        OutputStream out = null;
        try {
            out = openOutputStream(file, append);
            IOUtils.write(data, out, encoding);
            out.close(); // don't swallow close Exception if copy completes normally
        } finally {
            IOUtils.closeSilently(out);
        }
    }
    
    /**
     * Deletes a directory recursively. 
     * 
     * 暂时忽略传入的是Symbolic Link这个可能性
     *
     * @param directory  directory to delete
     * @throws IOException in case deletion is unsuccessful
     */
    public static void deleteDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        cleanDirectory(directory);

        if (!directory.delete()) {
            String message =
                "Unable to delete directory " + directory + ".";
            throw new IOException(message);
        }
    }
    
    /**
     * Cleans a directory without deleting it.
     *
     * @param directory directory to clean
     * @throws IOException in case cleaning is unsuccessful
     */
    public static void cleanDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        File[] files = directory.listFiles();
        if (files == null) {  // null if security restricted
            throw new IOException("Failed to list contents of " + directory);
        }

        IOException exception = null;
        for (File file : files) {
            try {
                forceDelete(file);
            } catch (IOException ioe) {
                exception = ioe;
            }
        }

        if (null != exception) {
            throw exception;
        }
    }
    
    /**
     * Deletes a file. If file is a directory, delete it and all sub-directories.
     * <p>
     * The difference between File.delete() and this method are:
     * <ul>
     * <li>A directory to be deleted does not have to be empty.</li>
     * <li>You get exceptions when a file or directory cannot be deleted.
     *      (java.io.File methods returns a boolean)</li>
     * </ul>
     *
     * @param file  file or directory to delete, must not be {@code null}
     * @throws NullPointerException if the directory is {@code null}
     * @throws FileNotFoundException if the file was not found
     * @throws IOException in case deletion is unsuccessful
     */
    public static void forceDelete(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            boolean filePresent = file.exists();
            if (!file.delete()) {
                if (!filePresent){
                    throw new FileNotFoundException("File does not exist: " + file);
                }
                String message =
                    "Unable to delete file: " + file;
                throw new IOException(message);
            }
        }
    }
    
    /**
     * Deletes a file, never throwing an exception. If file is a directory, delete it and all sub-directories.
     * <p>
     * The difference between File.delete() and this method are:
     * <ul>
     * <li>A directory to be deleted does not have to be empty.</li>
     * <li>No exceptions are thrown when a file or directory cannot be deleted.</li>
     * </ul>
     *
     * @param file  file or directory to delete, can be {@code null}
     * @return {@code true} if the file or directory was deleted, otherwise
     * {@code false}
     *
     * @since 1.4
     */
    public static boolean deleteQuietly(File file) {
        if (file == null) {
            return false;
        }
        try {
            if (file.isDirectory()) {
                cleanDirectory(file);
            }
        } catch (Exception ignored) {
        }

        try {
            return file.delete();
        } catch (Exception ignored) {
            return false;
        }
    }
    
    // =====================================================================
    // ===============================Android相关==============================
    // =====================================================================
    
    // ************************************************Internal Storage****************************************
    /**
     * 在手机内部存储的/data/data/包名/目录下新建一个文件夹
     */
    public static File createOrGetDirectoryInInternalStorage(Context context, String directoryName) {
    	if (TextUtils.isEmpty(directoryName) || context == null) return null;
    	return context.getDir(directoryName, Context.MODE_PRIVATE);
    }
    
    /**
     * 在手机内部存储的/data/data/包名/目录下删除一个文件夹
     */
    public static void deleteDirectoryInInternalStorage(Context context, String directoryName) {
    	if (TextUtils.isEmpty(directoryName) || context == null) return;
    	File temp = createOrGetDirectoryInInternalStorage(context, directoryName);
    	if (temp != null && temp.exists()) {
			try {
				deleteDirectory(temp);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
    
    /**
     * Create a file on internal storage. May return null if exception happens.
     */
    public static FileOutputStream createFileOnInternalStorage(Context context, String name) {
    	FileOutputStream result = null;
    	try {
    		result = context.openFileOutput(name, Context.MODE_PRIVATE);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	
    	return result;
    }
    
    /**
     * Read a file on internal storage. May return null if exception happens.
     */
    public static FileInputStream getFileOnInternalStorage(Context context, String name) {
    	FileInputStream result = null;
    	
    	try {
    		result = context.openFileInput(name);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	
    	return result;
    }
    
    public static boolean deleteFileOnInternalStorage(Context context, String name) {
    	return context.deleteFile(name);
    }
    
    
    public static void getInternalStorageFreeSpace() {
		
	}
    
    // ************************************************External Storage****************************************
    /**
     * ExternalStorage是否可写
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * ExternalStorage是否可读
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
            Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
    
    /**
     * 获取外部存储的根目录的绝对路径，如果外部存储不可读，就会返回null.
     * @return
     */
    public static String getExternalStorageRootAbsolutePath() {
		if (!isExternalStorageReadable()) return null;
		else {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		}
	}
    
    /**
     * Provide a relative path on external storage, return a full path.
     */
    public static String getFullPathOnExternalStorage(String relativePath) {
    	String path = getExternalStorageRootAbsolutePath();
    	path = path + File.separator + relativePath;
    	return path;
    }
    
    /**
     * 在外部存储根目录创建或者获取一个文件夹，如果失败了会返回null
     */
    public static File createOrGetDirectoryInSDCradRoot(String relativeDirectoryName) {
    	File target = null;
    	
    	if (TextUtils.isEmpty(relativeDirectoryName)) return target;
    	
    	if (!isExternalStorageWritable()) return target;
    	
    	target = new File(getFullPathOnExternalStorage(relativeDirectoryName));
    	
    	if (target.exists()) {
			return target;
		}
    	
    	// 如果创建文件夹失败了，就返回null
    	if (!target.mkdir()) {
			target = null;
		}
    	
    	return target;
    }
    
    /**
     * Try to delete a directory on external storage, return the result.
     */
    public static boolean deleteDirectoryOnExternalStorage(String relativePath) {
    	if (TextUtils.isEmpty(relativePath)) return false;
    	
    	if (!isExternalStorageWritable()) return false;
    	
    	File tarFile = new File(getFullPathOnExternalStorage(relativePath));
    	try {
			deleteDirectory(tarFile);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
    	
    	return true;
    }
    
    /**
     * Try to create a file on external strorage, return null if any failure happens.
     */
    public static File getOrCreateFileOnExternalStorage(String relativePath) {
    	File result = null;
    	
    	if (TextUtils.isEmpty(relativePath)) return result;
    	
    	if (!isExternalStorageWritable()) return result;
    	
    	result = new File(getFullPathOnExternalStorage(relativePath));
    	
    	if (result.exists()) {
			return result;
		}
    	
    	// In case the pass-in path is illegal.
    	try {
			result.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			result = null;
		}
    	
    	return result;
    }
    
    public static boolean deleteFileOnExternalStorage(String relativePath) {
    	if (TextUtils.isEmpty(relativePath)) return false;
    	
    	if (!isExternalStorageWritable()) return false;
    	
    	File target = new File(getFullPathOnExternalStorage(relativePath));
    	
    	return target.delete();
    }

    
    // =====================================================================
    // =====================================================================
    // =====================================================================
    
    

}
