package com.massivecraft.creativegates.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import com.massivecraft.creativegates.CreativeGates;

public class MainListener implements Listener
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MainListener i = new MainListener();
	public static MainListener get() { return i; }
	public MainListener() {}
	
	// -------------------------------------------- //
	// ACTIVATE & DEACTIVATE
	// -------------------------------------------- //
	
	public void activate()
	{
		Bukkit.getPluginManager().registerEvents(this, CreativeGates.get());
	}
	
	public void deactivate()
	{
		HandlerList.unregisterAll(this);
	}
	
	// -------------------------------------------- //
	// LISTENER
	// -------------------------------------------- //
	
	/*
	@EventHandler(priority = EventPriority.MONITOR)
    public void hatUpdate(InventoryClickEvent event)
    {
		// If a player is clicking their hat slot ...
		final Player player = getHattingPlayer(event);
		if (player == null) return;
		
		// ... force an update update to avoid odd client bugs. 
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(MassiveHat.get(), new Runnable(){
			@Override
			public void run()
			{
				player.updateInventory();
			}
		});
    }*/
	
}
