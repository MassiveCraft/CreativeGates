package com.massivecraft.creativegates.index;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.massivecraft.creativegates.entity.UGate;
import com.massivecraft.massivecore.ps.PS;

public class IndexCombined extends IndexAbstract
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected final Map<String, IndexWorld> worldToIndex = new ConcurrentHashMap<String, IndexWorld>(8, 0.9f, 1);
	public Map<String, IndexWorld> getWorldToIndex() { return this.worldToIndex; }
	
	// -------------------------------------------- //
	// INDEX FETCHING
	// -------------------------------------------- //
	
	public IndexWorld getIndex(String world)
	{
		if (world == null) throw new IllegalArgumentException("world was null");
		IndexWorld ret = this.worldToIndex.get(world);
		if (ret == null)
		{
			ret = new IndexWorld();
			this.worldToIndex.put(world, ret);
		}
		return ret;
	}
	
	public IndexWorld getIndex(PS ps)
	{
		if (ps == null) throw new IllegalArgumentException("ps was null");
		return this.getIndex(ps.getWorld());
	}
	
	public IndexWorld getIndex(UGate ugate)
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
		for (IndexWorld indexWorld : this.worldToIndex.values())
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
