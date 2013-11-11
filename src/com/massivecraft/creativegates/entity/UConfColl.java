package com.massivecraft.creativegates.entity;

import com.massivecraft.creativegates.CreativeGates;
import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.store.Coll;
import com.massivecraft.mcore.store.MStore;

public class UConfColl extends Coll<UConf>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public UConfColl(String name)
	{
		super(name, UConf.class, MStore.getDb(), CreativeGates.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void init()
	{
		super.init();
		this.get(MCore.INSTANCE, true);
	}
	
}
