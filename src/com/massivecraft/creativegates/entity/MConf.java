package com.massivecraft.creativegates.entity;

import java.util.List;

import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import org.bukkit.permissions.PermissionDefault;

import com.massivecraft.creativegates.Perm;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.PermissionUtil;

@EditorName("config")
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
	
	// Aliases
	public List<String> aliasesCg = MUtil.list("cg", "creativegates", "creativegate");
	public List<String> aliasesCgWorld = MUtil.list("world");
	public List<String> aliasesCgWorldList = MUtil.list("list");
	public List<String> aliasesCgWorldDelete = MUtil.list("delete");
	public List<String> aliasesCgConfig = MUtil.list("config");
	public List<String> aliasesCgVersion = MUtil.list("v", "version");
	
	public boolean teleportationSoundActive = true;
	public boolean teleportationMessageActive = true;
	
	public PermissionDefault permissionDefaultCreate = PermissionDefault.TRUE;
	public PermissionDefault permissionDefaultUse = PermissionDefault.TRUE;

	public void updatePerms()
	{
		PermissionUtil.getPermission(false, true, Perm.CREATE.getId(), "create a gate", this.permissionDefaultCreate);
		PermissionUtil.getPermission(false, true, Perm.USE.getId(), "use a gate", this.permissionDefaultUse);
	}
	
}
