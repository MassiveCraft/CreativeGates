package org.mcteam.creativegates;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcteam.creativegates.listeners.*;
import org.mcteam.creativegates.util.DiscUtil;
import org.mcteam.creativegates.util.TextUtil;


public class P extends JavaPlugin {
	// Our single plugin instance
	public static P p;
	
	// The file to which we persist the gates.
	public File file;
	
	// Our gates
	public TreeSet<Gate> gates;
	
	// Listeners
	public PluginPlayerListener playerListener;
	public PluginBlockListener blockListener;
	public PluginBlockListenerMonitor blockListenerMonitor;
	public PluginEntityListenerMonitor entityListenerMonitor;
	
	// Some static config
	public static ChatColor colorDefault = ChatColor.AQUA;
	public static ChatColor colorHighlight = ChatColor.GREEN;
	
	
	public P() {
		p = this;
		
		this.gates = new TreeSet<Gate>(new Comparator<Gate>() {
			@Override
			public int compare(Gate me, Gate you) {
				return me.sourceCoord.toString().compareTo(you.sourceCoord.toString());
			}
        });
		
		this.playerListener = new PluginPlayerListener(this);
		this.blockListener = new PluginBlockListener(this);
		this.blockListenerMonitor = new PluginBlockListenerMonitor(this);
		this.entityListenerMonitor = new PluginEntityListenerMonitor(this);
	}

	public void onEnable() {
		// Ensure the data folder exists!
		this.getDataFolder().mkdirs();
		
		// Find the data folder
		this.file = new File(this.getDataFolder(), "gates.txt");
		
		// Load gates from disc
		this.load();
		
		// Register events
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_INTERACT, this.playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_BUCKET_FILL, this.playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_BUCKET_EMPTY, this.playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_MOVE, this.playerListener, Event.Priority.Monitor, this);
		
		pm.registerEvent(Event.Type.BLOCK_FROMTO, this.blockListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_PLACE, this.blockListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, this.blockListenerMonitor, Event.Priority.Monitor, this);
		
		pm.registerEvent(Event.Type.ENTITY_EXPLODE, this.entityListenerMonitor, Event.Priority.High, this);
		
	}
	
	public void onDisable() {
		for (Gate gate : gates) {
			gate.empty();
		}
	}
	
	// -------------------------------------------- //
	// Get gate from block
	// -------------------------------------------- //
	
	public Gate getGateFromContentBlock(Block block) {
		for (Gate gate : gates) {
			if (gate.contentCoords.contains(new WorldCoord(block))) {
				return gate;
			}
		}
		return null;
	}
	
	public Gate getGateFromFrameBlock(Block block) {
		for (Gate gate : gates) {
			if (gate.frameCoords.contains(new WorldCoord(block))) {
				return gate;
			}
		}
		return null;
	}
	
	public Gate getGateFromBlock(Block block) {
		Gate gate; 
		
		gate = this.getGateFromContentBlock(block);
		if (gate != null) {
			return gate;
		}
		
		return this.getGateFromFrameBlock(block);
	}
	
	// -------------------------------------------- //
	// Gate Factory
	// -------------------------------------------- //
	
	public Gate open(WorldCoord sourceCoord) {
		try {
			return new Gate(sourceCoord);
		} catch (Exception e) {
		}
		
		return null;
	}
	
	public Gate open(WorldCoord sourceCoord, Player player) {
		Gate gate = this.open(sourceCoord);
		
		if (gate != null) {
			gate.informPlayer(player);
		} else {
			player.sendMessage(P.colorDefault+"There is no frame...");
		}
		
		return gate;
	}
	
	public void close(Gate gate) {
		gate.empty();
		gates.remove(gate);
		save();
	}
	
	// -------------------------------------------- //
	// Save and Load
	// -------------------------------------------- //
	
	public boolean save() {
		ArrayList<String> gates = new ArrayList<String>();
		
		for (Gate gate : this.gates) {
			gates.add(gate.sourceCoord.toString());
		}
		
		String content = TextUtil.implode(gates, "\n");
		
		try {
			DiscUtil.write(file, content);
			return true;
		} catch (Exception e) {
			log("Could not save. Check your file permissions.");
			return false;
		}
	}
	
	public boolean load() {
		if ( ! file.exists()) {
			return true;
		}
		
		gates.clear();
		
		try {
			ArrayList<String> strings = DiscUtil.readLines(file);
			for (String s : strings) {
				try {
					open(new WorldCoord(s));
				} catch (Exception e) {
					
				}
			}
			return true;
		} catch (Exception e) {
			log("Could not load. Check your file permissions.");
			return false;
		}
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
