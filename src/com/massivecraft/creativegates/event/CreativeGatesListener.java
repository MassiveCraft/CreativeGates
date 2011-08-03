package com.massivecraft.creativegates.event;

import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;


public class CreativeGatesListener extends CustomEventListener {
    @Override
    public void onCustomEvent(Event event) {
        if(event instanceof CreativeGatesTeleportEvent) {
            onPlayerGateTeleport((CreativeGatesTeleportEvent) event);
        }
    }
    
    public void onPlayerGateTeleport(CreativeGatesTeleportEvent event) {}
}
