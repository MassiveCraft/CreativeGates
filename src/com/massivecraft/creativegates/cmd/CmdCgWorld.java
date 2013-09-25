package com.massivecraft.creativegates.cmd;

import com.massivecraft.creativegates.Perm;
import com.massivecraft.mcore.cmd.HelpCommand;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdCgWorld extends MCommand
{
	// SubCommands
	public CmdCgWorldList cmdCgWorldList = new CmdCgWorldList();
	public CmdCgWorldDelete cmdCgWorldDelete = new CmdCgWorldDelete();
	
	public CmdCgWorld()
	{
		// Aliases
		this.addAliases("world");
		
		// Help SubCommand 
		this.addSubCommand(HelpCommand.get());
		
		// Add SubCommands
		this.addSubCommand(this.cmdCgWorldList);
		this.addSubCommand(this.cmdCgWorldDelete);
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(Perm.CG_WORLD.node));
	}
	
	@Override
	public void perform()
	{
		this.getCommandChain().add(this);
		HelpCommand.getInstance().execute(this.sender, this.args, this.commandChain);
	}
}
