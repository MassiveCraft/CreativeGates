package com.massivecraft.creativegates;

import org.bukkit.entity.Player;

public enum Permission {
	CREATE("create gates", "creativegates.create", false),
	DESTROY("destroy gates", "creativegates.destroy", false);
	
	public final String description;
	public final String permissionNode;
	public final boolean defaultOpOnly;
	
	Permission(final String description, final String permissionNode, final boolean defaultOpOnly) {
        this.description = description;
		this.permissionNode = permissionNode;
        this.defaultOpOnly = defaultOpOnly;
    }
	
	public boolean has(Player player) {
		if (P.permissionHandler == null) {
			if (this.defaultOpOnly) {
				return player.isOp();
			}
			return true;
		}
		
		return P.permissionHandler.has(player, permissionNode);
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
