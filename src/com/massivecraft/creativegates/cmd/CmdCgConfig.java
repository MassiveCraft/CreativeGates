package com.massivecraft.creativegates.cmd;

import com.massivecraft.creativegates.Perm;
import com.massivecraft.creativegates.entity.MConf;
import com.massivecraft.massivecore.command.editor.CommandEditSingleton;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

public class CmdCgConfig extends CommandEditSingleton<MConf>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdCgConfig()
	{
		super(MConf.get());
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(Perm.CG_CONFIG));
	}
	
}
