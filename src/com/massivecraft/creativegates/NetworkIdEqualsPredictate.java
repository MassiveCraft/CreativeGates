package com.massivecraft.creativegates;

import com.massivecraft.creativegates.entity.UGate;
import com.massivecraft.mcore.Predictate;

public class NetworkIdEqualsPredictate implements Predictate<UGate>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final String networkId;
	public String getNetworkId() { return this.networkId; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public NetworkIdEqualsPredictate(String networkId)
	{
		this.networkId = networkId;
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean apply(UGate ugate)
	{
		return ugate.getNetworkId().equalsIgnoreCase(this.networkId);
	}
	
}
