package org.mcteam.creativegates;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

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
		try {
			ArrayList<String> lines = DiscUtil.readLines(file);
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
				}
			}
			return true;
		} catch (IOException e) {
			P.log(Level.WARNING, "Could not load "+fileName+". Please check your file permissions. The default config will be used.");
			return false;
		}
	}
	
	public static boolean save() {
		String content = "wand: "+wand.getId()+"\n"+"block: "+block.getId();
		try {
			DiscUtil.write(file, content);
			P.log("Creating default "+fileName);
			return true;
		} catch (IOException e) {
			P.log(Level.WARNING, "Could not create default "+fileName+". Please check your file permissions.");
			return false;
		}
	}
	
	
}
