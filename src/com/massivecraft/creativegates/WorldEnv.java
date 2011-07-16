package com.massivecraft.creativegates;

import java.io.File;
import java.util.*;

import org.bukkit.World;
import org.bukkit.World.Environment;

import com.massivecraft.creativegates.util.DiscUtil;
import com.massivecraft.creativegates.util.TextUtil;

// This is a big pice of ugly crap to logg and remember the world environments.
// I hope bukkit will fix this soon.

public class WorldEnv {
	// Our world environments
	public static HashMap<String, Environment> worldenv;
	
	// Info
	public static String fileName = "worldenv.txt";
	public static File file;
	
	static {
		file = new File (P.p.getDataFolder(), fileName);
		worldenv = new HashMap<String, Environment>();
	}
	
	// -------------------------------------------- //
	// Get and Set
	// -------------------------------------------- //
	
	public static Environment get(String name) {
		return worldenv.get(name);
	}
	
	public static Environment get(World world) {
		return worldenv.get(world.getName()); 
	}
	
	public static void set(String name, Environment env) {
		if (worldenv.get(name) == env) {
			return;
		}
		worldenv.put(name, env);
		save();
	}
	
	public static void set(World world) {
		 set(world.getName(), world.getEnvironment());
	}
	
	// -------------------------------------------- //
	// Save and Load
	// -------------------------------------------- //
	public static boolean load() {
		if ( ! file.exists()) {
			return true;
		}
		
		ArrayList<String> lines = DiscUtil.readLinesCatch(file);
		if (lines == null) {
			return false;
		}
		
		for (String line : lines) {
			String[] parts = line.trim().split("[\\s\\:]+");
			worldenv.put(parts[0], Environment.valueOf(parts[1]));
		}
		
		return true;
	}
	
	public static boolean save() {
		ArrayList<String> strings = new ArrayList<String>();
		
		for (String name : worldenv.keySet()) {
			strings.add(name+": "+worldenv.get(name));
		}
		
		String content = TextUtil.implode(strings, "\n");
		
		return DiscUtil.writeCatch(file, content);
	}
}
