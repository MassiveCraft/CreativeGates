package com.massivecraft.creativegates.util;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.World.Environment;

import com.massivecraft.creativegates.CreativeGates;
import com.massivecraft.creativegates.WorldEnv;

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
			CreativeGates.p.log(Level.WARNING, "Failed to load world. Environment was unknown.");
			return false;
		}
		
		Bukkit.getServer().createWorld(name, env);
		return true;
	}
	
	public static boolean isWorldLoaded(String name) {
		return Bukkit.getServer().getWorld(name) != null;
	}
	
	public static boolean doesWorldExist(String name) {
		return new File(name, "level.dat").exists();
	}
}
