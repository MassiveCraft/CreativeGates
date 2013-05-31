package com.massivecraft.creativegates;

import com.massivecraft.mcore.SimpleConfig;

public class ConfServer extends SimpleConfig
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static transient ConfServer i = new ConfServer();
	public static ConfServer get() { return i; }
	public ConfServer() { super(CreativeGates.get()); }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public static String dburi = "default";
	
}
