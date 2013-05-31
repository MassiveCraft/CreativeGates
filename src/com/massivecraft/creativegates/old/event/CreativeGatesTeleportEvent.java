package com.massivecraft.creativegates.old.event;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;

import com.massivecraft.creativegates.old.Gate;

public class CreativeGatesTeleportEvent extends Event implements Cancellable
{
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	private static final HandlerList handlers = new HandlerList();
	public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// FIELD: cancelled
	private boolean cancelled;
	public boolean isCancelled() { return this.cancelled; }
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
	
    // FIELD: event
	private PlayerMoveEvent event;
	public PlayerMoveEvent getPlayerMoveEvent() { return this.event; }
	
	// FIELD: location
	private Location location;
	public Location getLocation() { return this.location; }   
    public void setLocation(Location location) { this.location = location; }
	
	// FIELD: materials
    private Set<Material> materials;
    public Set<Material> getMaterials() { return this.materials; }
    
    // FIELD: gateFrom
    private Gate gateFrom;
    public void setGateFrom(Gate gateFrom) { this.gateFrom = gateFrom; }
	public Gate getGateFrom() { return gateFrom; }
    
    // FIELD: gateTo
	// The smoke effect will be created at these gates
    private Gate gateTo;
    public void setGateTo(Gate gateTo) { this.gateTo = gateTo; }
	public Gate getGateTo() { return gateTo; }
    
    public CreativeGatesTeleportEvent(PlayerMoveEvent event, Location location, Set<Material> materials, Gate gateFrom, Gate gateTo)
    {
        this.event = event;
        this.location = location;
        this.materials = materials;
        this.cancelled = false;
        this.setGateFrom(gateFrom);
        this.setGateTo(gateTo);
    }
}
