package net.slashware.spriteGears.entities;

import java.util.ArrayList;
import java.util.List;

import net.slashware.spriteGears.ai.BasicComputerAI;
import net.slashware.spriteGears.game.ShipEventListener;
import net.slashware.spriteGears.ui.BattleScreen;

public class BattleScenario implements ShipEventListener {
	private BasicComputerAI computerAI;
	private List<AssaultGroup> assaultGroups = new ArrayList<AssaultGroup>();
	private List<StarShip> activeSpriteGear = new ArrayList<StarShip>();
	private String music;
	private int prize;
	
	public int getPrize() {
		return prize;
	}

	public void setPrize(int prize) {
		this.prize = prize;
	}

	public String getMusic() {
		return music;
	}

	public void addGroup (AssaultGroup assaultGroup){
		assaultGroups.add(assaultGroup);
		for (StarShip ship: assaultGroup.getEnabledStarShips()){
			ship.addShipEventListener(this);
			if (ship.isSpriteGear() && !ship.isDisabled())
				activeSpriteGear.add(ship);
				
		}
	}

	public List<AssaultGroup> getGroups() {
		return assaultGroups;
	}

	public List<StarShip> getEnabledEnemyShipsFor(Faction faction) {
		for (AssaultGroup group: assaultGroups){
			if (group.getFaction() != faction){
				return group.getEnabledStarShips();
			}
		}
		return null;
	}

	public List<StarShip> getEnabledFriendShipsFor(Faction faction) {
		for (AssaultGroup group: assaultGroups){
			if (group.getFaction() == faction){
				return group.getEnabledStarShips();
			}
		}
		return null;
	}

	public void shipDisabled(StarShip ship) {
		BattleScreen.updateShips();
	}

	public StarShip getEnabledShipAt(int x, int y) {
		for (AssaultGroup group: assaultGroups){
			for (StarShip ship: group.getEnabledStarShips()){
				if (ship.getPosition().x == x && ship.getPosition().y == y)
					return ship;
			}
		}
		return null;
	}

	
	public BasicComputerAI getComputerAI() {
		return computerAI;
	}

	public void setComputerAI(BasicComputerAI computerAI) {
		this.computerAI = computerAI;
	}


	public List<StarShip> getEnabledEnemySpriteGears() {
		return activeSpriteGear;
	}

	public void changeGroup(StarShip ship, Faction faction) {
		AssaultGroup target = null;
		AssaultGroup source = null;
		//
		// TODO Auto-generated method stub
		for (AssaultGroup group: assaultGroups){
			if (group.getEnabledStarShips().contains(ship)){
				group.removeStarShip(ship);
				break;
			}
		}
		
		for (AssaultGroup group: assaultGroups){
			if (group.getFaction() == faction){
				group.addStarShip(ship);
				break;
			}
		}
	}

	public void setMusic(String string) {
		this.music = string;
	}

}
