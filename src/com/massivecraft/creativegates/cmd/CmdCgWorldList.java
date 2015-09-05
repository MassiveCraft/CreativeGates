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
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.ArgSetting;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;

public class CmdCgWorldList extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdCgWorldList()
	{
		// Aliases
		this.addAliases("l", "list");
		
		// Args
		this.addArg(ArgSetting.getPage());
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(Perm.CG_WORLD_LIST.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Args
		int page = this.readArg();
		
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
		this.message(Txt.getPage(lines, page, "Gates per World", this));	
	}
	
}
