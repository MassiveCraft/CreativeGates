package org.mcteam.creativegates;

import java.util.*;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.mcteam.creativegates.util.BlockUtil;
import org.mcteam.creativegates.util.TextUtil;

public class Gate {
	public Set<WorldCoord> contentCoords = new HashSet<WorldCoord>();
	public Set<WorldCoord> frameCoords = new HashSet<WorldCoord>();
	public WorldCoord sourceCoord;
	public Set<Integer> frameMaterialIds = new TreeSet<Integer>();
	public boolean frameDirIsNS; // True means NS direction. false means WE direction.
	
	private static final Set<BlockFace> expandFacesWE = new HashSet<BlockFace>();
	private static final Set<BlockFace> expandFacesNS = new HashSet<BlockFace>();
	static {
		expandFacesWE.add(BlockFace.UP);
		expandFacesWE.add(BlockFace.DOWN);
		expandFacesWE.add(BlockFace.WEST);
		expandFacesWE.add(BlockFace.EAST);
		
		expandFacesNS.add(BlockFace.UP);
		expandFacesNS.add(BlockFace.DOWN);
		expandFacesNS.add(BlockFace.NORTH);
		expandFacesNS.add(BlockFace.SOUTH);
	}
	
	// To create a gate instance is the same as trying to open a gate ingame.
	public Gate(WorldCoord sourceCoord) throws Exception {
		this.sourceCoord = sourceCoord;
		Block sourceBlock = sourceCoord.getBlock();
		
		if (sourceBlock.getType() != Conf.block) {
			throw new Exception("The source block must be made of "+TextUtil.getMaterialName(Conf.block)+"."); 
		}
		
		if (P.p.getGateFromFrameBlock(sourceBlock) != null) {
			throw new Exception("The gate is already opened."); 
		}
		
		// Search for content WE and NS
		Block floodStartBlock = sourceBlock.getRelative(BlockFace.UP);
		Set<Block> contentBlocksWE = getFloodBlocks(floodStartBlock, new HashSet<Block>(), expandFacesWE);
		Set<Block> contentBlocksNS = getFloodBlocks(floodStartBlock, new HashSet<Block>(), expandFacesNS);
		
		// Figure out dir and content... or throw no frame fail. 
		Set<Block> contentBlocks;
		
		if (contentBlocksWE == null && contentBlocksNS == null) {
			throw new Exception("There is no frame, or it is broken, or it is to large.");
		}
		
		if (contentBlocksNS == null) {
			contentBlocks = contentBlocksWE;
			frameDirIsNS = false;
		} else if (contentBlocksWE == null) {
			contentBlocks = contentBlocksNS;
			frameDirIsNS = true;
		} else if (contentBlocksWE.size() > contentBlocksNS.size()) {
			contentBlocks = contentBlocksNS;
			frameDirIsNS = true;
		} else {
			contentBlocks = contentBlocksWE;
			frameDirIsNS = false;
		}
		
		// Find the frame blocks and materials
		Set<Block> frameBlocks = new HashSet<Block>();
		Set<BlockFace> expandFaces = frameDirIsNS ? expandFacesNS : expandFacesWE;
		for (Block currentBlock : contentBlocks) {
			for (BlockFace face : expandFaces) {
				Block potentialBlock = currentBlock.getFace(face);
				if ( ! contentBlocks.contains(potentialBlock)) {
					frameBlocks.add(potentialBlock);
					if (potentialBlock != sourceBlock) {
						frameMaterialIds.add(potentialBlock.getTypeId());
					}
				}
			}
		}
		
		// Now we add the frame and content blocks as world coords to the lookup maps.
		for (Block frameBlock : frameBlocks) {
			this.frameCoords.add(new WorldCoord(frameBlock));
		}
		for (Block contentBlock : contentBlocks) {
			this.contentCoords.add(new WorldCoord(contentBlock));
		}
		
		// We add the gate to the list
		P.p.gates.add(this);
		
		// Finally we set the content blocks material to water
		this.fill();
		
		// Save
		P.p.save();
	}
	
	//----------------------------------------------//
	// Find Target Gate And Location
	//----------------------------------------------//
	
	/*
	 * This method finds the place where this gates goes to.
	 * We pick the next gate in the network chain that has a non blocked exit.
	 */
	public Location getMyTargetExitLocation() {
		Location ret;
		for (Gate gate : this.getSelfRelativeGatePath()) {
			ret = gate.getMyOwnExitLocation();
			if (ret != null) {
				return ret;
			}
		}
		return null;
	}
	
	/*
	 * Find all the gates on the network of this gate (including this gate itself).
	 * The gates on the same network are those with the same frame materials.
	 */
	public ArrayList<Gate> getNetworkGatePath() {
		ArrayList<Gate> networkGatePath = new ArrayList<Gate>();
		
		// As P.p.gates is a TreeSet it is always sorted the same way (after gate source location).
		for (Gate gate : P.p.gates) {
			if (this.frameMaterialIds.equals(gate.frameMaterialIds)) {
				networkGatePath.add(gate);
			}
		}
		
		return networkGatePath;
	}
	
