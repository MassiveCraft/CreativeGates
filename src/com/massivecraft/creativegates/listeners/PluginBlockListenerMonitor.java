package com.massivecraft.creativegates.listeners;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;

import com.massivecraft.creativegates.Gate;
import com.massivecraft.creativegates.CreativeGates;


public class PluginBlockListenerMonitor extends BlockListener {
	CreativeGates p;
	
	public PluginBlockListenerMonitor(CreativeGates p) {
		this.p = p;
	}
	
	// Destroy the gate if the frame breaks
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		Gate gate = p.gates.findFromFrame(event.getBlock());
		if (gate != null) {
			gate.close();
		}
	}
}
