package com.massivecraft.creativegates.zcore.util;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.massivecraft.creativegates.zcore.MPlugin;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;


public class PermUtil {
	
	private PermissionManager pex = null;
	private PermissionHandler perm2or3 = null;
	public Map<String, String> permissionDescriptions = new HashMap<String, String>();
	
	//public static Map<String,Map<String,JsonElement>> permvars = new HashMap<String,Map<String,JsonElement>>();
	
	protected MPlugin p;
	
	public PermUtil(MPlugin p)
	{
		this.p = p;
		this.setup();
	}
	
	/**
	 * This method does the same thing as "has" but will also send "me"
	 * a warning message using the description of the permission if available.
	 */
	public boolean test (CommandSender me, String perm)
	{
		if (has(me, perm))
		{
			return true;
		}
		me.sendMessage(this.getForbiddenMessage(perm));
		return false;
	}
	
	public String getForbiddenMessage(String perm)
	{
		return p.txt.get("perm.forbidden", getPermissionDescription(perm));
	}
	
	/**
	 * This method hooks into all permission plugins we are supporting
	 */
	public void setup()
	{
		if ( Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionsEx"))
		{
			pex = PermissionsEx.getPermissionManager();
			p.log("Will use this plugin for permissions: " + Bukkit.getServer().getPluginManager().getPlugin("PermissionsEx").getDescription().getFullName());
			return;
		}		
		
		if ( Bukkit.getServer().getPluginManager().isPluginEnabled("Permissions"))
		{
			Plugin permissionsPlugin = Bukkit.getServer().getPluginManager().getPlugin("Permissions");
			perm2or3 = ((Permissions) permissionsPlugin).getHandler();
			p.log("Will use this plugin for permissions: " + permissionsPlugin.getDescription().getFullName());
			return;
		}
		
	    p.log("No permission plugin detected. Defaulting to native bukkit permissions.");
	    
	    for(Permission permission : p.getDescription().getPermissions())
		{
			this.permissionDescriptions.put(permission.getName(), permission.getDescription());
		}
	}

	public String getPermissionDescription (String perm)
	{
		String desc = permissionDescriptions.get(perm);
		if (desc == null)
		{
			return p.txt.get("perm.dothat");
		}
		return desc;
	}
	
	/**
	 * This method tests if me has a certain permission and returns
	 * true if me has. Otherwise false
	 */
	public boolean has (CommandSender me, String perm)
	{
		if ( ! (me instanceof Player))
		{
			return me.hasPermission(perm);
		}
		
		if (pex != null)
		{
			return pex.has((Player)me, perm);
		} 
		
		if (perm2or3 != null)
		{
			return perm2or3.has((Player)me, perm);
		}
		
		return me.hasPermission(perm);
	}
	
	/*
	public JsonElement getVar(CommandSender me, String varname)
	{
		Map<String,JsonElement> perm2val = permvars.get(varname);
		if (perm2val == null) return null;
		JsonElement ret = null;
		for ( Entry<String, JsonElement> entry : perm2val.entrySet())
		{
			ret = entry.getValue();
			if (has(me, entry.getKey())) break;
		}
		return ret;
	}

	public Map<String, Map<String, JsonElement>> getPermvars()
	{
		return permvars;
	}

	public void setPermvars(Map<String, Map<String, JsonElement>> permvars)
	{
		PermUtil.permvars = permvars;
	}*/
	
}
