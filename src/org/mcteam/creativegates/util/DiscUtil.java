package org.mcteam.creativegates.util;

import java.io.*;
import java.util.ArrayList;

/**
 * Harddisc related methods such as read and write.
 */
public class DiscUtil {
	/**
	 * Convenience function for writing a string to a file.
	 */
	public static void write(File file, String content) throws IOException {
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF8"));
		out.write(content);
		out.close();
	}
	
	/**
	 * Convenience function for reading a file as a string.
	 */
	public static ArrayList<String> readLines(File file) throws IOException {
		ArrayList<String> lines = new ArrayList<String>();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		 
		String line;
		while ((line = in.readLine()) != null) {
			lines.add(line);
		}
		
		return lines;
	}
}
