package org.mcteam.creativegates.listeners;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.mcteam.creativegates.Gate;
import org.mcteam.creativegates.P;


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
		
		Gate gate = p.getGateFromFrameBlock(event.getBlock());
		if (gate != null) {
			p.close(gate);
		}
	}
}
