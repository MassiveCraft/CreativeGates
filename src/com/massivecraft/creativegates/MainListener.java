package com.massivecraft.creativegates;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.massivecraft.creativegates.entity.UConf;
import com.massivecraft.creativegates.entity.UConfColls;
import com.massivecraft.creativegates.entity.UGate;
import com.massivecraft.creativegates.entity.UGateColls;
import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;

public class MainListener implements Listener
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MainListener i = new MainListener();
	public static MainListener get() { return i; }
	public MainListener() {}
	
	// -------------------------------------------- //
	// ACTIVATE & DEACTIVATE
	// -------------------------------------------- //
	
	public void activate()
	{
		Bukkit.getPluginManager().registerEvents(this, CreativeGates.get());
	}
	
	public void deactivate()
	{
		HandlerList.unregisterAll(this);
	}
	
	// -------------------------------------------- //
	// IS X NEARBY (UTIL)
	// -------------------------------------------- //
	
	public static boolean isPortalNearby(Block block)
	{
		final int radius = 2; 
		for (int dx = -radius; dx <= radius; dx++)
		{
			for (int dy = -radius; dy <= radius; dy++)
			{
				for (int dz = -radius; dz <= radius; dz++)
				{
					if (block.getRelative(dx, dy, dz).getType() == Material.PORTAL) return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isGateNearby(Block block)
	{
		UConf uconf = UConf.get(block);
		if (!uconf.isEnabled()) return false;
		
		final int radius = 2; 
		for (int dx = -radius; dx <= radius; dx++)
		{
			for (int dy = -radius; dy <= radius; dy++)
			{
				for (int dz = -radius; dz <= radius; dz++)
				{
					if (CreativeGates.get().getIndex().get(PS.valueOf(block.getRelative(dx, dy, dz))) != null) return true;
				}
			}
		}
		return false;
	}
	
	// -------------------------------------------- //
	// STABILIZE PORTAL CONENT
	// -------------------------------------------- //
	
	// PORTAL
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void stabilizePortalContent(BlockPhysicsEvent event)
    {
		// If a portal block is running physics ...
		Block block = event.getBlock();
		if (block.getType() != Material.PORTAL) return;
				
		// ... and we are filling or that block is stable according to our algorithm ...
		if (!(CreativeGates.get().isFilling() || isPortalBlockStable(block))) return;
		
		// ... then block the physics to stop the portal from disappearing.
		event.setCancelled(true);
    }
	
	public static boolean isPortalBlockStable(Block block)
	{
		if (CreativeGates.isVoid(block.getRelative(+0, +1, +0)) == true) return false;
		if (CreativeGates.isVoid(block.getRelative(+0, -1, +0)) == true) return false;
		
		if (CreativeGates.isVoid(block.getRelative(+1, +0, +0)) == false && CreativeGates.isVoid(block.getRelative(-1, +0, +0)) == false) return true;
		if (CreativeGates.isVoid(block.getRelative(+0, +0, +1)) == false && CreativeGates.isVoid(block.getRelative(+0, +0, -1)) == false) return true;
		
		return false;
	}
	
	// WATER
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void stabilizePortalContent(BlockFromToEvent event)
    {
		UConf uconf = UConfColls.get().getForWorld(event.getBlock().getWorld().getName()).get(MassiveCore.INSTANCE);
		if (!uconf.isUsingWater()) return;
		if (UGate.get(event.getBlock()) == null && UGate.get(event.getToBlock()) == null) return;
		event.setCancelled(true);
    }
	
	public static void stabilizePortalContentBlock(Block block, Cancellable cancellable)
	{
		if (UGate.get(block) == null) return;
		cancellable.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void stabilizePortalContent(BlockPlaceEvent event)
	{
		stabilizePortalContentBlock(event.getBlock(), event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void stabilizePortalContent(PlayerBucketFillEvent event)
	{
		stabilizePortalContentBlock(event.getBlockClicked(), event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void stabilizePortalContent(PlayerBucketEmptyEvent event)
	{
		stabilizePortalContentBlock(event.getBlockClicked(), event);
	}
	
	// -------------------------------------------- //
	// DISABLE VANILLA PORTAL BEHAVIOR
	// -------------------------------------------- //
	
	public static void disableVanillaGates(Location location, Cancellable cancellable)
	{
		if (isGateNearby(location.getBlock()))
		{
			cancellable.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void disableVanillaGates(PlayerPortalEvent event)
	{
		disableVanillaGates(event.getFrom(), event);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void disableVanillaGates(EntityPortalEvent event)
	{
		disableVanillaGates(event.getFrom(), event);
	}
	
	// -------------------------------------------- //
	// NO ZOMBIE PIGMAN PORTAL SPAWN 
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void noZombiePigmanPortalSpawn(CreatureSpawnEvent event)
	{
		// If a zombie pigman is spawning ...
		if (event.getEntityType() != EntityType.PIG_ZOMBIE) return;
		
		// ... near a portal ...
		if (!isPortalNearby(event.getLocation().getBlock())) return;
		
		// ... and we are blocking zombie pigman portal spawn ...
		if (UConf.get(event.getLocation()).isPigmanPortalSpawnAllowed()) return;
		
		// ... then block the spawn event.
		event.setCancelled(true);
	}
	
	// -------------------------------------------- //
	// USE GATE
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void useGate(PlayerMoveEvent event)
	{
		// If a player is moving from one block to another ...
		if (MUtil.isSameBlock(event)) return;
		
		// ... and there is a gate in the new block ...
		UGate ugate = UGate.get(event.getTo());
		if (ugate == null) return;
		
		// ... and if the gate is intact ...
		if (!ugate.isIntact())
		{
			// We try to detect that a gate was destroyed once it happens by listening to a few events.
			// However there will always be cases we miss and by checking at use we catch those as well.
			// Examples could be map corruption or use of WorldEdit.
			ugate.destroy();
			return;
		}
		
		// ... and gates are enabled here ...
		UConf uconf = UConf.get(event.getTo());
		if (!uconf.isEnabled()) return;
		
		// ... and we have permission to use gates ...
		Player player = event.getPlayer();
		if (!Perm.USE.has(player, true)) return;
		
		// ... and the gate has enter enabled ...
		if (!ugate.isEnterEnabled())
		{
			String message = Txt.parse("<i>Este portão está com entrada desabilitada.");
			player.sendMessage(message);
			return;
		}
		
		// ... and the player is alive ...
		if (player.isDead()) return;
		
		// ... then transport the player.
		ugate.transport(player);
	}
	
	// -------------------------------------------- //
	// DESTROY GATE
	// -------------------------------------------- //
	
	public static void destroyGate(Block block)
	{
		UGate ugate = UGate.get(block);
		if (ugate == null) return;
		ugate.destroy();
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void destroyGate(BlockBreakEvent event)
	{
		destroyGate(event.getBlock());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void destroyGate(EntityChangeBlockEvent event)
	{
		destroyGate(event.getBlock());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void destroyGate(EntityExplodeEvent event)
	{
		for (Block block : event.blockList())
		{
			destroyGate(block);
		}
	}
	
	// This one looks weird since it needs to handle beds as well
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void destroyGate(BlockPistonExtendEvent event)
	{
		Set<Block> blocks = new HashSet<Block>();
		
		Block piston = event.getBlock();
		Block extension = piston.getRelative(event.getDirection());
		blocks.add(extension);
		
		for (Block block : event.getBlocks())
		{
			blocks.add(block);
			blocks.add(block.getRelative(event.getDirection()));
		}
		
		for (Block block : blocks)
		{
			destroyGate(block);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void destroyGate(BlockPistonRetractEvent event)
	{
		destroyGate(event.getBlock().getRelative(event.getDirection(), 1));
		if (event.isSticky())
		{
			destroyGate(event.getBlock().getRelative(event.getDirection(), 2));
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void destroyGate(BlockFadeEvent event)
	{
		destroyGate(event.getBlock());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void destroyGate(BlockBurnEvent event)
	{
		destroyGate(event.getBlock());
	}
	
	// -------------------------------------------- //
	// TOOLS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void tools(PlayerInteractEvent event)
    {
		// If a player ...
		final Player player = event.getPlayer();
		
		// ... is clicking a block ...
		final Block clickedBlock = event.getClickedBlock();
		if (clickedBlock == null) return;
		
		// ... and gates are enabled here ...
		UConf uconf = UConf.get(clickedBlock);
		if (!uconf.isEnabled()) return;
		
		// ... and the item in hand ...
		final ItemStack currentItem = event.getItem();
		if (currentItem == null) return;
		final Material material = currentItem.getType();
		
		// ... is in any way an interesting material ...
		if
		(
			material != uconf.getMaterialInspect()
			&& 
			material != uconf.getMaterialMode()
			&& 
			material != uconf.getMaterialSecret()
			&&
			material != uconf.getMaterialCreate()
		)
		{
			return;
		}
		
		// ... then find the current gate ...
		final UGate currentGate = UGate.get(clickedBlock);
		
		String message = null;
		
		// ... and if ...
		if (material == uconf.getMaterialCreate())
		{
			// ... we are trying to create ...
			
			// ... check permission node ...
			if (!Perm.CREATE.has(player, true)) return;
			
			// ... check if the place is occupied ...
			if (currentGate != null)
			{
				message = Txt.parse("<b>Não há espaço par um portal aqui, pois já há outro.");
				player.sendMessage(message);
				return;
			}
			
			// ... check if the item is named ...
			ItemMeta currentItemMeta = currentItem.getItemMeta();
			if (!currentItemMeta.hasDisplayName())
			{
				message = Txt.parse("<b>Você precisa renomear %s para poder criar um portal com o mesmo.", Txt.getMaterialName(material));
				player.sendMessage(message);
				return;
			}
			String newNetworkId = ChatColor.stripColor(currentItemMeta.getDisplayName());
			
			// ... perform the flood fill ...
			Block startBlock = clickedBlock.getRelative(event.getBlockFace());
			Entry<GateOrientation, Set<Block>> gateFloodInfo = FloodUtil.getGateFloodInfo(startBlock);
			if (gateFloodInfo == null)
			{
				message = Txt.parse("<b>Não há moldura para o portal. Ou este é muito grande.", Txt.getMaterialName(material));
				player.sendMessage(message);
				return;
			}
			
			GateOrientation gateOrientation = gateFloodInfo.getKey();
			Set<Block> blocks = gateFloodInfo.getValue();
			
			// ... ensure the required blocks are present ...
			Map<Material, Integer> materialCounts = MaterialCountUtil.count(blocks);
			if (!MaterialCountUtil.has(materialCounts, uconf.getBlocksrequired()))
			{
				message = Txt.parse("<b>A moldura deve conter %s<b>.", MaterialCountUtil.desc(uconf.getBlocksrequired()));
				player.sendMessage(message);
				return;
			}
			
			// ... calculate the exit location ...
			PS exit = PS.valueOf(player.getLocation());
			exit = exit.withPitch(0F);
			exit = exit.withYaw(gateOrientation.getExitYaw(exit, PS.valueOf(blocks.iterator().next())));
			
			// ... calculate the coords ...
			Set<PS> coords = new HashSet<PS>();
			for (Block block : blocks)
			{
				coords.add(PS.valueOf(block).withWorld(null));
			}
			
			// ... create the gate ...
			UGate newGate = UGateColls.get().get(startBlock).create();
			newGate.setCreatorId(IdUtil.getId(player));
			newGate.setNetworkId(newNetworkId);
			newGate.setExit(exit);
			newGate.setCoords(coords);
			
			// ... set the air blocks to portal material ...
			newGate.fill();
			
			// ... run fx ...
			newGate.fxKitCreate(player);
			
			// ... fx-inform the player ...
			message = Txt.parse("<g>Um portal \"<h>%s<g>\" toma forma adiante.", newNetworkId);
			player.sendMessage(message);
			
			// ... item cost ...
			if (uconf.isRemovingCreateToolItem())
			{
				// ... remove one item amount...
				
				// (decrease count in hand)
				ItemStack newItem = new ItemStack(currentItem);
				newItem.setAmount(newItem.getAmount() - 1);
				player.setItemInHand(newItem);
				
				// (message)
				message = Txt.parse("<i>%s se desmantela e é pulverizado.", Txt.getMaterialName(material));
				player.sendMessage(message);
			}
			else if (uconf.isRemovingCreateToolName())
			{
				// ... just remove the item name ...
				
				// (decrease count in hand)
				ItemStack newItemNamed = new ItemStack(currentItem);
				newItemNamed.setAmount(newItemNamed.getAmount() - 1);
				player.setItemInHand(newItemNamed);
				
				// (add one unnamed)
				ItemStack newItemUnnamed = new ItemStack(currentItem);
				ItemMeta newItemUnnamedMeta = newItemUnnamed.getItemMeta();
				newItemUnnamedMeta.setDisplayName(null);
				newItemUnnamed.setItemMeta(newItemUnnamedMeta);
				newItemUnnamed.setAmount(1);
				player.getInventory().addItem(newItemUnnamed);
				
				// Update soon
				Bukkit.getScheduler().runTask(CreativeGates.get(), new Runnable()
				{
					@SuppressWarnings("deprecation")
					@Override
					public void run()
					{
						player.updateInventory();
					}
				});
				
				// (message)
				message = Txt.parse("<i>%s aparenta ter perdido poder e não pode mais ser usado.", Txt.getMaterialName(material));
				player.sendMessage(message);
				
			}
		}
		else
		{
			// ... we are trying to do something else that create ...
			
			// ... and there is a gate ...
			if (currentGate == null)
			{
				// ... and there is no gate ...
				if (isPortalNearby(clickedBlock))
				{
					// ... but there is portal nearby.
					
					// ... exit with a message.
					player.sendMessage(Txt.parse("<i>Você usa %s em %s, mas parece não haver um portal.", Txt.getMaterialName(material), Txt.getMaterialName(clickedBlock.getType())));
					return;
				}
				else
				{
					// ... and there is no portal nearby ...
					
					// ... exit quietly.
					return;
				}
			}
			
			// ... send use action description ...
			message = Txt.parse("<i>você usa %s em %s...", Txt.getMaterialName(material), Txt.getMaterialName(clickedBlock.getType()));
			player.sendMessage(message);
			
			// ... check restriction ...
			if (currentGate.isRestricted())
			{
				if (currentGate.isCreator(player))
				{
					message = Txt.parse("<i>Este portal está restrito, mas você é o criador.");
					player.sendMessage(message);
				}
				else
				{
					message = Txt.parse("<b>Este portal está restrito e você não é o criador.");
					player.sendMessage(message);
					return;
				}
			}
			
			if (material == uconf.getMaterialInspect())
			{
				// ... we are trying to inspect ...
				message = Txt.parse("<i>São reveladas inscrições no portal:");
				player.sendMessage(message);
				
				message = Txt.parse("<k>rede: <v>%s", currentGate.getNetworkId());
				player.sendMessage(message);
				
				message = Txt.parse("<k>portais: <v>%d", currentGate.getGateChain().size());
				player.sendMessage(message);
			}
			else if (material == uconf.getMaterialSecret())
			{
				// ... we are trying to change secret state ...
				
				boolean creator = currentGate.isCreator(player);
				if (creator)
				{
					boolean secret = !currentGate.isRestricted();
					currentGate.setRestricted(secret);
					
					message = (secret ? Txt.parse("<h>Somente você <i>pode ler as inscrições agora.") : Txt.parse("<h>Qualquer pessoa <i>pode ler as inscrições."));
					player.sendMessage(message);
				}
				else
				{
					message = Txt.parse("<i>Aparentemente, <h>somente o criador do portal <i>pode alterar a legibilidade do portal.", Txt.getMaterialName(material), Txt.getMaterialName(clickedBlock.getType()));
					player.sendMessage(message);
				}
			}
			else if (material == uconf.getMaterialMode())
			{
				// ... we are trying to change mode ...
				
				currentGate.toggleMode();
				
				String enter = currentGate.isEnterEnabled() ? Txt.parse("<g>entrada habilitada") : Txt.parse("<b>entrada desabilitada");
				String exit = currentGate.isExitEnabled() ? Txt.parse("<g>saída habilitada") : Txt.parse("<b>saída desabilitada");
				
				message = Txt.parse("<i>Agora, o portal tem %s <i>e %s<i>.", enter, exit);
				player.sendMessage(message);
			}
			
		}
		
    }
	
}
