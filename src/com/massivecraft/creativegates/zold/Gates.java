package com.massivecraft.creativegates.zold;

import java.io.File;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.google.gson.reflect.TypeToken;
import com.massivecraft.creativegates.zold.zcore.persist.*;

public class Gates extends EntityCollection<Gate>
{
	public static Gates i = new Gates();
	
	CreativeGates p = CreativeGates.p;
	
	private Gates()
	{
		super
		(
			Gate.class,
			new ConcurrentSkipListSet<Gate>(new Comparator<Gate>()
			{
				@Override
				public int compare(Gate me, Gate you)
				{
					return me.sourceCoord.toString().compareTo(you.sourceCoord.toString());
				}
			}),
			new ConcurrentHashMap<String, Gate>(),
			new File(CreativeGates.p.getDataFolder(), "gate.json"),
			CreativeGates.p.gson
		);
	}
	
	@Override
	public Type getMapType()
	{
		return new TypeToken<Map<String, Gate>>(){}.getType();
	}
	
	// -------------------------------------------- //
	// Find gate from block or coord.
	// -------------------------------------------- //
	
	public Gate findFromContent(WorldCoord coord)
	{
		for (Gate gate : this.get())
		{
			if (gate.contentCoords.contains(coord))
			{
				return gate;
			}
		}
		return null;
	}
	
	public Gate findFromContent(Block block)
	{
		return findFromContent(new WorldCoord(block));
	}
	
	public Gate findFromFrame(WorldCoord coord)
	{
		for (Gate gate : this.get())
		{
			if (gate.frameCoords.contains(coord)) 
			{
				return gate;
			}
		}
		return null;
	}
	
	public Gate findFromFrame(Block block)
	{
		return findFromFrame(new WorldCoord(block));
	}
	
	public Gate findFrom(WorldCoord coord)
	{
		Gate gate; 
		
		gate = findFromContent(coord);
		if (gate != null)
		{
			return gate;
		}
		
		return findFromFrame(coord);
	}
	
	public Gate findFrom(Block block)
	{
		return findFrom(new WorldCoord(block));
	}

	// -------------------------------------------- //
	// Mass Content Management
	// -------------------------------------------- //
	
	public void emptyAll()
	{
		for (Gate gate : this.get())
		{
			gate.empty();
		}
	}
	
	public void openAllOrDetach()
	{
		for (Gate gate : this.get())
		{
			try
			{
				gate.open();
			}
			catch (GateOpenException e)
			{
				gate.detach();
				p.log(e.getMessage() + " Gate was removed.");
			}
		}
	}
	
	// -------------------------------------------- //
	// Gate Factory
	// -------------------------------------------- //
	
	// TODO: Kolla in open or die saken. Den ska nog bytas ut mot attach.
	public Gate open(WorldCoord sourceCoord, Player player)
	{
		Gate gate = new Gate();
		gate.sourceCoord = sourceCoord;
		//p.log("sourceCoord: "+sourceCoord);
		
		try
		{
			gate.open();
			gate.attach();
			if (player != null)
			{
				gate.informPlayer(player);
			}
			return gate;
		}
		catch (GateOpenException e)
		{
			if (player == null)
			{
				p.log(e.getMessage());
			}
			else
			{
				player.sendMessage(e.getMessage());
			}
			return null;
		}
	}
	
	public Gate open(WorldCoord sourceCoord) {
		return this.open(sourceCoord, null);
	}

}
