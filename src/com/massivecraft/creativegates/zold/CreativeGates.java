package com.massivecraft.creativegates.zold;

import com.massivecraft.creativegates.zold.zcore.*;

public class CreativeGates extends MPlugin
{
	// Our single plugin instance
	public static CreativeGates p;
	
	// Listeners
	public TheListener theListener;
	
	public CreativeGates()
	{
		p = this;
	}

	public void onEnable()
	{
		if ( ! preEnable()) return;
		
		// TODO fix config auto update routine... ?
		Conf.load();
		
		Gates.i.loadFromDisc();
		Gates.i.openAllOrDetach();
		
		// Register events
		this.theListener = new TheListener(this);
		
		postEnable();
	}
	
	public void onDisable()
	{
		Gates.i.emptyAll();
		super.onDisable();
	}
}
