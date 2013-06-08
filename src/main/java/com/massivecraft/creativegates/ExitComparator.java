package com.massivecraft.creativegates;

import java.util.Comparator;

import com.massivecraft.creativegates.entity.UGate;

public class ExitComparator implements Comparator<UGate>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ExitComparator i = new ExitComparator();
	public static ExitComparator get() { return i; }
	public ExitComparator() {}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public int compare(UGate o1, UGate o2)
	{
		return o1.getExit().compareTo(o2.getExit());
	}

}
