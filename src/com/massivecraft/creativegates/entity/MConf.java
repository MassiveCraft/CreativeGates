package com.massivecraft.creativegates.entity;

import java.util.List;

import org.bukkit.permissions.PermissionDefault;

import com.massivecraft.creativegates.Perm;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.PermissionUtil;

public class MConf extends Entity<MConf>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	protected static transient MConf i;
	public static MConf get() { return i; }
	
	@Override
	public MConf load(MConf that)
	{
		super.load(that);
		this.updatePerms();
		return this;
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public List<String> aliasesCg = MUtil.list("cg", "creativegates", "creativegate");
	
	public boolean teleportationSoundActive = true;
	public boolean teleportationMessageActive = true;
	
	public PermissionDefault permissionDefaultCreate = PermissionDefault.TRUE;
	public PermissionDefault permissionDefaultUse = PermissionDefault.TRUE;

	public void updatePerms()
	{
		PermissionUtil.get(false, true, Perm.CREATE.node, "create a gate", this.permissionDefaultCreate);
		PermissionUtil.get(false, true, Perm.USE.node, "use a gate", this.permissionDefaultUse);
	}
	
}
