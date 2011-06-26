package org.mcteam.creativegates;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcteam.creativegates.listeners.*;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;


public class P extends JavaPlugin {
	// Our single plugin instance
	public static P p;
	
	// Listeners
	public PluginPlayerListener playerListener;
	public PluginBlockListener blockListener;
	public PluginBlockListenerMonitor blockListenerMonitor;
	public PluginEntityListener entityListener;
	
	// Permission handler
	public static PermissionHandler permissionHandler;
	
	public P() {
		p = this;
		
		this.playerListener = new PluginPlayerListener(this);
		this.blockListener = new PluginBlockListener(this);
		this.blockListenerMonitor = new PluginBlockListenerMonitor(this);
		this.entityListener = new PluginEntityListener(this);
	}

	public void onEnable() {
		log("===== ENABLE START");
		// Ensure the data folder exists!
		this.getDataFolder().mkdirs();
		
		// Load gates from disc
		Conf.load();
		WorldEnv.load();
		Gates.load();
		
		// Register events
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_INTERACT, this.playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_BUCKET_FILL, this.playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_BUCKET_EMPTY, this.playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_MOVE, this.playerListener, Event.Priority.Monitor, this);
		
		pm.registerEvent(Event.Type.BLOCK_FROMTO, this.blockListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_PLACE, this.blockListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, this.blockListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, this.blockListenerMonitor, Event.Priority.Monitor, this);
		
		pm.registerEvent(Event.Type.ENTITY_EXPLODE, this.entityListener, Event.Priority.Normal, this);
		
		// Setup Permissions
		setupPermissions();
		
		log("===== ENABLE END");
	}
	
	public void onDisable() {
		for (Gate gate : Gates.gates) {
			gate.empty();
		}
	}
	
	// -------------------------------------------- //
	// Permissions integration
	// -------------------------------------------- //
	
	private void setupPermissions() {
		if (permissionHandler != null) {
			return;
		}
		
		Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");
		
		if (permissionsPlugin == null) {
			log("Permission system not detected, defaulting to OP");
			return;
		}
		
		permissionHandler = ((Permissions) permissionsPlugin).getHandler();
		log("Found and will use plugin "+((Permissions)permissionsPlugin).getDescription().getFullName());
	}
	
	// -------------------------------------------- //
	// Logging
	// -------------------------------------------- //
	public static void log(Object o) {
		log(Level.INFO, o);
	}
	
	public static void log(Level level, Object o) {
		Logger.getLogger("Minecraft").log(level, "["+p.getDescription().getFullName()+"] "+o);
	}

}