	/*
	 * Return the gates on the network in the order they come after this gate.
	 * This gate itself is not included (as opposed to getNetworkGatePath where it is).
	 * This gate itself would be in the beginning / end of this path.
	 */
	public ArrayList<Gate> getSelfRelativeGatePath() {
		ArrayList<Gate> selfRelativeGatePath = new ArrayList<Gate>();
		
		ArrayList<Gate> networkGatePath = this.getNetworkGatePath();
		int myIndex = networkGatePath.indexOf(this);
		
		// Add what is after me
		selfRelativeGatePath.addAll(networkGatePath.subList(myIndex+1, networkGatePath.size()));
		
		// Add what is before me
		selfRelativeGatePath.addAll(networkGatePath.subList(0, myIndex));
		
		return selfRelativeGatePath;
	}
	
	/*
	 * If someone arrives to this gate, where should we place them?
	 * This method returns a Location telling us just that.
	 * It might also return null if the gate exit is blocked.
	 */
	public Location getMyOwnExitLocation() {
		Block overSourceBlock = sourceCoord.getBlock().getFace(BlockFace.UP);
		Location firstChoice;
		Location secondChoice;
		
		if (frameDirIsNS) {
			firstChoice = overSourceBlock.getFace(BlockFace.EAST).getLocation();
			firstChoice.setYaw(180);
			
			secondChoice = overSourceBlock.getFace(BlockFace.WEST).getLocation();
			secondChoice.setYaw(0);
		} else {
			firstChoice = overSourceBlock.getFace(BlockFace.NORTH).getLocation();
			firstChoice.setYaw(90);
			
			secondChoice = overSourceBlock.getFace(BlockFace.SOUTH).getLocation();
			secondChoice.setYaw(270);
		}
		
		// We want to stand in the middle of the block. Not in the corner.
		firstChoice.add(0.5, 0, 0.5);
		secondChoice.add(0.5, 0, 0.5);
		
		firstChoice.setPitch(0);
		secondChoice.setPitch(0);
		
		if (BlockUtil.canPlayerStandInBlock(firstChoice.getBlock())) {
			return firstChoice;
		} else if (BlockUtil.canPlayerStandInBlock(secondChoice.getBlock())) {
			return secondChoice;
		}
		
		return null;
	}
	
	//----------------------------------------------//
	// Gate information
	//----------------------------------------------//
	
	public String getInfoMsgMaterial() {
		String ret = Conf.colorDefault + "Materials: ";
		
		ArrayList<String> materialNames = new ArrayList<String>();
		for (Integer frameMaterialId : this.frameMaterialIds) {
			materialNames.add(Conf.colorHighlight + TextUtil.getMaterialName(Material.getMaterial(frameMaterialId)));
		}
		
		ret += TextUtil.implode(materialNames, Conf.colorDefault + ", ");
		
		return ret;
	}
	
	public String getInfoMsgNetwork() {
		return Conf.colorDefault + "Gates: " + Conf.colorHighlight + this.getNetworkGatePath().size();
	}
	
	public void informPlayer(Player player) {
		player.sendMessage("");
		player.sendMessage(this.getInfoMsgMaterial());
		player.sendMessage(this.getInfoMsgNetwork());
	}
	
	//----------------------------------------------//
	// Content management
	//----------------------------------------------//
	
	public void fill() {
		for (WorldCoord coord : this.contentCoords) {
			coord.getBlock().setType(Material.STATIONARY_WATER);
		}
	}
	
	public void empty() {
		for (WorldCoord coord : this.contentCoords) {
			coord.getBlock().setType(Material.AIR);
		}
	}

	//----------------------------------------------//
	// Flood
	//----------------------------------------------//
	
	public static Set<Block> getFloodBlocks(Block startBlock, Set<Block> foundBlocks, Set<BlockFace> expandFaces) {
		if (foundBlocks == null) {
			return null;
		}
		
		if  (foundBlocks.size() > 200) {
			return null;
		}
		
		if (foundBlocks.contains(startBlock)) {
			return foundBlocks;
		}
		
		if (startBlock.getType() == Material.AIR || startBlock.getType() == Material.WATER || startBlock.getType() == Material.STATIONARY_WATER) {
			// ... We found a block :D ...
			foundBlocks.add(startBlock);
			
			// ... And flood away !
			for (BlockFace face : expandFaces) {
				Block potentialBlock = startBlock.getFace(face);
				foundBlocks = getFloodBlocks(potentialBlock, foundBlocks, expandFaces);
			}
		}
		
		return foundBlocks;
	}
}
