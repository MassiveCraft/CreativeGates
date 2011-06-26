package org.mcteam.creativegates.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;
import org.mcteam.creativegates.Gates;
import org.mcteam.creativegates.P;

public class PluginEntityListener extends EntityListener {
	P p;
	
	public PluginEntityListener(P p) {
		this.p = p;
	}
	
	// Gates can not be destroyed by explosions
	@Override
	public void onEntityExplode(EntityExplodeEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		for (Block block : event.blockList()) {
			if (Gates.findFrom(block) != null) {
				event.setCancelled(true);
				return;
			}
		}
	}
}
