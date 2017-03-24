package com.massivecraft.creativegates.index;

import com.massivecraft.creativegates.entity.UGate;
import com.massivecraft.massivecore.ps.PS;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IndexWorld extends IndexAbstract
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	final protected Map<PS, UGate> coordToGate = new ConcurrentHashMap<PS, UGate>(8, 0.9f, 1);
	public Map<PS, UGate> getCoordToGate() { return this.coordToGate; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void add(UGate ugate)
	{
		for (PS coord : ugate.getCoords())
		{
			this.coordToGate.put(coord, ugate);
		}
	}

	@Override
	public void remove(UGate ugate)
	{
		for (PS coord : ugate.getCoords())
		{
			this.coordToGate.remove(coord);
		}
	}

	@Override
	public void clear()
	{
		this.coordToGate.clear();
	}

	@Override
	public UGate get(PS ps)
	{
		return this.coordToGate.get(ps.getBlockCoords(true));
	}
	
	public UGate getRaw(PS ps)
	{
		return this.coordToGate.get(ps);
	}

}
