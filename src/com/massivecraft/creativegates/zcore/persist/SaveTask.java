package com.massivecraft.creativegates.zcore.persist;

public class SaveTask implements Runnable
{
	public void run()
	{
		EM.saveAllToDisc();
	}
}
