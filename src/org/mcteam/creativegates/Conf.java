package org.mcteam.creativegates;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.mcteam.creativegates.util.DiscUtil;
import org.mcteam.creativegates.util.TextUtil;

public class Conf {
	// Change-able: False
	public static ChatColor colorDefault = ChatColor.AQUA;
	public static ChatColor colorHighlight = ChatColor.GREEN;
	
	// Change-able: True
	public static Material wand = Material.WATCH;
	public static Material block = Material.DIAMOND_BLOCK;
	public static int maxarea = 200;
	
	// Info
	public static String fileName = "conf.txt";
	public static File file;
	
	static {
		file = new File (P.p.getDataFolder(), fileName);
	}
	
	// -------------------------------------------- //
	// Save and Load
	// -------------------------------------------- //
	public static boolean load() {
		if ( ! file.exists()) {
			save();
			return true;
		}
		
		P.log("Loading "+fileName+" ...");
		
		ArrayList<String> lines = DiscUtil.readLinesCatch(file);
		if (lines == null) {
			return false;
		}
		
		for (String line : lines) {
			String[] parts = line.trim().split("[\\s\\:]+");
			String key = parts[0];
			String val = parts[1];
			if (key.equalsIgnoreCase("wand")) {
				wand = Material.getMaterial(Integer.parseInt(val));
				P.log("Wand: "+TextUtil.getMaterialName(wand));
			} else if (key.equalsIgnoreCase("block")) {
				block = Material.getMaterial(Integer.parseInt(val));
				P.log("Block: "+TextUtil.getMaterialName(block));
			} else if (key.equalsIgnoreCase("maxarea")) {
				maxarea = Integer.parseInt(val);
				P.log("Max Area: "+maxarea);
			} 
		}
		return true;
	}
	
	public static boolean save() {
		P.log("Creating default "+fileName);
		
		String content = "wand: "+wand.getId()+"\n"+"block: "+block.getId()+"\n"+"maxarea: "+maxarea;
		
		return DiscUtil.writeCatch(file, content);
	}
	
	
}
