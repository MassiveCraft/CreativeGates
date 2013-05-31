package com.massivecraft.creativegates.old;

import org.bukkit.Material;

public class Conf
{	
	public static int wand = Material.WATCH.getId();
	public static int block = Material.DIAMOND_BLOCK.getId();
	public static int maxarea = 200;
	public static boolean effects = true;
	
	public static transient Conf i = new Conf();
	
	public static void load()
	{
		CreativeGates.p.persist.loadOrSaveDefault(i, Conf.class);
	}
	public static void save()
	{
		CreativeGates.p.persist.save(i);
	}
}
