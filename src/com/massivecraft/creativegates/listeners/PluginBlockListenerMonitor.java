package com.massivecraft.creativegates.listeners;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;

import com.massivecraft.creativegates.Gate;
import com.massivecraft.creativegates.Gates;
import com.massivecraft.creativegates.P;


public class PluginBlockListenerMonitor extends BlockListener {
	P p;
	
	public PluginBlockListenerMonitor(P p) {
		this.p = p;
	}
	
	// Destroy the gate if the frame breaks
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		Gate gate = Gates.findFromFrame(event.getBlock());
		if (gate != null) {
			Gates.close(gate);
		}
	}
}
