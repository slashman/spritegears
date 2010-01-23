package net.slashware.spriteGears.entities;

import java.util.ArrayList;
import java.util.List;

public class StarExpedition {
	private String commander;
	private List<StarShip> starShips = new ArrayList<StarShip>();
	private StarZone currentZone;
	private int marved;
	private int spriteGears;

	public int getSpriteGears() {
		return spriteGears;
	}

	public void setSpriteGears(int spriteGears) {
		this.spriteGears = spriteGears;
	}

	public List<StarShip> getStarShips() {
		return starShips;
	}

	public void addStarShip(StarShip ship) {
		this.starShips.add(ship);
	}

	public String getCommander() {
		return commander;
	}

	public void setCommander(String commander) {
		this.commander = commander;
	}

	public StarZone getCurrentZone() {
		return currentZone;
	}

	public void setCurrentZone(StarZone currentZone) {
		this.currentZone = currentZone;
	}

	public int getMarved() {
		return marved;
	}
	
	public void setMarved(int marved){
		this.marved = marved;
	}
	
	public void reduceMarved(int marved){
		this.marved -= marved;
	}

	public void partAssaultGroup(AssaultGroup activeAssaultGroup) {
		for (StarShip ship: activeAssaultGroup.getEnabledStarShips()){
			starShips.remove(ship);
		}
	}

	public void storeRemaining(List<StarShip> remainingShips) {
		for (StarShip ship: remainingShips){
			if (!starShips.contains(ship)){
				starShips.add(ship);
			}
		}
	}

	public void increaseSpriteGears() {
		spriteGears++;
	
	}

	public void addMvd(int prize) {
		// TODO Auto-generated method stub
		this.marved += marved;

	}
}
