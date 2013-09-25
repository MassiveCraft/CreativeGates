package com.massivecraft.creativegates.cmd;

import com.massivecraft.creativegates.ConfServer;
import com.massivecraft.creativegates.CreativeGates;
import com.massivecraft.creativegates.Perm;
import com.massivecraft.mcore.cmd.HelpCommand;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.VersionCommand;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdCg extends MCommand
{
	// SubCommands
	public CmdCgWorld cmdCgWorld = new CmdCgWorld();
	public VersionCommand cmdCgVersion = new VersionCommand(CreativeGates.get(), Perm.CG_VERSION.node, "v", "version");
	
	public CmdCg()
	{
		// Aliases
		this.addAliases(ConfServer.aliasesCg);
		
		// Help SubCommand 
		this.addSubCommand(HelpCommand.get());
		
		// Add SubCommands
		this.addSubCommand(this.cmdCgWorld);
		this.addSubCommand(this.cmdCgVersion);
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(Perm.CG.node));
	}
	
	@Override
	public void perform()
	{
		this.getCommandChain().add(this);
		HelpCommand.getInstance().execute(this.sender, this.args, this.commandChain);
	}
}
