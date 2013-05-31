package com.massivecraft.creativegates;

import com.massivecraft.creativegates.entity.UConfColls;
import com.massivecraft.creativegates.entity.UGateColls;
import com.massivecraft.creativegates.index.IndexCombined;
import com.massivecraft.creativegates.listener.MainListener;
import com.massivecraft.mcore.Aspect;
import com.massivecraft.mcore.AspectColl;
import com.massivecraft.mcore.MPlugin;
import com.massivecraft.mcore.Multiverse;

public class CreativeGates extends MPlugin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static CreativeGates i;
	public static CreativeGates get() { return i; }
	public CreativeGates() { CreativeGates.i = this; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// Aspects
	private Aspect aspect;
	public Aspect getAspect() { return this.aspect; }
	public Multiverse getMultiverse() { return this.getAspect().getMultiverse(); }
	
	// Index
	private IndexCombined index = new IndexCombined();
	public IndexCombined getIndex() { return this.index; };
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void onEnable()
	{
		if ( ! preEnable()) return;
		
		// Load Server Config
		ConfServer.get().load();
		
		// Initialize Aspects
		this.aspect = AspectColl.get().get(Const.ASPECT_ID, true);
		this.aspect.register();
		this.aspect.setDesc(
			"<i>What gates do exist.",
			"<i>What are the config options are set to."
		);

		// Initialize Database
		this.getIndex().clear();
		UConfColls.get().init();
		UGateColls.get().init();
		
		// Setup Listeners
		MainListener.get().activate();
		
		postEnable();
	}
	
	@Override
	public void onDisable()
	{
		this.getIndex().clear();
		super.onDisable();
	}
	
}
