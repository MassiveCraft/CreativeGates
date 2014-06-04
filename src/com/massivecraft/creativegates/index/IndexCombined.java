package com.massivecraft.creativegates.index;

import java.util.HashMap;
import java.util.Map;

import com.massivecraft.creativegates.entity.UGate;
import com.massivecraft.massivecore.ps.PS;

public class IndexCombined extends IndexAbstract
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private Map<String, IndexWorld> world2index = new HashMap<String, IndexWorld>();
	
	// -------------------------------------------- //
	// INDEX FETCHING
	// -------------------------------------------- //
	
	private IndexWorld getIndex(String world)
	{
		if (world == null) throw new IllegalArgumentException("world was null");
		IndexWorld ret = this.world2index.get(world);
		if (ret == null)
		{
			ret = new IndexWorld();
			this.world2index.put(world, ret);
		}
		return ret;
	}
	
	private IndexWorld getIndex(PS ps)
	{
		if (ps == null) throw new IllegalArgumentException("ps was null");
		return this.getIndex(ps.getWorld());
	}
	
	private IndexWorld getIndex(UGate ugate)
	{
		if (ugate == null) throw new IllegalArgumentException("ugate was null");
		return this.getIndex(ugate.getExit());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void add(UGate ugate)
	{
		if (ugate == null) throw new IllegalArgumentException("ugate was null");
		this.getIndex(ugate).add(ugate);
	}

	@Override
	public void remove(UGate ugate)
	{
		if (ugate == null) throw new IllegalArgumentException("ugate was null");
		this.getIndex(ugate).remove(ugate);
	}

	@Override
	public void clear()
	{
		for (IndexWorld indexWorld : this.world2index.values())
		{
			indexWorld.clear();
		}
	}

	@Override
	public UGate get(PS ps)
	{
		if (ps == null) throw new IllegalArgumentException("ps was null");
		return this.getIndex(ps).get(ps);
	}
	
}
