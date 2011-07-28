package com.massivecraft.creativegates.event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

@SuppressWarnings("serial")
public class CreativeGatesTeleportEvent extends Event implements Cancellable {
    
    private Location location;
    private Material material;
    private boolean cancelled;
    private PlayerMoveEvent event;

    public CreativeGatesTeleportEvent(PlayerMoveEvent event, Location location, Material material) {
        super("CreativeGatesTeleportEvent");
        this.event = event;
        this.location = location;
        this.material = material;
        this.cancelled = false;
    }
    
    public boolean isCancelled(){
        return this.cancelled;
    }
    
    public void setCancelled(boolean cancelled){
        this.cancelled = cancelled;
    }
    
    public Location getLocation() {
        return this.location;
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }
    
    public Material getMaterial() {
        return this.material;
    }
    
    public PlayerMoveEvent getPlayerMoveEvent() {
        return this.event;
    }
}
