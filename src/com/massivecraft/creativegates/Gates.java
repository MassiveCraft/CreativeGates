package com.massivecraft.creativegates;

import java.lang.reflect.Type;
import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.google.gson.reflect.TypeToken;
import com.massivecraft.core.*;

public class Gates extends GsonItems<Gate> {
	
	CreativeGates p;
	
	public Gates(CreativeGates p) {
		super(p, Gate.class);
		this.p = p;
	}

	@Override
	protected Map<String, Gate> contentToMap(String content) {
		Type type = new TypeToken<Map<String, Gate>>(){}.getType();
		return gson.fromJson(content, type);
	}
	
	
	// -------------------------------------------- //
	// Find gate from block or coord.
	// -------------------------------------------- //
	
	public Gate findFromContent(WorldCoord coord) {
		for (Gate gate : this.getAll()) {
			if (gate.contentCoords.contains(coord)) {
				return gate;
			}
		}
		return null;
	}
	
	public Gate findFromContent(Block block) {
		return findFromContent(new WorldCoord(block));
	}
	
	public Gate findFromFrame(WorldCoord coord) {
		for (Gate gate : this.getAll()) {
			if (gate.frameCoords.contains(coord)) {
				return gate;
			}
		}
		return null;
	}
	
	public Gate findFromFrame(Block block) {
		return findFromFrame(new WorldCoord(block));
	}
	
	public Gate findFrom(WorldCoord coord) {
		Gate gate; 
		
		gate = findFromContent(coord);
		if (gate != null) {
			return gate;
		}
		
		return findFromFrame(coord);
	}
	
	public Gate findFrom(Block block) {
		return findFrom(new WorldCoord(block));
	}

	// -------------------------------------------- //
	// Gate Factory
	// -------------------------------------------- //
	
	public Gate open(WorldCoord sourceCoord, Player player) {
		Gate gate = this.create();
		gate.sourceCoord = sourceCoord;
		p.log("sourceCoord: "+sourceCoord);
		if (gate.openOrDie(player)) {
			return gate;
		}
		return null;
	}
	
	public Gate open(WorldCoord sourceCoord) {
		return this.open(sourceCoord, null);
	}
}
