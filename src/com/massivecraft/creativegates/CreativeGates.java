   package com.massivecraft.creativegates;

   import org.bukkit.event.Event;
   import org.bukkit.plugin.PluginManager;

   import com.massivecraft.creativegates.listeners.*;
   import com.massivecraft.creativegates.zcore.*;

   public class CreativeGates extends MPlugin
   {
   // Our single plugin instance
      public static CreativeGates p;
   
   // Listeners
      public PluginPlayerListener playerListener;
      public PluginBlockListener blockListener;
      public PluginBlockListenerMonitor blockListenerMonitor;
      public PluginEntityListener entityListener;
      public PluginGateListener gateListener;
   
      public CreativeGates()
      {
         p = this;
      
         this.playerListener = new PluginPlayerListener();
         this.blockListener = new PluginBlockListener();
         this.blockListenerMonitor = new PluginBlockListenerMonitor();
         this.entityListener = new PluginEntityListener();
         this.gateListener = new PluginGateListener();
      }
   
      public void onEnable()
      {
         if ( ! preEnable()) 
            return;
      
      // TODO fix config auto update routine... ?
         Conf.load();
      
         Gates.i.loadFromDisc();
         Gates.i.openAllOrDetach();
      
      // Register events
         PluginManager pm = this.getServer().getPluginManager();
         pm.registerEvent(Event.Type.PLAYER_INTERACT, this.playerListener, Event.Priority.Normal, this);
         pm.registerEvent(Event.Type.PLAYER_BUCKET_FILL, this.playerListener, Event.Priority.Normal, this);
         pm.registerEvent(Event.Type.PLAYER_BUCKET_EMPTY, this.playerListener, Event.Priority.Normal, this);
         pm.registerEvent(Event.Type.PLAYER_MOVE, this.playerListener, Event.Priority.Monitor, this);
      
         pm.registerEvent(Event.Type.BLOCK_FROMTO, this.blockListener, Event.Priority.Normal, this);
         pm.registerEvent(Event.Type.BLOCK_PISTON_EXTEND, this.blockListener, Event.Priority.Normal, this);
         pm.registerEvent(Event.Type.BLOCK_PLACE, this.blockListener, Event.Priority.Normal, this);
         pm.registerEvent(Event.Type.BLOCK_BREAK, this.blockListener, Event.Priority.Normal, this);
         pm.registerEvent(Event.Type.BLOCK_BREAK, this.blockListenerMonitor, Event.Priority.Monitor, this);
      
         pm.registerEvent(Event.Type.ENTITY_EXPLODE, this.entityListener, Event.Priority.Normal, this);
      
         pm.registerEvent(Event.Type.CUSTOM_EVENT, this.gateListener, Event.Priority.Monitor, this);
      
         postEnable();
      }
   
      public void onDisable()
      {
         Gates.i.emptyAll();
      }
   
   // -------------------------------------------- //
   // LANG AND TAGS
   // -------------------------------------------- //
   
      @Override
      public void addLang()
      {
         super.addLang();
         this.lang.put("openfail.wrong_source_material", "<b>The source block must be made of %s.");
         this.lang.put("openfail.no_frame", "<b>There is no valid frame for the gate.");
         this.lang.put("usefail.no_target_location", "<i>This gate does not lead anywhere.");
         this.lang.put("info.materials", "<i>Materials: %s");
         this.lang.put("info.gatecount", "<i>Gates: <h>%s");
      }
   
      @Override
      public void addTags()
      {
         super.addTags();
         this.tags.put("i", "§b");
         this.tags.put("h", "§a");
      }
   
   }
