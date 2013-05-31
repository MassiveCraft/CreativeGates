package com.massivecraft.creativegates.entity;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;

import com.massivecraft.mcore.store.Entity;
import com.massivecraft.mcore.util.MUtil;

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
		this.setMaxarea(that.getMaxarea());
		this.setBlocksrequired(that.getBlocksrequired());
		this.setMaterialCreate(that.getMaterialCreate());
		this.setMaterialInspect(that.getMaterialInspect());
		this.setMaterialMode(that.getMaterialMode());
		
		return this;
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private int maxarea = 200;
	public int getMaxarea() { return this.maxarea; }
	public void setMaxarea(int maxarea) { this.maxarea = maxarea; this.changed(); }
	
	private Map<Material, Integer> blocksrequired = MUtil.map(
		Material.EMERALD_BLOCK, 2
	);
	public Map<Material, Integer> getBlocksrequired() { return new HashMap<Material, Integer>(this.blocksrequired); }
	public void setBlocksrequired(Map<Material, Integer> blocksrequired) { this.blocksrequired = new HashMap<Material, Integer>(blocksrequired); this.changed(); }
	
	private Material materialCreate = Material.WATCH;
	public Material getMaterialCreate() { return this.materialCreate; }
	public void setMaterialCreate(Material materialCreate) { this.materialCreate = materialCreate; this.changed(); }
	
	private Material materialInspect = Material.WATCH;
	public Material getMaterialInspect() { return this.materialInspect; }
	public void setMaterialInspect(Material materialInspect) { this.materialInspect = materialInspect; this.changed(); }
	
	private Material materialSecret = Material.COAL;
	public Material getMaterialSecret() { return this.materialSecret; }
	public void setMaterialSecret(Material materialSecret) { this.materialSecret = materialSecret; this.changed(); }
	
	private Material materialMode = Material.COMPASS;
	public Material getMaterialMode() { return this.materialMode; }
	public void setMaterialMode(Material materialMode) { this.materialMode = materialMode; this.changed(); }
	
}
