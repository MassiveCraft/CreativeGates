package com.massivecraft.creativegates.entity;


import org.bukkit.block.Block;

import com.massivecraft.creativegates.ConfServer;
import com.massivecraft.creativegates.CreativeGates;
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
		super(name, UGate.class, MStore.getDb(ConfServer.dburi), CreativeGates.get());
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
		
		if (oid instanceof PS)
		{
			UGate gate = CreativeGates.get().getIndex().get((PS)oid);
			if (gate == null) return null;
			return gate.getId();
		}
		
		return super.fixId(oid);
	}

}
