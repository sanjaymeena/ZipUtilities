package com.sanjay.ziputilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *  
 * @author Sanjay_Meena
 *
 */
public class TestZipUtilities {

	static String directory = "testfiles";
	static String compress_this_directory = "compress_this_directory";
	String unzippedDirecotry = "uncompressed_directory";
	static String zippedFiles = "zipfolder";

	public static void main(String args[]) {

		// Compress a file to the given zip file name
		String zipfilename = "test.zip";
		String filename1 = "testfiles" + File.separator + "TestFile1.txt";
		String filename2 = "testfiles" + File.separator + "TestFile2.txt";

		File file1 = new File(filename1);
		File file2 = new File(filename2);
		List<File> fileList = new ArrayList<File>();
		fileList.add(file1);
		fileList.add(file2);

		/**
		 * Compress a list of Files to the given zip file name;
		 */
		System.out
				.println("****************************************************************");
		System.out.println("Compressing the given file list to the Zip file: "
				+ zipfilename);
		ZipUtilities.getInstance().compressFiles(fileList, zipfilename);
		System.out
				.println("****************************************************************");
		System.out.println("");

		// Compress content of a directory
		System.out.println("Compressing the directory : "
				+ compress_this_directory + "  to the Zip file:  "
				+ zippedFiles);

		ZipUtilities.getInstance().compressDirectory(compress_this_directory,
				zippedFiles);

		String directoryOfZipFile = zippedFiles + File.separator
				+ compress_this_directory + ".zip";
		System.out
				.println("****************************************************************");
		System.out.println("");

		// See the content of a zip file
		System.out.println("See the content of the zip file :"
				+ compress_this_directory + directoryOfZipFile);
		ZipUtilities.getInstance().seeContentOfZipFile(directoryOfZipFile);
		System.out
				.println("****************************************************************");
		System.out.println("");
		// UnCompress content of a zip file.
		System.out.println("UnCompress the content of a zip file :"
				+ compress_this_directory + directoryOfZipFile);
		ZipUtilities.getInstance().unCompressZipFile(directoryOfZipFile);
		System.out
				.println("****************************************************************");

		System.out.println("");
	}
}
