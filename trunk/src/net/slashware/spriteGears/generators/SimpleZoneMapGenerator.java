package net.slashware.spriteGears.generators;

import java.util.ArrayList;
import java.util.List;

import net.slashware.spriteGears.entities.SpaceStation;
import net.slashware.spriteGears.entities.StarSection;
import net.slashware.spriteGears.entities.StarZone;
import net.slashware.spriteGears.factory.ShipFactory;
import net.slashware.spriteGears.game.ZoneRules;
import net.slashware.util.Line;
import net.slashware.util.Position;
import net.slashware.util.Util;

/**
 * Decide the main features of the sector
 * Place the features spacedly
 * Connect the features
 * Add fill in sectors
 * @author Slash
 *
 */
public class SimpleZoneMapGenerator {

	private Position randomPosition(){
		return new Position(Util.rand(0, StarZone.ZONE_WIDTH-1), Util.rand(0, StarZone.ZONE_HEIGHT-1));
	}

	public void generateZoneMap(StarZone zone) {
		int difficulty = zone.getDifficulty();
		boolean hasFriendlySpaceStation = hasFriendlySpaceStation(zone);
		int bigEnergy = 1; //Util.rand(1,difficulty+1);
		int tries = 0;
		Position entryPoint = null;
		Position spaceStationPosition = null;
		List<Position> bigEnergiesPositions = new ArrayList<Position>();
		while (true){
			entryPoint = randomPosition();
			if (hasFriendlySpaceStation){
				spaceStationPosition = randomPosition();
			}
			bigEnergiesPositions.clear();
			for (int i = 0; i < bigEnergy; i++){
				bigEnergiesPositions.add(randomPosition());
			}
			if (correctlySpaced(entryPoint, spaceStationPosition, bigEnergiesPositions))
				break;
			if (tries > 1000)
				break;
			tries++;
		}
		zone.setSection(entryPoint.x, entryPoint.y, StarSection.MOTHERSHIP);
		if (hasFriendlySpaceStation){
			addSpaceStationAt(zone, spaceStationPosition);
		}
		for (Position bigEnergyPosition: bigEnergiesPositions){
			zone.setSection(bigEnergyPosition.x, bigEnergyPosition.y, StarSection.BIG_ENERGY);
		}
		/* Connect the features.
		 *  Connect Mothership to each big energy and spacestation with a line of void or enemies or energies
		 */
		for (Position bigEnergyPosition: bigEnergiesPositions){
			Line l = new Line(entryPoint, bigEnergyPosition);
			Position p = l.next();
			p = l.next();
			while (!p.equals(bigEnergyPosition)){
				int number = Util.rand(1,100);
				/*
				 * 15% Energy
				 * 15% Enemy
				 * 70% Void
				 */
				if (number > 85){
					// Energy
					zone.setSection(p.x, p.y, StarSection.ENERGY);
				} else if (number > 70-difficulty*10){
					// Enemy
					zone.setSection(p.x, p.y, StarSection.ENEMY);
				} else {
					// Void
					zone.setSection(p.x, p.y, StarSection.VOID);
				}
				p = l.next();
			}
		}
		if (hasFriendlySpaceStation){
			Line l = new Line(entryPoint, spaceStationPosition);
			Position p = l.next();
			p = l.next();
			while (!p.equals(spaceStationPosition)){
				int number = Util.rand(1,100);
				/*
				 * 15% Energy
				 * 15% Enemy
				 * 70% Void
				 */
				if (number > 85){
					// Energy
					zone.setSection(p.x, p.y, StarSection.ENERGY);
				} else if (number > 70){
					// Enemy
					zone.setSection(p.x, p.y, StarSection.ENEMY);
				} else {
					// Void
					zone.setSection(p.x, p.y, StarSection.VOID);
				}
				p = l.next();
			}
		}
		
		// Random voids
		int randomVoids = Util.rand(5, 15-difficulty);
		int failSafe = 0;
		for (int i = 0; i < randomVoids; i++){
			failSafe++;
			Position p = randomPosition();
			
			if (zone.getSection(p.x, p.y) == null && hasSurroundingVoid(zone, p)){
				zone.setSection(p.x, p.y, StarSection.VOID);
			} else {
				if (failSafe > 10000)
					break;
				i--;
			}
		}
		zone.setEntryPosition(entryPoint);
		zone.setMapGenerated(true);
	}
	
	public void addSpaceStationAt(StarZone zone, Position spaceStationPosition) {
		zone.setSpaceStationPosition(spaceStationPosition);
		zone.setSpaceStation(createSpaceStation(zone,spaceStationPosition));
		zone.setSection(spaceStationPosition.x, spaceStationPosition.y, StarSection.TERRAN_SPACE_STATION);
	}

	private SpaceStation createSpaceStation(StarZone zone, Position spaceStationPosition) {
		SpaceStation ret = new SpaceStation();
		int ships = 10+(zone.getDifficulty()+1)*5;
		for (int i = 0; i < ships; i++){
			ret.addShip(ShipFactory.createShipModelByDifficulty(zone.getDifficulty()+1));
		}
		ret.setPosition(spaceStationPosition);
		return ret;
	}

	public boolean hasFriendlySpaceStation(StarZone zone) {
		return Util.chance(80-zone.getDifficulty()*5);
	}

	public final static int MIN_SPACE = 5;
	private boolean correctlySpaced(Position entryPoint,
			Position spaceStationPosition, List<Position> bigEnergiesPositions) {
		for (Position position: bigEnergiesPositions){
			if (spaceStationPosition != null && spaceStationPosition.equals(position))
				return false;
			if (Position.distance(entryPoint, position) < MIN_SPACE)
				return false;
		}
		if (spaceStationPosition != null){
			for (Position position: bigEnergiesPositions){
				if (position.equals(spaceStationPosition))
					return false;
			}
			if (Position.distance(entryPoint, spaceStationPosition) < MIN_SPACE)
				return false;
		}
		return true;
	}
	
	private boolean hasSurroundingVoid(StarZone zone, Position p) {
		return isVoid(zone, p.x-1, p.y) ||
			   isVoid(zone, p.x, p.y-1) ||
			   isVoid(zone, p.x, p.y+1) ||
			   isVoid(zone, p.x+1, p.y);
	}
	private boolean isVoid(StarZone zone, int x, int y) {
		return ZoneRules.isValid(x,y) && zone.getSection(x,y) == StarSection.VOID;
	}
	
	

}
