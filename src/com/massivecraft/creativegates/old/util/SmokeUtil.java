package com.massivecraft.creativegates.old.util;

import java.util.Collection;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;

public class SmokeUtil
{
	public static void emmitFromLocations(Collection<Location> locations)
	{
		World world;
		for (Location location : locations)
		{
			if (location == null) continue;
			world = location.getWorld();
			if (world == null) continue;
			for (int i = 0; i <= 8; i++)
			{
				world.playEffect(location, Effect.SMOKE, i);
			}
		}
	}
}
