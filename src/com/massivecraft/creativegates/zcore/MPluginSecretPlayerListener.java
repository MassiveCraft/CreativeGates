package com.massivecraft.creativegates.zcore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;

public class MPluginSecretPlayerListener extends PlayerListener
{
	private MPlugin p;
	public MPluginSecretPlayerListener(MPlugin p)
	{
		this.p = p;
	}
	
	@Override
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
	{
		if (event.isCancelled()) return;

		List<String> args = new ArrayList<String>(Arrays.asList(event.getMessage().split("\\s+")));
		String label = args.remove(0).substring(1);
		
		if (p.handleCommand(label, args, event.getPlayer()))
		{
			event.setCancelled(true);
		}
	}
}
