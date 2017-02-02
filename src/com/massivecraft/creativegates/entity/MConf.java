package com.massivecraft.creativegates.entity;

import java.util.List;

import com.massivecraft.creativegates.cmd.type.TypePermissionDefault;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.command.editor.annotation.EditorType;
import com.massivecraft.massivecore.command.editor.annotation.EditorTypeInner;
import com.massivecraft.massivecore.command.type.TypePermission;
import com.massivecraft.massivecore.command.type.TypeStringCommand;
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
	
	public List<String> aliasesCg = MUtil.list("cg", "creativegates", "creativegate");
	
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
