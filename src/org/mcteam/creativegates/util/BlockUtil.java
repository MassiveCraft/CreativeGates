package org.mcteam.creativegates.util;

import java.util.*;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class BlockUtil {
	
	public static HashSet<Material> standableMaterials;
	
	static {
		standableMaterials = new HashSet<Material>();
		standableMaterials.add(Material.AIR);
		standableMaterials.add(Material.BROWN_MUSHROOM);
		standableMaterials.add(Material.CROPS);
		standableMaterials.add(Material.DEAD_BUSH);
		standableMaterials.add(Material.DETECTOR_RAIL);
		standableMaterials.add(Material.DIODE_BLOCK_OFF);
		standableMaterials.add(Material.DIODE_BLOCK_ON);
		//standableMaterials.add(Material.IRON_DOOR_BLOCK);
		standableMaterials.add(Material.LADDER);
		standableMaterials.add(Material.LEVER);
		standableMaterials.add(Material.LONG_GRASS);
		// PORTAL ??
		standableMaterials.add(Material.RAILS);
		standableMaterials.add(Material.RED_MUSHROOM);
		standableMaterials.add(Material.RED_ROSE);
		standableMaterials.add(Material.REDSTONE_TORCH_OFF);
		standableMaterials.add(Material.REDSTONE_TORCH_ON);
		standableMaterials.add(Material.REDSTONE_WIRE);
		standableMaterials.add(Material.SAPLING);
		standableMaterials.add(Material.STATIONARY_WATER);
		standableMaterials.add(Material.STONE_BUTTON);
		//standableMaterials.add(Material.STEP);
		standableMaterials.add(Material.SUGAR_CANE_BLOCK);
		standableMaterials.add(Material.TORCH);
		//standableMaterials.add(Material.TRAP_DOOR); // ??
		standableMaterials.add(Material.WALL_SIGN);
		standableMaterials.add(Material.WATER);
		//standableMaterials.add(Material.WOOD_DOOR); //???
		standableMaterials.add(Material.YELLOW_FLOWER); //???
		}
	
	public static boolean isMaterialStandable(Material material) {
		return standableMaterials.contains(material);
	}
	
	public static boolean canPlayerStandInBlock(Block block) {
		return isMaterialStandable(block.getType()) && isMaterialStandable(block.getFace(BlockFace.UP).getType());
	}

}
