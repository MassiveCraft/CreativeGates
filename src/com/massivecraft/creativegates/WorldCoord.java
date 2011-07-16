package com.massivecraft.creativegates;

import org.bukkit.Location;
import org.bukkit.block.Block;

import com.massivecraft.creativegates.util.WorldUtil;


public class WorldCoord {
	
	public String worldName = "world";
	public int x = 0;
	public int y = 0;
	public int z = 0;
	
	//----------------------------------------------//
	// Constructors
	//----------------------------------------------//
	
	public WorldCoord(String worldName, int x, int y, int z) {
		this.worldName = worldName;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public WorldCoord(Location location) {
		this(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}
	
	public WorldCoord(Block block) {
		this(block.getLocation());
	}
	
	public WorldCoord(String str) throws Exception {
		String worldName = null;
		int x = 0;
		int y = 0;
		int z = 0;
		
		String[] parts = str.split(",");
		
		try {
			worldName = parts[0];
			x = Integer.parseInt(parts[1]);
			y = Integer.parseInt(parts[2]);
			z = Integer.parseInt(parts[3]);
		} catch (Exception e) {
			throw new Exception("Failed to parse this invalid WorldCoord: " + str);
		}
		
		this.worldName = worldName;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	//----------------------------------------------//
	// Converters
	//----------------------------------------------//
	
	public String toString() {
		return worldName+","+x+","+y+","+z;
	}
	
	public Block getBlock() {
		if ( ! WorldUtil.load(worldName)) {
			return null;
		}
		return P.p.getServer().getWorld(worldName).getBlockAt(x, y, z);
	}
	
	//----------------------------------------------//
	// Comparison
	//----------------------------------------------//
	
	public int hashCode() {
		int hash = 3;
        hash = 19 * hash + (this.worldName != null ? this.worldName.hashCode() : 0);
        hash = 19 * hash + this.x;
        hash = 19 * hash + this.y;
        hash = 19 * hash + this.z;
        return hash;
	};
	
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof WorldCoord))
			return false;

		WorldCoord that = (WorldCoord) obj;
		return this.x == that.x && this.y == that.y && this.z == that.z && ( this.worldName==null ? that.worldName==null : this.worldName.equals(that.worldName) );
	}

}