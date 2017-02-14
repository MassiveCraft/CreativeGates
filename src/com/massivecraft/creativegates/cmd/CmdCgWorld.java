package com.massivecraft.creativegates.cmd;

import com.massivecraft.creativegates.Perm;
import com.massivecraft.creativegates.entity.MConf;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

import java.util.List;

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
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(Perm.CG_WORLD));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public List<String> getAliases()
	{
		return MConf.get().aliasesCgWorld;
	}
	
}
