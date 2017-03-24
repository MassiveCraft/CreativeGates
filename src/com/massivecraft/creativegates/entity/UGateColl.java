package com.massivecraft.creativegates.entity;

import com.massivecraft.creativegates.CreativeGates;
import com.massivecraft.creativegates.ExitComparator;
import com.massivecraft.creativegates.NetworkIdEqualsPredicate;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.Coll;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class UGateColl extends Coll<UGate>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public UGateColl(String name)
	{
		super(name);
	}
	
	// -------------------------------------------- //
	// STACK TRACEABILITY
	// -------------------------------------------- //
	
	@Override
	public void onTick()
	{
		super.onTick();
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String fixId(Object oid)
	{
		if (oid instanceof Block)
		{
			oid = PS.valueOf((Block)oid);
		}
		
		if (oid instanceof Location)
		{
			oid = PS.valueOf((Location)oid);
		}
		
		if (oid instanceof PS)
		{
			UGate gate = CreativeGates.get().getIndex().get((PS)oid);
			if (gate == null) return null;
			return gate.getId();
		}
		
		return super.fixId(oid);
	}
	
	// -------------------------------------------- //
	// EXTRAS
	// -------------------------------------------- //
	
	public List<UGate> getGateChain(String networkId)
	{
		List<UGate> ret = new ArrayList<>();
		ret.addAll(this.getAll(new NetworkIdEqualsPredicate(networkId), ExitComparator.get()));
		return ret;
	}

}
