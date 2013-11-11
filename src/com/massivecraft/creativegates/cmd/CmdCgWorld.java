package com.massivecraft.creativegates.cmd;

import com.massivecraft.creativegates.Perm;
import com.massivecraft.mcore.cmd.HelpCommand;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdCgWorld extends MCommand
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
		this.addSubCommand(HelpCommand.get());
		this.addSubCommand(this.cmdCgWorldList);
		this.addSubCommand(this.cmdCgWorldDelete);
		
		// Aliases
		this.addAliases("world");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(Perm.CG_WORLD.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		this.getCommandChain().add(this);
		HelpCommand.get().execute(this.sender, this.args, this.commandChain);
	}
	
}
