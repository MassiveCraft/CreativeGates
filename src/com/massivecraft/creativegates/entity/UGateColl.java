package com.massivecraft.creativegates.entity;


import com.massivecraft.creativegates.ConfServer;
import com.massivecraft.creativegates.CreativeGates;
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

}
