package com.massivecraft.creativegates.entity;

import java.util.List;

import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;

public class MConf extends Entity<MConf>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	protected static transient MConf i;
	public static MConf get() { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public List<String> aliasesCg = MUtil.list("cg", "creativegates", "creativegate");
	
	public boolean teleportationSoundActive = true;
	public boolean teleportationMessageActive = true;
	
}
