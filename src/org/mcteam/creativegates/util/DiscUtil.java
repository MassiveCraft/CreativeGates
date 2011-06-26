package org.mcteam.creativegates.util;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;

import org.mcteam.creativegates.P;

/**
 * Harddisc related methods such as read and write.
 */
public class DiscUtil {
	public static void write(File file, String content) throws IOException {
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF8"));
		out.write(content);
		out.close();
	}
	
	public static ArrayList<String> readLines(File file) throws IOException {
		ArrayList<String> lines = new ArrayList<String>();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		 
		String line;
		while ((line = in.readLine()) != null) {
			lines.add(line);
		}
		
		return lines;
	}
	
	public static boolean writeCatch(File file, String content) {
		try {
			write(file, content);
			return true;
		} catch (Exception e) {
			P.log(Level.SEVERE, "Could not save "+file+". Please check your file permissions.");
			return false;
		}
	}
	
	public static ArrayList<String> readLinesCatch(File file) {
		ArrayList<String> lines = null;
		try {
			lines = readLines(file);
		} catch (IOException e) {
			P.log(Level.SEVERE, "Could not load "+file+". Please check your file permissions.");
		}
		return lines;
	}
}
