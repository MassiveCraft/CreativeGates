package com.massivecraft.creativegates.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.creativegates.CreativeGates;
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.mixin.TeleporterException;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.SmokeUtil;
import com.massivecraft.massivecore.util.Txt;

public class UGate extends Entity<UGate>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	public static UGate get(Object oid)
	{
		return UGateColls.get().get2(oid);
	}
	
	// -------------------------------------------- //
	// OVERRIDE: ENTITY
	// -------------------------------------------- //
	
	@Override
	public UGate load(UGate that)
	{
		this.setCreatorId(that.getCreatorId());
		this.setCreatedMillis(that.getCreatedMillis());
		this.setUsedMillis(that.getUsedMillis());
		this.setNetworkId(that.getNetworkId());
		this.setRestricted(that.isRestricted());
		this.setEnterEnabled(that.isEnterEnabled());
		this.setExitEnabled(that.isExitEnabled());
		this.setExit(that.getExit());
		this.setCoords(that.getCoords());
		
		return this;
	}
	
	@Override
	public void postAttach(String id)
	{
		if (this.getExit() == null) return;
		CreativeGates.get().getIndex().add(this);
	}
	
	@Override
	public void postDetach(String id)
	{
		if (this.getExit() == null) return;
		CreativeGates.get().getIndex().remove(this);
	}
	
	@Override
	public UGateColl getColl()
	{
		return (UGateColl) super.getColl();
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private String creatorId = null;
	public String getCreatorId() { return this.creatorId; }
	public void setCreatorId(String creatorId) { this.creatorId = creatorId; this.changed(); }
	
	private long createdMillis = System.currentTimeMillis();
	public long getCreatedMillis() { return this.createdMillis; }
	public void setCreatedMillis(long createdMillis) { this.createdMillis = createdMillis; this.changed(); }
	
	private long usedMillis = 0;
	public long getUsedMillis() { return this.usedMillis; }
	public void setUsedMillis(long usedMillis) { this.usedMillis = usedMillis; this.changed(); }
	
	private String networkId = null;
	public String getNetworkId() { return this.networkId; }
	public void setNetworkId(String networkId) { this.networkId = networkId; this.changed(); }
	
	private boolean restricted = false;
	public boolean isRestricted() { return this.restricted; }
	public void setRestricted(boolean restricted) { this.restricted = restricted; this.changed(); }
	
	private boolean enterEnabled = true;
	public boolean isEnterEnabled() { return this.enterEnabled; }
	public void setEnterEnabled(boolean enterEnabled) { this.enterEnabled = enterEnabled; this.changed(); }
	
	private boolean exitEnabled = true;
	public boolean isExitEnabled() { return this.exitEnabled; }
	public void setExitEnabled(boolean exitEnabled) { this.exitEnabled = exitEnabled; this.changed(); }
	
	private PS exit = null;
	public PS getExit() { return this.exit; }
	public void setExit(PS exit) { this.exit = exit; this.changed(); }
	
	private Set<PS> coords = new TreeSet<PS>();
	public Set<PS> getCoords() { return Collections.unmodifiableSet(this.coords);}
	public void setCoords(Collection<PS> coords)
	{
		if (this.attached()) CreativeGates.get().getIndex().remove(this);
		
		this.coords = new TreeSet<PS>(coords);
		
		if (this.attached()) CreativeGates.get().getIndex().add(this);
			
		this.changed();
	}
	
	// -------------------------------------------- //
	// ASSORTED
	// -------------------------------------------- //
	
	public boolean isCreator(CommandSender sender)
	{
		String senderId = IdUtil.getId(sender);
		if (senderId == null) return false;
		return senderId.equalsIgnoreCase(this.creatorId);
	}
	
	public void destroy()
	{
		this.empty();
		this.detach();
		this.fxKitDestroy(null);
	}
	
	public void toggleMode()
	{
		boolean enter = this.isEnterEnabled();
		boolean exit = this.isExitEnabled();
		
		if (enter == false && exit == false)
		{
			this.setEnterEnabled(true);
			this.setExitEnabled(false);
		}
		else if (enter == true && exit == false)
		{
			this.setEnterEnabled(false);
			this.setExitEnabled(true);
		}
		else if (enter == false && exit == true)
		{
			this.setEnterEnabled(true);
			this.setExitEnabled(true);
		}
		else if (enter == true && exit == true)
		{
			this.setEnterEnabled(false);
			this.setExitEnabled(false);
		}
	}
	
	// -------------------------------------------- //
	// TRANSPORT
	// -------------------------------------------- //
	
	public void transport(Player player)
	{
		List<UGate> gateChain = this.getGateChain();
		
		String message = null;
		
		for (UGate ugate : gateChain)
		{
			if (!ugate.isExitEnabled()) continue;
			PS destinationPs = ugate.getExit();
			try
			{
				Mixin.teleport(player, destinationPs, MConf.get().teleportationMessageActive ? "the gate destination" : null, 0);
				this.setUsedMillis(System.currentTimeMillis());
				this.fxKitUse(player);
				return;
			}
			catch (TeleporterException e)
			{
				player.sendMessage(e.getMessage());
			}
		}
		
		message = Txt.parse("<i>This gate does not seem to lead anywhere.");
		player.sendMessage(message);
	}
	
	public List<UGate> getGateChain()
	{
		List<UGate> ret = new ArrayList<UGate>();
		
		List<UGate> rawchain = this.getColl().getGateChain(this.getNetworkId());
		int myIndex = rawchain.indexOf(this);
		
		// Add what is after me
		ret.addAll(rawchain.subList(myIndex+1, rawchain.size()));
		
		// Add what is before me
		ret.addAll(rawchain.subList(0, myIndex));
		
		return ret;
	}
	
	// -------------------------------------------- //
	// CONTENT
	// -------------------------------------------- //
	
	// These blocks are sorted since the coords are sorted
	public List<Block> getBlocks()
	{
		List<Block> ret = new ArrayList<Block>();
		
		World world = null;
		try
		{
			world = this.getExit().asBukkitWorld(true);
		}
		catch (IllegalStateException e)
		{
			return null;
		}
		
		for (PS coord : this.getCoords())
		{
			Block block = world.getBlockAt(coord.getBlockX(), coord.getBlockY(), coord.getBlockZ());
			ret.add(block);
		}
		
		return ret;
	}
	
	public Block getCenterBlock()
	{
		List<Block> blocks = this.getBlocks();
		if (blocks == null) return null;
		
		return blocks.get(blocks.size() / 2);
	}
	
	public boolean isIntact()
	{
		List<Block> blocks = this.getBlocks();
		if (blocks == null) return true;
		
		for (Block block : blocks)
		{
			if (CreativeGates.isVoid(block))
			{
				return false;
			}
		}
		return true;
	}
	
	public void setContent(Material material)
	{
		List<Block> blocks = this.getBlocks();
		if (blocks == null) return;
		
		for (Block block : blocks)
		{
			Material blockMaterial = block.getType();
			if (blockMaterial == Material.PORTAL || blockMaterial == Material.STATIONARY_WATER || blockMaterial == Material.WATER || CreativeGates.isVoid(blockMaterial))
			{
				block.setType(material);
			}
		}
	}
	
	public void fill()
	{
		UConf uconf = UConf.get(this.getExit());
		CreativeGates.get().setFilling(true);
		this.setContent(uconf.isUsingWater() ? Material.STATIONARY_WATER : Material.PORTAL);
		CreativeGates.get().setFilling(false);
	}
	
	public void empty()
	{
		this.setContent(Material.AIR);
	}
	
	// -------------------------------------------- //
	// FX KIT
	// -------------------------------------------- //

	public void fxKitCreate(Player player)
	{
		//this.fxSmoke();
		this.fxShootSound();
	}
	
	public void fxKitUse(Player player)
	{
		//this.fxEnder();
		if (!MConf.get().teleportationSoundActive) return;
		this.fxShootSound();
	}
	
	public void fxKitDestroy(Player player)
	{
		this.fxExplode();
	}
	
	// -------------------------------------------- //
	// FX SINGLE
	// -------------------------------------------- //
	
	public void fxSmoke()
	{
		List<Block> blocks = this.getBlocks();
		if (blocks == null) return;
		for (Block block : blocks)
		{
			SmokeUtil.spawnCloudSimple(block.getLocation());
		}
	}
	
	public void fxEnder()
	{
		List<Block> blocks = this.getBlocks();
		if (blocks == null) return;
		for (Block block : blocks)
		{
			Location location = block.getLocation();
			location.getWorld().playEffect(location, Effect.ENDER_SIGNAL, 0);
		}
	}
	
	public void fxExplode()
	{
		Block block = this.getCenterBlock();
		if (block == null) return;
		
		Location location = block.getLocation();
		
		SmokeUtil.fakeExplosion(location);
	}
	
	public void fxShootSound()
	{
		Block block = this.getCenterBlock();
		if (block == null) return;
		
		Location location = block.getLocation();
		
		location.getWorld().playEffect(location, Effect.GHAST_SHOOT, 0);
	}
	
}
