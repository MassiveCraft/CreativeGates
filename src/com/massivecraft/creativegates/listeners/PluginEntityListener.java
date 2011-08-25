package com.massivecraft.creativegates.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;

import com.massivecraft.creativegates.CreativeGates;
import com.massivecraft.creativegates.Gates;

public class PluginEntityListener extends EntityListener {
	
	CreativeGates p = CreativeGates.p;
	
	// Gates can not be destroyed by explosions
	@Override
	public void onEntityExplode(EntityExplodeEvent event)
	{
		if (event.isCancelled()) return;
		
		for (Block block : event.blockList())
		{
			if (Gates.i.findFrom(block) != null)
			{
				event.setCancelled(true);
				return;
			}
		}
	}
}
