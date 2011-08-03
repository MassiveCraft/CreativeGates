package com.massivecraft.creativegates;

import org.bukkit.Material;

public class Conf {
	// Change-able: False
	// public static ChatColor colorDefault = ChatColor.AQUA;
	// public static ChatColor colorHighlight = ChatColor.GREEN;
	
	public static Material wand = Material.WATCH;
	public static Material block = Material.DIAMOND_BLOCK;
	public static int maxarea = 200;
	
	public static void load() {
		CreativeGates.p.persist.loadOrSaveDefault(new Conf(), Conf.class);
	}
	
	public static void save() {
		CreativeGates.p.persist.save(new Conf());
	}
}
