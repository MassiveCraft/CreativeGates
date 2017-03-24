package com.massivecraft.creativegates.entity;

import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class UConf extends Entity<UConf>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	public static UConf get(Object oid)
	{
		return UConfColls.get().get2(oid);
	}
	
	// -------------------------------------------- //
	// OVERRIDE: ENTITY
	// -------------------------------------------- //
	
	@Override
	public UConf load(UConf that)
	{
		this.setEnabled(that.isEnabled());
		this.setUsingWater(that.isUsingWater());
		this.setPigmanPortalSpawnAllowed(that.isPigmanPortalSpawnAllowed());
		this.setMaxarea(that.getMaxarea());
		this.setBlocksrequired(that.getBlocksrequired());
		this.setRemovingCreateToolName(that.isRemovingCreateToolName());
		this.setRemovingCreateToolItem(that.isRemovingCreateToolItem());
		this.setMaterialCreate(that.getMaterialCreate());
		this.setMaterialInspect(that.getMaterialInspect());
		this.setMaterialSecret(that.getMaterialSecret());
		this.setMaterialMode(that.getMaterialMode());
		
		return this;
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private boolean enabled = true;
	public boolean isEnabled() { return this.enabled; }
	public void setEnabled(boolean enabled) { this.enabled = enabled; this.changed(); }
	
	private boolean usingWater = false;
	public boolean isUsingWater() { return this.usingWater; }
	public void setUsingWater(boolean usingWater) { this.usingWater = usingWater; this.changed(); }
	
	private boolean pigmanPortalSpawnAllowed = true;
	public boolean isPigmanPortalSpawnAllowed() { return this.pigmanPortalSpawnAllowed; }
	public void setPigmanPortalSpawnAllowed(boolean pigmanPortalSpawnAllowed) { this.pigmanPortalSpawnAllowed = pigmanPortalSpawnAllowed; this.changed(); }
	
	private int maxarea = 200;
	public int getMaxarea() { return this.maxarea; }
	public void setMaxarea(int maxarea) { this.maxarea = maxarea; this.changed(); }
	
	private Map<Material, Integer> blocksrequired = MUtil.map(
		Material.EMERALD_BLOCK, 2
	);
	public Map<Material, Integer> getBlocksrequired() { return new HashMap<>(this.blocksrequired); }
	public void setBlocksrequired(Map<Material, Integer> blocksrequired) { this.blocksrequired = new HashMap<>(blocksrequired); this.changed(); }
	
	private boolean removingCreateToolName = true;
	public boolean isRemovingCreateToolName() { return this.removingCreateToolName; }
	public void setRemovingCreateToolName(boolean removingCreateToolName) { this.removingCreateToolName = removingCreateToolName; this.changed(); }
	
	private boolean removingCreateToolItem = false;
	public boolean isRemovingCreateToolItem() { return this.removingCreateToolItem; }
	public void setRemovingCreateToolItem(boolean removingCreateToolItem) { this.removingCreateToolItem = removingCreateToolItem; this.changed(); }
	
	private Material materialCreate = Material.WATCH;
	public Material getMaterialCreate() { return this.materialCreate; }
	public void setMaterialCreate(Material materialCreate) { this.materialCreate = materialCreate; this.changed(); }
	
	private Material materialInspect = Material.BLAZE_POWDER;
	public Material getMaterialInspect() { return this.materialInspect; }
	public void setMaterialInspect(Material materialInspect) { this.materialInspect = materialInspect; this.changed(); }
	
	private Material materialSecret = Material.MAGMA_CREAM;
	public Material getMaterialSecret() { return this.materialSecret; }
	public void setMaterialSecret(Material materialSecret) { this.materialSecret = materialSecret; this.changed(); }
	
	private Material materialMode = Material.BLAZE_ROD;
	public Material getMaterialMode() { return this.materialMode; }
	public void setMaterialMode(Material materialMode) { this.materialMode = materialMode; this.changed(); }
	
}
