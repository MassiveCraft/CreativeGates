package com.massivecraft.creativegates.entity;

import java.util.List;

import org.bukkit.permissions.PermissionDefault;

import com.massivecraft.creativegates.Perm;
import com.massivecraft.mcore.store.Entity;
import com.massivecraft.mcore.util.MUtil;
import com.massivecraft.mcore.util.PermUtil;

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
	
	public PermissionDefault permissionDefaultCreate = PermissionDefault.TRUE;
	
	public PermissionDefault permissionDefaultUse = PermissionDefault.TRUE;
	
	public void updatePerms()
	{
		PermUtil.get(false, true, Perm.CREATE.node, "create a gate", this.permissionDefaultCreate);
		PermUtil.get(false, true, Perm.USE.node, "use a gate", this.permissionDefaultUse);
	}
	
}
