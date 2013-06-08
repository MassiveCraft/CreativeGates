package com.massivecraft.creativegates.entity;

import com.massivecraft.creativegates.Const;
import com.massivecraft.creativegates.CreativeGates;
import com.massivecraft.mcore.Aspect;

public class UGateColls extends XColls<UGateColl, UGate>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static UGateColls i = new UGateColls();
	public static UGateColls get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE: COLLS
	// -------------------------------------------- //
	
	@Override
	public UGateColl createColl(String collName)
	{
		return new UGateColl(collName);
	}

	@Override
	public Aspect getAspect()
	{
		return CreativeGates.get().getAspect();
	}
	
	@Override
	public String getBasename()
	{
		return Const.COLLECTION_BASENAME_UGATE;
	}
	
}
