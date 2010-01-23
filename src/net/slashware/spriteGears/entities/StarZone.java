package net.slashware.spriteGears.entities;

import java.util.ArrayList;
import java.util.List;

import net.slashware.util.Position;

public class StarZone {
	public final static int ZONE_WIDTH = 11, ZONE_HEIGHT = 7;
	private String name;
	private List<StarPath> paths = new ArrayList<StarPath>();
	private StarSection[][] zoneMap = new StarSection[ZONE_WIDTH][ZONE_HEIGHT];
	private Position entryPosition;
	private int difficulty;
	private boolean mapGenerated;
	private Position spaceStationPosition;
	private SpaceStation spaceStation;

	public SpaceStation getSpaceStation() {
		return spaceStation;
	}

	public void setSpaceStation(SpaceStation spaceStation) {
		this.spaceStation = spaceStation;
	}

	public Position getSpaceStationPosition() {
		return spaceStationPosition;
	}

	public boolean isMapGenerated() {
		return mapGenerated;
	}

	public void setMapGenerated(boolean mapGenerated) {
		this.mapGenerated = mapGenerated;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addPath(StarZone to, int cost){
		paths.add(new StarPath(to, cost));
		if (!to.hasPathTo(this)){
			to.addPath(this, cost);
		}
	}

	private boolean hasPathTo(StarZone starZone) {
		for (StarPath path: paths){
			if (path.getTo().equals(starZone)){
				return true;
			}
		}
		return false;
	}

	public List<StarPath> getPaths() {
		return paths;
	}
	
	public StarSection[][] getZoneMap(){
		return zoneMap;
	}
	
	public void setSection(int x, int y, StarSection section){
		zoneMap[x][y] = section;
	}

	public Position getEntryPosition() {
		return entryPosition;
	}

	public void setEntryPosition(Position entryPosition) {
		this.entryPosition = entryPosition;
	}

	public StarSection getSection(int x, int y) {
		return zoneMap[x][y];
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setSpaceStationPosition(Position spaceStationPosition) {
		this.spaceStationPosition = spaceStationPosition;
	}
	
	
	
}
