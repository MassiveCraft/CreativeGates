package com.massivecraft.creativegates;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.AbstractMap.SimpleEntry;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.massivecraft.creativegates.entity.UConf;

public class FloodUtil
{
	public static Entry<GateOrientation, Set<Block>> getGateFloodInfo(Block startBlock)
	{
		UConf uconf = UConf.get(startBlock);
		
		// Search for content WE and NS
		GateOrientation gateOrientaion = null;
		Set<Block> blocksNS = getFloodBlocks(startBlock, new HashSet<Block>(), GateOrientation.NS.expandFaces, uconf.getMaxarea());
		Set<Block> blocksWE = getFloodBlocks(startBlock, new HashSet<Block>(), GateOrientation.WE.expandFaces, uconf.getMaxarea());
		
		// Figure out dir and content... or throw no frame fail. 
		Set<Block> blocks;
		if (blocksNS != null && blocksWE != null)
		{
			if (blocksNS.size() > blocksWE.size())
			{
				blocks = blocksWE;
				gateOrientaion = GateOrientation.WE;
			}
			else
			{
				blocks = blocksNS;
				gateOrientaion = GateOrientation.NS;
			}
		}
		else if (blocksNS != null)
		{
			blocks = blocksNS;
			gateOrientaion = GateOrientation.NS;
		}
		else if (blocksWE != null)
		{
			blocks = blocksWE;
			gateOrientaion = GateOrientation.WE;
		}
		else
		{
			return null;
		}
		
		// Add in the frame as well.
		blocks = expandedByOne(blocks, gateOrientaion.expandFaces);
		
		return new SimpleEntry<GateOrientation, Set<Block>>(gateOrientaion, blocks);
	}
	
	public static Set<Block> getFloodBlocks(Block startBlock, Set<Block> foundBlocks, Set<BlockFace> expandFaces, int maxarea)
	{
		if (foundBlocks == null)
		{
			return null;
		}
		
		if  (foundBlocks.size() > maxarea)
		{
			return null;
		}
		
		if (foundBlocks.contains(startBlock))
		{
			return foundBlocks;
		}
		
		if (CreativeGates.isVoid(startBlock))
		{
			// ... We found a block :D ...
			foundBlocks.add(startBlock);
			
			// ... And flood away !
			for (BlockFace face : expandFaces)
			{
				Block potentialBlock = startBlock.getRelative(face);
				foundBlocks = getFloodBlocks(potentialBlock, foundBlocks, expandFaces, maxarea);
			}
		}
		
		return foundBlocks;
	}
	
	public static Set<Block> expandedByOne(Set<Block> blocks, Set<BlockFace> expandFaces)
	{
		Set<Block> ret = new HashSet<Block>();
		
		ret.addAll(blocks);
		
		for (Block block : blocks)
		{
			for (BlockFace face : expandFaces)
			{
				Block potentialBlock = block.getRelative(face);
				if (ret.contains(potentialBlock)) continue;
				ret.add(potentialBlock);
			}
		}
		
		return ret;
	}
}
