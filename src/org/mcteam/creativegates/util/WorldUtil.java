package org.mcteam.creativegates.util;

import java.io.File;

import org.bukkit.World.Environment;
import org.mcteam.creativegates.P;

public class WorldUtil {
	public static boolean load(String name) {
		if (P.p.getServer().getWorld(name) != null) {
			return true;
		}
		
		if ( ! doesExist(name)) {
			return false;
		}
		
		P.p.getServer().createWorld(name, Environment.NORMAL);
		return true;
	}
	
	public static boolean doesExist(String name) {
		File baseFolder = new File(".");
		File worldFolder = new File(baseFolder, name);
		File datFile = new File(worldFolder, "level.dat");
		return datFile.exists();
	}
}
