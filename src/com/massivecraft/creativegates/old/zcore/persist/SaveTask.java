package com.massivecraft.creativegates.old.zcore.persist;

import com.massivecraft.creativegates.old.zcore.MPlugin;

public class SaveTask implements Runnable
{
	MPlugin p;
	public SaveTask(MPlugin p)
	{
		this.p = p;
	}
	
	public void run()
	{
		if ( ! p.getAutoSave()) return;
		p.preAutoSave();
		EM.saveAllToDisc();
		p.postAutoSave();
	}
}
