package com.massivecraft.creativegates;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.massivecraft.creativegates.event.CreativeGatesTeleportEvent;

public class TheListener implements Listener
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	public CreativeGates p;
	public TheListener(CreativeGates p)
	{
		this.p = p;
		Bukkit.getServer().getPluginManager().registerEvents(this, this.p);
	}
	
	// -------------------------------------------- //
	// BLOCK LISTENER NORMAL
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockPistonExtend(BlockPistonExtendEvent event)
	{
		if (event.isCancelled())
		{
			return;
		}
		
		for (Block block : event.getBlocks())
		{
			if (Gates.i.findFrom(block) != null)
			{
				event.setCancelled(true);
				return;
			}
		}
	}
	
	// The purpose is to stop the water from falling
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockFromTo(BlockFromToEvent event)
	{
		if (event.isCancelled()) return;
		
		Block blockFrom = event.getBlock();
		boolean isWater = blockFrom.getTypeId() == 9;
		
		if ( ! isWater)
		{
			return;
		}
		
		if (Gates.i.findFrom(blockFrom) != null)
		{
			event.setCancelled(true);
		}
	}
	
	// The gate content is invulnerable
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if (event.isCancelled()) return;
		
		Gate gate = Gates.i.findFromContent(event.getBlock()); 
		if (gate != null)
		{
			event.setCancelled(true);
		}
	}
	
	// Is the player allowed to destroy gates?
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreakNormal(BlockBreakEvent event)
	{
		if (event.isCancelled()) return;
		
		Gate gate = Gates.i.findFromFrame(event.getBlock());
		if (gate == null)
		{
			return;
		}
		
		// A player is attempting to destroy a gate. Can he?
		if ( ! Permission.DESTROY.has(event.getPlayer(), true))
		{
			event.setCancelled(true);
		}
	}
	
	// -------------------------------------------- //
	// BLOCK LISTENER MONITOR
	// -------------------------------------------- //
	
	// Destroy the gate if the frame breaks
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockBreakMonitor(BlockBreakEvent event)
	{
		if (event.isCancelled()) return;
		
		Gate gate = Gates.i.findFromFrame(event.getBlock());
		if (gate != null)
		{
			gate.close();
		}
	}
	
	// -------------------------------------------- //
	// BLOCK LISTENER MONITOR
	// -------------------------------------------- //
	// Gates can not be destroyed by explosions
	@EventHandler(priority = EventPriority.NORMAL)
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
	
	// -------------------------------------------- //
	// BLOCK LISTENER MONITOR
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerGateTeleport(CreativeGatesTeleportEvent event)
    {
        if(event.isCancelled()) return;
        
        Player player = event.getPlayerMoveEvent().getPlayer();
        
        // Smoke Time \ :D / FX FTW!! WOOOOooooo..... woo ... yaaah. *Nodding confidently*
        if (Conf.effects)
        {
        	Gate gateFrom = event.getGateFrom();
            if (gateFrom != null) gateFrom.emmitSmoke();
            
            Gate gateTo = event.getGateTo();
            if (gateTo != null) gateTo.emmitSmoke();
        }
        
        // For now we do not handle vehicles
        if (player.isInsideVehicle())
        {
            player.leaveVehicle();
        }
        
        player.setNoDamageTicks(5);
        event.getPlayerMoveEvent().setFrom(event.getLocation());
        event.getPlayerMoveEvent().setTo(event.getLocation());
        player.teleport(event.getLocation());
    }
	
	// -------------------------------------------- //
	// PLAYER LISTENER
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerMove(PlayerMoveEvent event)
	{
		if (event.isCancelled()) return;
		if (event.getFrom().getBlock().equals(event.getTo().getBlock())) return;
		
		// We look one up due to half blocks.
		Block blockToTest = event.getTo().getBlock().getRelative(BlockFace.UP);
		
		// Fast material check 
		if (blockToTest.getType() != Material.STATIONARY_WATER && blockToTest.getType() != Material.WATER) return;
		
		// Find the gate if there is one
		Gate gateFrom = Gates.i.findFromContent(blockToTest);
		if (gateFrom == null) return;
		
		// Is the gate intact?
		if ( ! gateFrom.isIntact())
		{
			gateFrom.close();
			return;
		}
		
		// Can the player use gates?
		if ( ! Permission.USE.has(event.getPlayer(), true)) return;
		
		// Find the target location
		Gate gateTo = gateFrom.getMyTargetGate();
		Location targetLocation = gateTo == null ? null : gateTo.getMyOwnExitLocation();
		if (targetLocation == null)
		{
			event.getPlayer().sendMessage(p.txt.parse(Lang.useFailNoTargetLocation));
			return;
		}
		
		HashSet<Material> frameMaterials = new HashSet<Material>();
		for(int id : gateFrom.frameMaterialIds)
		{
		    frameMaterials.add(Material.getMaterial(id));
		}
		
		CreativeGatesTeleportEvent gateevent = new CreativeGatesTeleportEvent(event, targetLocation, frameMaterials, gateFrom, gateTo);
		p.getServer().getPluginManager().callEvent(gateevent);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if (event.isCancelled()) return;
		
		// We are only interested in clicks on a block with a wand
		if (event.getAction() != Action.LEFT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if (event.getPlayer().getItemInHand().getTypeId() != Conf.wand) return;
		
		Block clickedBlock = event.getClickedBlock();
		Player player = event.getPlayer();
		
		// Did we hit an existing gate?
		// In such case send information.
		Gate gate = Gates.i.findFrom(clickedBlock);
		if (gate != null)
		{
			gate.informPlayer(player);
			return;
		}
		
		// Did we hit a diamond block?
		if (clickedBlock.getTypeId() == Conf.block)
		{
			// create a gate if the player has the permission
			if (Permission.CREATE.has(player, true))
			{
				Gates.i.open(new WorldCoord(clickedBlock), player);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerBucketFill(PlayerBucketFillEvent event)
	{
		if (event.isCancelled())
		{
			return;
		}
		
		if ( Gates.i.findFromContent(event.getBlockClicked()) != null )
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event)
	{
		if (event.isCancelled())
		{
			return;
		}
		
		if ( Gates.i.findFromContent(event.getBlockClicked().getRelative(event.getBlockFace())) != null )
		{
			event.setCancelled(true);
		}
	}
	
}
