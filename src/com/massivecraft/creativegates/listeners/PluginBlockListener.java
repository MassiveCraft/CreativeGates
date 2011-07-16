package com.massivecraft.creativegates.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.massivecraft.creativegates.Gate;
import com.massivecraft.creativegates.Gates;
import com.massivecraft.creativegates.P;
import com.massivecraft.creativegates.Permission;


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
		
		if (Gates.findFromContent(blockFrom) != null) {
			event.setCancelled(true);
		}
	}
	
	// The gate content is invulnerable
	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		Gate gate = Gates.findFromContent(event.getBlock()); 
		if (gate != null) {
			event.setCancelled(true);
		}
	}
	
	// Is the player allowed to destroy gates?
	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		Gate gate = Gates.findFromFrame(event.getBlock());
		if (gate == null) {
			return;
		}
		
		// A player is attempting to destroy a gate. Can he?
		if ( ! Permission.DESTROY.test(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
}
