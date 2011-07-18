package com.massivecraft.creativegates;

import org.bukkit.entity.Player;

public enum Permission {
	CREATE("create gates", "creativegates.create"),
	DESTROY("destroy gates", "creativegates.destroy"),
	USE("use gates", "creativegates.use");
	
	public final String description;
	public final String permissionNode;
	
	Permission(final String description, final String permissionNode) {
        this.description = description;
		this.permissionNode = permissionNode;
    }
	
	public boolean has(Player player) {
		return player.hasPermission(permissionNode);
	}
	
	public boolean test(Player player) {
		if (has(player)) {
			return true;
		}
		player.sendMessage(this.getForbiddenMessage());
		return false;
	}
	
	public String getForbiddenMessage() {
		return Conf.colorDefault+"You don't have permission to "+this.description;
	}
	
}
