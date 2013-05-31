package com.massivecraft.creativegates.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.massivecraft.creativegates.CreativeGates;
import com.massivecraft.creativegates.Perm;
import com.massivecraft.creativegates.entity.UConf;
import com.massivecraft.creativegates.entity.UGate;
import com.massivecraft.mcore.ps.PS;
import com.massivecraft.mcore.util.Txt;

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
		
		// ... and the item in hand ...
		final ItemStack item = event.getItem();
		final Material material = item.getType();
		
		// ... is in any way an interesting material ...
		final UConf uconf = UConf.get(player);
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
				message = Txt.parse("<b>There is no room for a new gate since there already is one here.");
				player.sendMessage(message);
				return;
			}
			
			// ... check if the item is named ...
			ItemMeta meta = item.getItemMeta();
			if (!meta.hasDisplayName())
			{
				message = Txt.parse("<i>You must name the %s before creating a gate with it.", Txt.getMaterialName(material));
				player.sendMessage(message);
				return;
			}
			String newNetworkId = ChatColor.stripColor(meta.getDisplayName());
			
			// ... perform the flood fill ...
			// TODO
			
			// ... calculate the exit location ...
			PS exit = PS.valueOf(player.getLocation());
			// TODO
			
			// ... ensure the required blocks are present ...
			// TODO
			
			// ... set the air blocks to portal material ...
			// TODO
			
			// ... create the gate ...
			// TODO
			
			// ... run fx ...
			// TODO
			
			// ... inform the player.
			// TODO
		}
		else if (material == uconf.getMaterialInspect())
		{
			// ... we are trying to inspect ...
			if (currentGate != null)
			{
				// ... and there is a gate ...
				message = Txt.parse("<i>You use the %s on the %s and these gate inscriptions are revealed:", Txt.getMaterialName(material), Txt.getMaterialName(clickedBlock.getType()));
				player.sendMessage(message);
				
				boolean creator = currentGate.isCreator(player);
				boolean secret = currentGate.isNetworkSecret();
				boolean hidden = (secret && !creator);
				
				message = Txt.parse("<k>network: <v>") + (hidden ? Txt.parse("<magic>asdfasdfasdf") : currentGate.getNetworkId());
				if (secret) message += Txt.parse(" <i>(secret)");
				player.sendMessage(message);
				
				// TODO: Show more info here?
			}
			else
			{
				// ... and there is no gate ...
				if (isPortalNearby(clickedBlock))
				{
					// ... but there is portal nearby.
					
					// ... exit with a message.
					player.sendMessage(Txt.parse("<i>You use the %s on the %s but no gate inscriptions are revealed.", Txt.getMaterialName(material), Txt.getMaterialName(clickedBlock.getType())));
					return;
				}
				else
				{
					// ... and there is no portal nearby ...
					
					// ... exit quietly.
					return;
				}
			}
		}
		else if (material == uconf.getMaterialSecret())
		{
			// ... we are trying to change secret state ...
			if (currentGate != null)
			{
				// ... and there is a gate ...
				boolean creator = currentGate.isCreator(player);
				if (creator)
				{
					boolean secret = !currentGate.isNetworkSecret();
					currentGate.setNetworkSecret(secret);
					
					message = Txt.parse("<i>You use the %s on the %s.", Txt.getMaterialName(material), Txt.getMaterialName(clickedBlock.getType()));
					player.sendMessage(message);
					
					message = (secret ? Txt.parse("<h>Only you <i>can read the gate inscriptions now.") : Txt.parse("<h>Anyone <i>can read the gate inscriptions now."));
					player.sendMessage(message);
				}
				else
				{
					message = Txt.parse("<i>You use the %s on the %s but the gate <h>ignores you<i>.", Txt.getMaterialName(material), Txt.getMaterialName(clickedBlock.getType()));
					player.sendMessage(message);
					
					message = Txt.parse("<i>It seems <h>only the gate creator <i>can change inscription readability.", Txt.getMaterialName(material), Txt.getMaterialName(clickedBlock.getType()));
					player.sendMessage(message);
				}
			}
			else
			{
				// ... and there is no gate ...
				if (isPortalNearby(clickedBlock))
				{
					// ... but there is portal nearby.
					
					// ... exit with a message.
					player.sendMessage(Txt.parse("<i>You use the %s on the %s but there seem to be no gate.", Txt.getMaterialName(material), Txt.getMaterialName(clickedBlock.getType())));
					return;
				}
				else
				{
					// ... and there is no portal nearby ...
					
					// ... exit quietly.
					return;
				}
			}
		}
		else if (material == uconf.getMaterialMode())
		{
			// ... we are trying to change mode ...
			// TODO
		}
	
    }
	
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
}
