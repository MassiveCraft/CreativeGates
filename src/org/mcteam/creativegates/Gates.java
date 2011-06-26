package org.mcteam.creativegates;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.mcteam.creativegates.util.DiscUtil;
import org.mcteam.creativegates.util.TextUtil;

public class Gates {
	// The file to which we persist the gates.
	public static File file;
	
	// Our gates
	public static TreeSet<Gate> gates;
	
	static {
		// Find the data folder
		file = new File(P.p.getDataFolder(), "gates.txt");
		
		// As you can see I use a custom comparator here.
		// I sort on the gate location.
		// This is used to find the target gate. Which other gate a gate is linked to. 
		gates = new TreeSet<Gate>(new Comparator<Gate>() {
			@Override
			public int compare(Gate me, Gate you) {
				return me.sourceCoord.toString().compareTo(you.sourceCoord.toString());
			}
        });
	}
	
	// -------------------------------------------- //
	// Find gate from block or coord.
	// -------------------------------------------- //
	
	public static Gate findFromContent(WorldCoord coord) {
		for (Gate gate : gates) {
			if (gate.contentCoords.contains(coord)) {
				return gate;
			}
		}
		return null;
	}
	
	public static Gate findFromContent(Block block) {
		return findFromContent(new WorldCoord(block));
	}
	
	public static Gate findFromFrame(WorldCoord coord) {
		for (Gate gate : gates) {
			if (gate.frameCoords.contains(coord)) {
				return gate;
			}
		}
		return null;
	}
	
	public static Gate findFromFrame(Block block) {
		return findFromFrame(new WorldCoord(block));
	}
	
	public static Gate findFrom(WorldCoord coord) {
		Gate gate; 
		
		gate = findFromContent(coord);
		if (gate != null) {
			return gate;
		}
		
		return findFromFrame(coord);
	}
	
	public static Gate findFrom(Block block) {
		return findFrom(new WorldCoord(block));
	}
	
	// -------------------------------------------- //
	// Gate Factory
	// -------------------------------------------- //
	
	public static Gate open(WorldCoord sourceCoord) {
		try {
			return new Gate(sourceCoord);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static Gate open(WorldCoord sourceCoord, Player player) {
		Gate gate = open(sourceCoord);
		
		if (gate != null) {
			gate.informPlayer(player);
		} else {
			player.sendMessage(Conf.colorDefault+"There is no frame...");
		}
		
		return gate;
	}
	
	public static void close(Gate gate) {
		gate.empty();
		gates.remove(gate);
		save();
	}
	
	// -------------------------------------------- //
	// Save and Load
	// -------------------------------------------- //
	
	public static boolean save() {
		ArrayList<String> strings = new ArrayList<String>();
		
		for (Gate gate : gates) {
			strings.add(gate.sourceCoord.toString());
		}
		
		String content = TextUtil.implode(strings, "\n");
		
		return DiscUtil.writeCatch(file, content);
	}
	
	public static boolean load() {
		if ( ! file.exists()) {
			return true;
		}
		
		gates.clear();
		
		ArrayList<String> lines = DiscUtil.readLinesCatch(file);
		if (lines == null) {
			return false;
		}
		
		for (String line : lines) {
			try {
				open(new WorldCoord(line));
			} catch (Exception e) {
				
			}
		}
		return true;
	}
}
