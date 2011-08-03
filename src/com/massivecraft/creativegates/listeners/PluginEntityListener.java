package com.massivecraft.creativegates.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;

import com.massivecraft.creativegates.CreativeGates;

public class PluginEntityListener extends EntityListener {
	CreativeGates p;
	
	public PluginEntityListener(CreativeGates p) {
		this.p = p;
	}
	
	// Gates can not be destroyed by explosions
	@Override
	public void onEntityExplode(EntityExplodeEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		for (Block block : event.blockList()) {
			if (p.gates.findFrom(block) != null) {
				event.setCancelled(true);
				return;
			}
		}
	}
}
