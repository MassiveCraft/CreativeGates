package com.massivecraft.creativegates.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.massivecraft.creativegates.CreativeGates;
import com.massivecraft.mcore.ps.PS;
import com.massivecraft.mcore.store.Entity;
import com.massivecraft.mcore.util.SenderUtil;

public class UGate extends Entity<UGate>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	public static UGate get(Object oid)
	{
		return UGateColls.get().get2(oid);
	}
	
	// -------------------------------------------- //
	// OVERRIDE: ENTITY
	// -------------------------------------------- //
	
	@Override
	public UGate load(UGate that)
	{
		this.setCreatorId(that.getCreatorId());
		this.setCreatedMillis(that.getCreatedMillis());
		this.setUsedMillis(that.getUsedMillis());
		this.setNetworkId(that.getNetworkId());
		this.setNetworkSecret(that.isNetworkSecret());
		this.setEnterEnabled(that.isEnterEnabled());
		this.setExitEnabled(that.isExitEnabled());
		this.setExit(that.getExit());
		this.setCoords(that.getCoords());
		
		return this;
	}
	
	@Override
	public void postAttach(String id)
	{
		CreativeGates.get().getIndex().add(this);
	}
	
	@Override
	public void postDetach(String id)
	{
		CreativeGates.get().getIndex().remove(this);
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private String creatorId = null;
	public String getCreatorId() { return this.creatorId; }
	public void setCreatorId(String creatorId) { this.creatorId = creatorId; this.changed(); }
	
	private long createdMillis = 0;
	public long getCreatedMillis() { return this.createdMillis; }
	public void setCreatedMillis(long createdMillis) { this.createdMillis = createdMillis; this.changed(); }
	
	private long usedMillis = 0;
	public long getUsedMillis() { return this.usedMillis; }
	public void setUsedMillis(long usedMillis) { this.usedMillis = usedMillis; this.changed(); }
	
	private String networkId = null;
	public String getNetworkId() { return this.networkId; }
	public void setNetworkId(String networkId) { this.networkId = networkId; this.changed(); }
	
	private boolean networkSecret = false;
	public boolean isNetworkSecret() { return this.networkSecret; }
	public void setNetworkSecret(boolean networkSecret) { this.networkSecret = networkSecret; this.changed(); }
	
	private boolean enterEnabled = true;
	public boolean isEnterEnabled() { return this.enterEnabled; }
	public void setEnterEnabled(boolean enterEnabled) { this.enterEnabled = enterEnabled; this.changed(); }
	
	private boolean exitEnabled = true;
	public boolean isExitEnabled() { return this.exitEnabled; }
	public void setExitEnabled(boolean exitEnabled) { this.exitEnabled = exitEnabled; this.changed(); }
	
	private PS exit = null;
	public PS getExit() { return this.exit; }
	public void setExit(PS exit) { this.exit = exit; this.changed(); }
	
	private Set<PS> coords = new HashSet<PS>();
	public Set<PS> getCoords() { return Collections.unmodifiableSet(this.coords);}
	public void setCoords(Collection<PS> coords)
	{
		if (this.attached()) CreativeGates.get().getIndex().remove(this);
		
		this.coords = new HashSet<PS>(coords);
		
		if (this.attached()) CreativeGates.get().getIndex().add(this);
			
		this.changed();
	}
	
	// -------------------------------------------- //
	// FIELDS: EXTRA
	// -------------------------------------------- //
	
	public boolean isCreator(Object o)
	{
		String senderId = SenderUtil.getSenderId(o);
		if (senderId == null) return false;
		return senderId.equalsIgnoreCase(this.creatorId);
	}
	
}
