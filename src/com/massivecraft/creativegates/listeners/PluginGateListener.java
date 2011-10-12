package com.massivecraft.creativegates.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.massivecraft.creativegates.Conf;
import com.massivecraft.creativegates.CreativeGates;
import com.massivecraft.creativegates.Gate;
import com.massivecraft.creativegates.event.CreativeGatesListener;
import com.massivecraft.creativegates.event.CreativeGatesTeleportEvent;

public class PluginGateListener extends CreativeGatesListener
{
	
	CreativeGates p = CreativeGates.p;
    
    @Override
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
}
