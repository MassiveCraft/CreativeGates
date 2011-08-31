   package com.massivecraft.creativegates.listeners;

   import org.bukkit.entity.Player;
   import org.bukkit.Location;
   import org.bukkit.Material;

   import com.massivecraft.creativegates.CreativeGates;
   import com.massivecraft.creativegates.event.CreativeGatesListener;
   import com.massivecraft.creativegates.event.CreativeGatesTeleportEvent;

   public class PluginGateListener extends CreativeGatesListener {
   
      CreativeGates p = CreativeGates.p;
    
      @Override
      public void onPlayerGateTeleport(CreativeGatesTeleportEvent event)
      {
         if(event.isCancelled()) 
            return;
        // Teleport
         Player player = event.getPlayerMoveEvent().getPlayer();
        
        // For now we do not handle vehicles
         if (player.isInsideVehicle())
         {
            player.leaveVehicle();
         }
         AnimationRun run = new AnimationRun(player, event);
         new Thread(run).start();
      }
      private class AnimationRun implements Runnable {
         private Player player;
         private CreativeGatesTeleportEvent event;
         public AnimationRun(Player p, CreativeGatesTeleportEvent e) {
            player = p;
            event = e;
         }
         public void run() {
            Location loc = player.getLocation();
            int block = loc.getBlock().getType().getId();
            player.sendBlockChange(loc, Material.PORTAL.getId(), (byte)0);
            try {
               Thread.sleep(5000);
            } 
               catch(Exception e) {
               }
            player.sendBlockChange(loc, block, (byte)0);
            player.setNoDamageTicks(5);
            event.getPlayerMoveEvent().setFrom(event.getLocation());
            event.getPlayerMoveEvent().setTo(event.getLocation());
            player.teleport(event.getLocation());
         }
      }
   }
