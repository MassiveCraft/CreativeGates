package com.massivecraft.creativegates.zcore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.ServerListener;

public class MPluginSecretServerListener extends ServerListener
{
	private MPlugin p;
	public MPluginSecretServerListener(MPlugin p)
	{
		this.p = p;
	}
	
	// This method is not perfect. It says unknown console command.
	@Override
	public void onServerCommand(ServerCommandEvent event)
	{
		if (event.getCommand().length() == 0) return;

		List<String> args = new ArrayList<String>(Arrays.asList(event.getCommand().split("\\s+")));
		String label = args.remove(0);
		
		if (p.handleCommand(label, args, event.getSender()))
		{
			event.setCommand("");
		}
	}
	
	
}
