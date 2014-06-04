package com.massivecraft.creativegates.index;

import java.util.HashMap;
import java.util.Map;

import com.massivecraft.creativegates.entity.UGate;
import com.massivecraft.massivecore.ps.PS;

public class IndexWorld extends IndexAbstract
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private Map<PS, UGate> coord2ugate = new HashMap<PS, UGate>();
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void add(UGate ugate)
	{
		for (PS coord : ugate.getCoords())
		{
			this.coord2ugate.put(coord, ugate);
		}
	}

	@Override
	public void remove(UGate ugate)
	{
		for (PS coord : ugate.getCoords())
		{
			this.coord2ugate.remove(coord);
		}
	}

	@Override
	public void clear()
	{
		this.coord2ugate.clear();
	}

	@Override
	public UGate get(PS ps)
	{
		return this.coord2ugate.get(ps.getBlockCoords(true));
	}

}
