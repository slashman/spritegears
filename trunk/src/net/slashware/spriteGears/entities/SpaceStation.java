package net.slashware.spriteGears.entities;

import java.util.ArrayList;
import java.util.List;

import net.slashware.util.Position;

public class SpaceStation {
	private List<StarShip> inventory = new ArrayList<StarShip>();
	private Position position;

	public void setPosition(Position position) {
		this.position = position;
	}

	public List<StarShip> getInventory() {
		return inventory;
	}

	public Position getPosition() {
		return position;
	}

	public void removeStarShip(StarShip ship) {
		inventory.remove(ship);
		
	}

	public void addShip(StarShip ship) {
		inventory.add(ship);
	}
}