package com.massivecraft.creativegates.listeners;

import org.bukkit.entity.Player;

import com.massivecraft.creativegates.CreativeGates;
import com.massivecraft.creativegates.event.CreativeGatesListener;
import com.massivecraft.creativegates.event.CreativeGatesTeleportEvent;

public class PluginGateListener extends CreativeGatesListener {
    public PluginGateListener(CreativeGates plugin) {
    }
    @Override
    public void onPlayerGateTeleport(CreativeGatesTeleportEvent event) {
        if(event.isCancelled()) return;
        // Teleport
        Player player = event.getPlayerMoveEvent().getPlayer();
        
        // For now we do not handle vehicles
        if (player.isInsideVehicle()) {
            player.leaveVehicle();
        }
        
        player.setNoDamageTicks(5);
        event.getPlayerMoveEvent().setFrom(event.getLocation());
        event.getPlayerMoveEvent().setTo(event.getLocation());
        player.teleport(event.getLocation());
    }
}
