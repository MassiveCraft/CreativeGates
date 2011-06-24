package org.mcteam.creativegates.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.mcteam.creativegates.Conf;
import org.mcteam.creativegates.Gate;
import org.mcteam.creativegates.P;
import org.mcteam.creativegates.Permission;
import org.mcteam.creativegates.WorldCoord;



public class PluginPlayerListener extends PlayerListener {
	P p;
	
	public PluginPlayerListener(P p) {
		this.p = p;
	}
	
	public void onPlayerMove(PlayerMoveEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		// We look one up due to half blocks.
		Block blockToTest = event.getTo().getBlock().getFace(BlockFace.UP);
		
		// Fast material check 
		if (blockToTest.getType() != Material.STATIONARY_WATER) {
			return;
		}
		
		// Find the gate if there is one
		Gate gateFrom = p.getGateFromContentBlock(blockToTest);
		if (gateFrom == null) {
			return;
		}
		
		// Find the target location
		Location targetLocation = gateFrom.getMyTargetExitLocation();
		if (targetLocation == null) {
			return;
		}
		
		// Teleport
		Player player = event.getPlayer();
		
		// For now we do not handle vehicles
		if (player.isInsideVehicle()) {
			player.leaveVehicle();
		}
		
		player.setNoDamageTicks(5);
		event.setFrom(targetLocation);
        event.setTo(targetLocation);
        player.teleport(targetLocation);
	}
	
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		// We are only interested in left clicks with a wand
		if
		(
			event.getAction() != Action.LEFT_CLICK_BLOCK ||
			event.getPlayer().getItemInHand().getType() != Conf.wand
		)
		{
			return;
		}
		
		Block clickedBlock = event.getClickedBlock();
		Player player = event.getPlayer();
		
		// Did we hit an existing gate?
		// In such case send information.
		Gate gate = p.getGateFromBlock(clickedBlock);
		if (gate != null) {
			gate.informPlayer(player);
			return;
		}
		
		// Did we hit a diamond block?
		if (clickedBlock.getType() == Conf.block) {
			// create a gate if the player has the permission
			if (Permission.CREATE.test(player)) {
				p.open(new WorldCoord(clickedBlock), player);
			}
		}
	}
	
	@Override
	public void onPlayerBucketFill(PlayerBucketFillEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		if ( p.getGateFromContentBlock(event.getBlockClicked()) != null	) {
			event.setCancelled(true);
		}
	}
	
	@Override
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		if ( p.getGateFromContentBlock(event.getBlockClicked().getFace(event.getBlockFace())) != null ) {
			event.setCancelled(true);
		}
	}
}
