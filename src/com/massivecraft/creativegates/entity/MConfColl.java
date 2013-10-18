package com.massivecraft.creativegates.entity;

import com.massivecraft.creativegates.ConfServer;
import com.massivecraft.creativegates.Const;
import com.massivecraft.creativegates.CreativeGates;
import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.store.Coll;
import com.massivecraft.mcore.store.MStore;

public class MConfColl extends Coll<MConf>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MConfColl i = new MConfColl();
	public static MConfColl get() { return i; }
	private MConfColl()
	{
		super(Const.COLLECTION_BASENAME_MCONF, MConf.class, MStore.getDb(ConfServer.dburi), CreativeGates.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void init()
	{
		super.init();
		MConf.i = this.get(MCore.INSTANCE, true);
	}
	
}
