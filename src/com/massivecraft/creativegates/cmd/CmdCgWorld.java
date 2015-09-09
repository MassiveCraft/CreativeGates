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
		// Add SubCommands
		this.addSubCommand(this.cmdCgWorldList);
		this.addSubCommand(this.cmdCgWorldDelete);
		
		// Aliases
		this.addAliases("world");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(Perm.CG_WORLD.node));
	}
	
}
