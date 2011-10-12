package com.massivecraft.creativegates;

import org.bukkit.event.Event;

import com.massivecraft.creativegates.listeners.*;
import com.massivecraft.creativegates.zcore.*;

public class CreativeGates extends MPlugin
{
	// Our single plugin instance
	public static CreativeGates p;
	
	// Listeners
	public PluginPlayerListener playerListener;
	public PluginBlockListener blockListener;
	public PluginBlockListenerMonitor blockListenerMonitor;
	public PluginEntityListener entityListener;
	public PluginGateListener gateListener;
	
	public CreativeGates()
	{
		p = this;
		
		this.playerListener = new PluginPlayerListener();
		this.blockListener = new PluginBlockListener();
		this.blockListenerMonitor = new PluginBlockListenerMonitor();
		this.entityListener = new PluginEntityListener();
		this.gateListener = new PluginGateListener();
	}

	public void onEnable()
	{
		if ( ! preEnable()) return;
		
		// TODO fix config auto update routine... ?
		Conf.load();
		
		Gates.i.loadFromDisc();
		Gates.i.openAllOrDetach();
		
		// Register events
		this.registerEvent(Event.Type.PLAYER_INTERACT, this.playerListener, Event.Priority.Normal);
		this.registerEvent(Event.Type.PLAYER_BUCKET_FILL, this.playerListener, Event.Priority.Normal);
		this.registerEvent(Event.Type.PLAYER_BUCKET_EMPTY, this.playerListener, Event.Priority.Normal);
		this.registerEvent(Event.Type.PLAYER_MOVE, this.playerListener, Event.Priority.Monitor);
		this.registerEvent(Event.Type.BLOCK_FROMTO, this.blockListener, Event.Priority.Normal);
		this.registerEvent(Event.Type.BLOCK_PISTON_EXTEND, this.blockListener, Event.Priority.Normal);
		this.registerEvent(Event.Type.BLOCK_PLACE, this.blockListener, Event.Priority.Normal);
		this.registerEvent(Event.Type.BLOCK_BREAK, this.blockListener, Event.Priority.Normal);
		this.registerEvent(Event.Type.BLOCK_BREAK, this.blockListenerMonitor, Event.Priority.Monitor);
		this.registerEvent(Event.Type.ENTITY_EXPLODE, this.entityListener, Event.Priority.Normal);
		this.registerEvent(Event.Type.CUSTOM_EVENT, this.gateListener, Event.Priority.Monitor);
		
		postEnable();
	}
	
	public void onDisable()
	{
		Gates.i.emptyAll();
		super.onDisable();
	}
}
