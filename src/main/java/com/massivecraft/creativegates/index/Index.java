package com.massivecraft.creativegates.index;

import com.massivecraft.creativegates.entity.UGate;
import com.massivecraft.mcore.ps.PS;

public interface Index
{
	public void add(UGate ugate);
	public void remove(UGate ugate);
	public void clear();
	public UGate get(PS ps);
}