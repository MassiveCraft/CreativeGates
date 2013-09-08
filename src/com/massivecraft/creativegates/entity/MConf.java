package com.massivecraft.creativegates.entity;

import org.bukkit.permissions.PermissionDefault;

import com.massivecraft.creativegates.Perm;
import com.massivecraft.mcore.store.Entity;
import com.massivecraft.mcore.util.PermUtil;

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
	
	protected PermissionDefault permissionDefaultCreate = PermissionDefault.TRUE;
	public PermissionDefault getPermissionDefaultCreate()
	{
		return this.permissionDefaultCreate;
	}
	public void setPermissionDefaultCreate(PermissionDefault permissionDefaultCreate)
	{
		this.permissionDefaultCreate = permissionDefaultCreate;
		this.changed();
		PermUtil.get(false, true, Perm.CREATE.node, "create a gate", this.permissionDefaultCreate);
	}
	
	protected PermissionDefault permissionDefaultUse = PermissionDefault.TRUE;
	public PermissionDefault getPermissionDefaultUse()
	{
		return this.permissionDefaultUse;
	}
	public void setPermissionDefaultUse(PermissionDefault permissionDefaultUse)
	{
		this.permissionDefaultUse = permissionDefaultUse;
		this.changed();
		PermUtil.get(false, true, Perm.USE.node, "use a gate", this.permissionDefaultUse);
	}
	
}
