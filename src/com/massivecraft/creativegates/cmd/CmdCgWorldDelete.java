package com.massivecraft.creativegates.cmd;

import com.massivecraft.creativegates.Perm;
import com.massivecraft.creativegates.entity.UGate;
import com.massivecraft.creativegates.entity.UGateColl;
import com.massivecraft.creativegates.entity.UGateColls;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

public class CmdCgWorldDelete extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdCgWorldDelete()
	{
		// Aliases
		this.addAliases("delete");
		
		// Args
		this.addRequiredArg("world");
		this.setErrorOnToManyArgs(false);
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(Perm.CG_WORLD_DELETE.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		// Args
		String world = this.argConcatFrom(0);
		if (world == null) return;
		
		// Apply
		int countDeleted = 0;
		for (UGateColl coll : UGateColls.get().getColls())
		{
			for (UGate gate : coll.getAll())
			{
				if (world.equals(gate.getExit().getWorld()))
				{
					gate.destroy();
					countDeleted++;
				}
			}
		}
		
		// Inform
		msg("<i>Deleted all <h>%d <i>gates in world <h>%s<i>.", countDeleted, world);
	}
	
}
