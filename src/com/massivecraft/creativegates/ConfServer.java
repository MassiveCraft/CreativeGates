package com.massivecraft.creativegates;

import java.util.List;

import com.massivecraft.mcore.SimpleConfig;
import com.massivecraft.mcore.util.MUtil;

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
	public static List<String> aliasesCg = MUtil.list("cg", "creativegates", "creativegate");
	
}
