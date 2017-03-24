package com.massivecraft.creativegates.index;

import com.massivecraft.creativegates.entity.UGate;
import com.massivecraft.massivecore.ps.PS;

public interface Index
{
	void add(UGate ugate);
	void remove(UGate ugate);
	void clear();
	UGate get(PS ps);
}