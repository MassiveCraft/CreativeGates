package com.massivecraft.creativegates.old.zcore.util;

import java.util.*;
import java.util.Map.Entry;

import org.bukkit.command.CommandSender;

import com.massivecraft.creativegates.old.zcore.Lang;
import com.massivecraft.creativegates.old.zcore.MPlugin;


public class PermDEPR {
	
	public Map<String, String> permissionDescriptions = new HashMap<String, String>();
	
	protected MPlugin p;
	
	public PermDEPR(MPlugin p)
	{
		this.p = p;
	}
	
	public String getForbiddenMessage(String perm)
	{
		return p.txt.parse(Lang.permForbidden, getPermissionDescription(perm));
	}

	public String getPermissionDescription (String perm)
	{
		String desc = permissionDescriptions.get(perm);
		if (desc == null)
		{
			return Lang.permDoThat;
		}
		return desc;
	}
	
	/**
	 * This method tests if me has a certain permission and returns
	 * true if me has. Otherwise false
	 */
	public boolean has (CommandSender me, String perm)
	{
		return me.hasPermission(perm);
	}
	
	public boolean has (CommandSender me, String perm, boolean informSenderIfNot)
	{
		if (has(me, perm))
		{
			return true;
		}
		else if (informSenderIfNot)
		{
			me.sendMessage(this.getForbiddenMessage(perm));
		}
		return false;
	}
	
	public <T> T pickFirstVal(CommandSender me, Map<String, T> perm2val)
	{
		if (perm2val == null) return null;
		T ret = null;
		
		for ( Entry<String, T> entry : perm2val.entrySet())
		{
			ret = entry.getValue();
			if (has(me, entry.getKey())) break;
		}
		
		return ret;
	}
	
}
