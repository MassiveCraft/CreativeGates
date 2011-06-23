package org.mcteam.creativegates.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;
import org.mcteam.creativegates.Gate;
import org.mcteam.creativegates.P;

public class PluginEntityListenerMonitor extends EntityListener {
	P p;
	
	public PluginEntityListenerMonitor(P p) {
		this.p = p;
	}
	
	// Destroy the gate if the frame breaks
	@Override
	public void onEntityExplode(EntityExplodeEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		for (Block block :  event.blockList()) {
			Gate gate = p.getGateFromFrameBlock(block);
			if (gate != null) {
				P.p.close(gate);
				return;
			}
		}
	}
}
