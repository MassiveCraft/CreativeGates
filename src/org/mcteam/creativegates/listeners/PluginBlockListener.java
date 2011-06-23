package org.mcteam.creativegates.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.mcteam.creativegates.Gate;
import org.mcteam.creativegates.P;


public class PluginBlockListener extends BlockListener {
	P p;
	
	public PluginBlockListener(P p) {
		this.p = p;
	}
	
	// The purpose is to stop the water from falling
	public void onBlockFromTo(BlockFromToEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		Block blockFrom = event.getBlock();
		boolean isWater = blockFrom.getTypeId() == 8 || blockFrom.getTypeId() == 9;
		
		if ( ! isWater) {
			return;
		}
		
		if (p.getGateFromContentBlock(blockFrom) != null) {
			event.setCancelled(true);
		}
	}
	
	// The gate content is invulnerable
	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		Gate gate = p.getGateFromContentBlock(event.getBlock()); 
		if (gate != null) {
			event.setCancelled(true);
		}
	}
}
