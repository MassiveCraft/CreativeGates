package com.massivecraft.creativegates.entity;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;

import com.massivecraft.creativegates.CreativeGates;
import com.massivecraft.creativegates.ExitComparator;
import com.massivecraft.creativegates.NetworkIdEqualsPredictate;
import com.massivecraft.mcore.ps.PS;
import com.massivecraft.mcore.store.Coll;
import com.massivecraft.mcore.store.MStore;

public class UGateColl extends Coll<UGate>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public UGateColl(String name)
	{
		super(name, UGate.class, MStore.getDb(), CreativeGates.get());
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
		List<UGate> ret = new ArrayList<UGate>();
		ret.addAll(this.getAll(new NetworkIdEqualsPredictate(networkId), ExitComparator.get()));
		return ret;
	}

}
