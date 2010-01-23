package net.slashware.spriteGears.entities;

import java.util.ArrayList;
import java.util.List;

import net.slashware.spriteGears.game.ShipEventListener;
import net.slashware.util.Position;

public class AssaultGroup implements ShipEventListener{
	//private List<StarShip> starShips = new ArrayList<StarShip>();
	private Position position;
	private Faction faction;
	private List<StarShip> enabledStarShips = new ArrayList<StarShip>();

	public void setFaction(Faction faction) {
		this.faction = faction;
	}

	/*public List<StarShip> getStarShips() {
		return starShips;
	}*/

	public void addStarShip(StarShip starShip){
		starShip.addShipEventListener(this);
		starShip.setFaction(getFaction());
		//starShips.add(starShip);
		enabledStarShips.add(starShip);
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Faction getFaction() {
		return faction;
	}

	
	public List<StarShip> getEnabledStarShips() {
		return enabledStarShips;
	}
	
	public void shipDisabled(StarShip ship) {
		removeStarShip(ship);
	}

	public void removeStarShip(StarShip starShip) {
		//starShip.removeShipEventListener(this);
		//starShips.remove(starShip);
		enabledStarShips.remove(starShip);
	}
}
