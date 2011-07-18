package com.massivecraft.creativegates.listeners;

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

import com.massivecraft.creativegates.Conf;
import com.massivecraft.creativegates.Gate;
import com.massivecraft.creativegates.Gates;
import com.massivecraft.creativegates.P;
import com.massivecraft.creativegates.Permission;
import com.massivecraft.creativegates.WorldCoord;



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
		Block blockToTest = event.getTo().getBlock().getRelative(BlockFace.UP);
		
		// Fast material check 
		if (blockToTest.getType() != Material.STATIONARY_WATER) {
			return;
		}
		
		// Find the gate if there is one
		Gate gateFrom = Gates.findFromContent(blockToTest);
		if (gateFrom == null) {
			return;
		}
		
		// Can the player use gates?
		if ( ! Permission.USE.test(event.getPlayer())) {
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
		Gate gate = Gates.findFrom(clickedBlock);
		if (gate != null) {
			gate.informPlayer(player);
			return;
		}
		
		// Did we hit a diamond block?
		if (clickedBlock.getType() == Conf.block) {
			// create a gate if the player has the permission
			if (Permission.CREATE.test(player)) {
				Gates.open(new WorldCoord(clickedBlock), player);
			}
		}
	}
	
	@Override
	public void onPlayerBucketFill(PlayerBucketFillEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		if ( Gates.findFromContent(event.getBlockClicked()) != null	) {
			event.setCancelled(true);
		}
	}
	
	@Override
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		if ( Gates.findFromContent(event.getBlockClicked().getRelative(event.getBlockFace())) != null ) {
			event.setCancelled(true);
		}
	}
}
