package com.sanjay.ziputilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Class for Zip Utilities
 * 
 * @author sanjay_meena
 * 
 */
public class ZipUtilities {

	private static ZipUtilities instance;

	public static ZipUtilities getInstance() {
		if (instance == null) {
			instance = new ZipUtilities();
		}
		return instance;
	}

	/**
	 * Compress a file to a zip
	 * 
	 * @param filename
	 * @param zipFilename
	 */
	public void compressFile(String filename, String zipFilename) {
		File file = new File(filename);
		File zipFileName = new File(zipFilename);
		List<File> fileList = new ArrayList<File>();
		fileList.add(file);

		try {
			FileOutputStream fos = new FileOutputStream(zipFilename);
			ZipOutputStream zos = new ZipOutputStream(fos);

			for (File file1 : fileList) {
				if (!file1.isDirectory()) { // we only zip directory, not
											// directories
					addToZip(zipFileName, file, zos);
				}
			}

			zos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Compress a given directory recursively and store the zip in the provided
	 * directory name
	 * 
	 * @param directoryToZip
	 */
	public void compressDirectory(String fileDirectory,
			String savedZipFileDirectory) {
		File directoryToZip = new File(fileDirectory);

		List<File> fileList = new ArrayList<File>();
		try {
			System.out.println("---Getting references to all directory in: "
					+ directoryToZip.getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getAllFiles(directoryToZip, fileList);
		System.out.println("---Creating zip file");
		String folder = savedZipFileDirectory + File.separator
				+ directoryToZip.getName();

		writeZipFile(folder, directoryToZip, fileList);
		System.out.println("---Done");
	}

	/**
	 * Uncompress a zip file
	 * 
	 * @param zipFile
	 */
	public void unCompressZipFile(String zipFileName) {
		try {
			ZipFile zipFile = new ZipFile(zipFileName);
			Enumeration<?> enu = zipFile.entries();

			while (enu.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) enu.nextElement();

				String name = zipEntry.getName();
				long size = zipEntry.getSize();
				long compressedSize = zipEntry.getCompressedSize();
				System.out.printf(
						"name: %-20s | size: %6d | compressed size: %6d\n",
						name, size, compressedSize);

				File file = new File(name);
				if (name.endsWith("/")) {
					file.mkdirs();
					continue;
				}

				File parent = file.getParentFile();
				if (parent != null) {
					parent.mkdirs();
				}

				InputStream is = zipFile.getInputStream(zipEntry);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] bytes = new byte[1024];
				int length;
				while ((length = is.read(bytes)) >= 0) {
					fos.write(bytes, 0, length);
				}
				is.close();
				fos.close();

			}
			zipFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Read all the files recursively from the directory
	 * 
	 * @param dir
	 * @param fileList
	 */
	private void getAllFiles(File dir, List<File> fileList) {
		try {
			File[] files = dir.listFiles();
			for (File file : files) {
				fileList.add(file);
				if (file.isDirectory()) {
					System.out.println("directory:" + file.getCanonicalPath());
					getAllFiles(file, fileList);
				} else {
					System.out.println("     file:" + file.getCanonicalPath());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * See the contents of a zip file
	 */
	public void seeContentOfZipFile(String zipfile) {
		try {
			ZipFile zipFile = new ZipFile(zipfile);
			Enumeration<?> enu = zipFile.entries();
			while (enu.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) enu.nextElement();
				String name = zipEntry.getName();
				long size = zipEntry.getSize();
				long compressedSize = zipEntry.getCompressedSize();
				System.out.printf(
						"name: %-20s | size: %6d | compressed size: %6d\n",
						name, size, compressedSize);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the zip file
	 * 
	 * @param directoryToZip
	 * @param fileList
	 */
	private void writeZipFile(String folder, File directoryToZip,
			List<File> fileList) {

		try {
			FileOutputStream fos = new FileOutputStream(folder + ".zip");
			ZipOutputStream zos = new ZipOutputStream(fos);

			for (File file : fileList) {
				if (!file.isDirectory()) { // we only zip directory, not
											// directories
					addToZip(directoryToZip, file, zos);
				}
			}

			zos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add a file to the zip
	 * 
	 * @param zipfilename
	 * @param file
	 * @param zos
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void addToZip(File zipfilename, File file, ZipOutputStream zos)
			throws FileNotFoundException, IOException {

		FileInputStream fis = new FileInputStream(file);

		// we want the zipEntry's path to be a relative path that is relative
		// to the directory being zipped, so chop off the rest of the path
		String zipFilePath = file.getCanonicalPath().substring(
				zipfilename.getCanonicalPath().length() + 1,
				file.getCanonicalPath().length());
		System.out.println("Writing '" + zipFilePath + "' to zip file");
		ZipEntry zipEntry = new ZipEntry(zipFilePath);
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}

	/**
	 * Compress a given list of Files to the given zipped file name
	 * 
	 * @param fileList
	 * @param zipfileName
	 */

	public void compressFiles(List<File> fileList, String zipfileName) {

		File zip = new File(zipfileName);
		try {
			FileOutputStream fos = new FileOutputStream(zip);
			ZipOutputStream zos = new ZipOutputStream(fos);

			for (File file1 : fileList) {
				if (!file1.isDirectory()) { // we only zip directory, not
											// directories
					addToZip(zip, file1, zos);
				}
			}

			zos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
