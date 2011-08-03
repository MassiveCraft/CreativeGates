package com.massivecraft.creativegates;

import org.bukkit.command.CommandSender;

import com.massivecraft.core.util.Perm;


public enum Permission {
	USE("creativegates.use"),
	CREATE("creativegates.create"),
	DESTROY("creativegates.destroy"),
	;
	
	public final String node;
	
	Permission(final String node) {
		this.node = node;
    }
	
	public boolean has(CommandSender sender) {
		return Perm.has(sender, this.node);
	}
	
	public boolean test(CommandSender sender) {
		return CreativeGates.p.perm.test(sender, this.node);
	}
}
