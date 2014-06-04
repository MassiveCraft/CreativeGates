package com.massivecraft.creativegates;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

import com.massivecraft.creativegates.entity.UGate;
import com.massivecraft.creativegates.entity.UGateColl;
import com.massivecraft.creativegates.entity.UGateColls;
import com.massivecraft.massivecore.EngineAbstract;
import com.massivecraft.massivecore.event.EventMassiveCoreUuidUpdate;
import com.massivecraft.massivecore.util.IdUpdateUtil;
import com.massivecraft.massivecore.util.MUtil;

public class EngineIdUpdate extends EngineAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineIdUpdate i = new EngineIdUpdate();
	public static EngineIdUpdate get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return CreativeGates.get();
	}
	
	// -------------------------------------------- //
	// LISTENER
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.MONITOR)
	public void update(EventMassiveCoreUuidUpdate event)
	{
		for (UGateColl coll : UGateColls.get().getColls())
		{
			for (UGate entity : coll.getAll())
			{
				update(coll, entity);
			}
		}
	}
	
	public static void update(UGateColl coll, UGate entity)
	{
		// Before and After
		String before = entity.getCreatorId();
		if (before == null) return;
		String after = IdUpdateUtil.update(before, false);
		if (after == null) return;
		
		// NoChange
		if (MUtil.equals(before, after)) return;
		
		// Apply
		entity.setCreatorId(after);
		entity.sync();
	}

}
