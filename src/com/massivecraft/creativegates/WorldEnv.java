package com.massivecraft.creativegates;

import java.util.*;

import org.bukkit.World;
import org.bukkit.World.Environment;

// This is a big pice of ugly crap to logg and remember the world environments.
// I hope bukkit will fix this soon.

public class WorldEnv {
	// Our world environments
	private static Map<String, Environment> worldenv = new HashMap<String, Environment>();
	
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
		CreativeGates.p.persist.save(new WorldEnv());
	}
	
	public static void set(World world) {
		 set(world.getName(), world.getEnvironment());
	}
	
	public static void load() {
		CreativeGates.p.persist.loadOrSaveDefault(new WorldEnv(), WorldEnv.class);
	}
	
	public static void save() {
		CreativeGates.p.persist.save(new WorldEnv());
	}
}