package com.massivecraft.creativegates.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.massivecraft.creativegates.Perm;
import com.massivecraft.creativegates.entity.UGate;
import com.massivecraft.creativegates.entity.UGateColl;
import com.massivecraft.creativegates.entity.UGateColls;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.arg.ARInteger;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.mixin.Mixin;
import com.massivecraft.mcore.util.MUtil;
import com.massivecraft.mcore.util.Txt;

public class CmdCgWorldList extends MCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdCgWorldList()
	{
		// Aliases
		this.addAliases("l", "list");
		
		// Args
		this.addOptionalArg("page", "1");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(Perm.CG_WORLD_LIST.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		// Args
		Integer pageHumanBased = this.arg(0, ARInteger.get(), 1);
		if (pageHumanBased == null) return;
		
		// Create Lines
		List<String> lines = new ArrayList<String>();
		
		// count the gates
		Map<String, Integer> world2count = new HashMap<String, Integer>();
		int countAll = 0;
		for (UGateColl coll : UGateColls.get().getColls())
		{
			for (UGate gate : coll.getAll())
			{
				String world = gate.getExit().getWorld();
				Integer count = world2count.get(world);
				if (count == null) count = 0;
				count++;
				countAll++;
				world2count.put(world, count);
			}
		}
		
		// convert to lines
		for (Entry<String, Integer> entry : MUtil.entriesSortedByValues(world2count, false))
		{
			String world = entry.getKey();
			int count = entry.getValue();
			
			if (Mixin.getWorldIds().contains(world))
			{
				lines.add(Txt.parse("<v>%d <g>%s", count, world));
			}
			else
			{
				lines.add(Txt.parse("<v>%d <b>%s", count, world));
			}
		}
		lines.add(Txt.parse("<v>%d <k>%s", countAll, "SUM"));
		
		// Send Lines
		this.sendMessage(Txt.getPage(lines, pageHumanBased, "Gates per World", sender));	
	}
	
}
