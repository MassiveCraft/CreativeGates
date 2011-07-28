package com.massivecraft.creativegates.event;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

@SuppressWarnings("serial")
public class CreativeGatesTeleportEvent extends Event implements Cancellable {
    
    private Location location;
    private Set<Material> materials;
    private boolean cancelled;
    private PlayerMoveEvent event;

    public CreativeGatesTeleportEvent(PlayerMoveEvent event, Location location, Set<Material> materials) {
        super("CreativeGatesTeleportEvent");
        this.event = event;
        this.location = location;
        this.materials = materials;
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
    
    public Set<Material> getMaterials() {
        return this.materials;
    }
    
    public PlayerMoveEvent getPlayerMoveEvent() {
        return this.event;
    }
}
