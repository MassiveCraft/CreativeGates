package com.massivecraft.creativegates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.block.Block;

import com.massivecraft.mcore.util.Txt;

public class MaterialCountUtil
{
	public static Map<Material, Integer> count(Collection<Block> blocks)
	{
		Map<Material, Integer> ret = new HashMap<Material, Integer>();
		for (Block block : blocks)
		{
			Material material = block.getType();
			if ( ! ret.containsKey(material))
			{
				ret.put(material, 1);
				continue;
			}
			ret.put(material, ret.get(material)+1);
		}
		return ret;
	}
	
	public static boolean has(Map<Material, Integer> me, Map<Material, Integer> req)
	{
		for (Entry<Material, Integer> entry : req.entrySet())
		{
			Material material = entry.getKey();
			Integer reqCount = entry.getValue();
			Integer meCount = me.get(material);
			if (meCount == null || meCount < reqCount) return false;
		}
		return true;
	}
	
	public static String desc(Map<Material, Integer> materialCounts)
	{
		List<String> parts = new ArrayList<String>();
		for (Entry<Material, Integer> entry : materialCounts.entrySet())
		{
			Material material = entry.getKey();
			Integer count = entry.getValue();
			String part = Txt.parse("<v>%d <k>%s", count, Txt.getMaterialName(material));
			parts.add(part);
		}
		return Txt.implodeCommaAnd(parts, Txt.parse("<i>, "), Txt.parse(" <i>and "));
	}
}
