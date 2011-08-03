package com.massivecraft.creativegates;


import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;

import com.massivecraft.core.*;
import com.massivecraft.creativegates.listeners.*;


public class CreativeGates extends MassivePlugin {
	// Our single plugin instance
	public static CreativeGates p;
	
	// Items
	public Gates gates;
	
	// Listeners
	public PluginPlayerListener playerListener;
	public PluginBlockListener blockListener;
	public PluginBlockListenerMonitor blockListenerMonitor;
	public PluginEntityListener entityListener;
	public PluginGateListener gateListener;
	
	public CreativeGates() {
		p = this;
		
		this.playerListener = new PluginPlayerListener(this);
		this.blockListener = new PluginBlockListener(this);
		this.blockListenerMonitor = new PluginBlockListenerMonitor(this);
		this.entityListener = new PluginEntityListener(this);
		this.gateListener = new PluginGateListener(this);
	}

	public void onEnable() {
		if ( ! preEnable()) return;
		
		Conf.load();
		WorldEnv.load();
		
		gates = new Gates(this);
		gates.load();
		for (Gate gate : gates.getAll()) {
			gate.openOrDie();
		}
		
		// Register events
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_INTERACT, this.playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_BUCKET_FILL, this.playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_BUCKET_EMPTY, this.playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_MOVE, this.playerListener, Event.Priority.Monitor, this);
		
		pm.registerEvent(Event.Type.BLOCK_FROMTO, this.blockListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_PISTON_EXTEND, this.blockListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_PLACE, this.blockListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, this.blockListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, this.blockListenerMonitor, Event.Priority.Monitor, this);
		
		pm.registerEvent(Event.Type.ENTITY_EXPLODE, this.entityListener, Event.Priority.Normal, this);
		
		pm.registerEvent(Event.Type.CUSTOM_EVENT, this.gateListener, Event.Priority.Monitor, this);
		
		postEnable();
	}
	
	public void onDisable() {
		for (Gate gate : gates.getAll()) {
			gate.empty();
		}
	}
}
