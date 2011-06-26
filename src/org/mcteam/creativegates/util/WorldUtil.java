package org.mcteam.creativegates.util;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.World.Environment;
import org.mcteam.creativegates.P;
import org.mcteam.creativegates.WorldEnv;

public class WorldUtil {
	public static boolean load(String name) {
		if (isWorldLoaded(name)) {
			return true;
		}
		
		if ( ! doesWorldExist(name)) {
			return false;
		}
		
		Environment env = WorldEnv.get(name);
		if (env == null) {
			P.log(Level.WARNING, "Failed to load world. Environment was unknown.");
			return false;
		}
		
		P.p.getServer().createWorld(name, env);
		return true;
	}
	
	public static boolean isWorldLoaded(String name) {
		return P.p.getServer().getWorld(name) != null;
	}
	
	public static boolean doesWorldExist(String name) {
		return new File(name, "level.dat").exists();
	}
}
