package com.massivecraft.creativegates.cmd;

import com.massivecraft.creativegates.Perm;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

public class CmdCgWorld extends MassiveCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdCgWorldList cmdCgWorldList = new CmdCgWorldList();
	public CmdCgWorldDelete cmdCgWorldDelete = new CmdCgWorldDelete();
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdCgWorld()
	{
		// Children
		this.addChild(this.cmdCgWorldList);
		this.addChild(this.cmdCgWorldDelete);
		
		// Aliases
		this.addAliases("world");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(Perm.CG_WORLD.node));
	}
	
}
