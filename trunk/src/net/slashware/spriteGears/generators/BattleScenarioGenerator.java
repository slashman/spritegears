package net.slashware.spriteGears.generators;

import java.util.ArrayList;
import java.util.List;

import net.slashware.spriteGears.ai.BasicComputerAI;
import net.slashware.spriteGears.entities.AssaultGroup;
import net.slashware.spriteGears.entities.BattleScenario;
import net.slashware.spriteGears.entities.Faction;
import net.slashware.spriteGears.entities.FighterShip;
import net.slashware.spriteGears.entities.StarSection;
import net.slashware.spriteGears.entities.StarShip;
import net.slashware.spriteGears.factory.ShipFactory;
import net.slashware.util.Direction;
import net.slashware.util.Position;
import net.slashware.util.Util;

public class BattleScenarioGenerator {
	public static BattleScenario generateBattleScenario(AssaultGroup group, StarSection section, int zoneDifficulty){
		BattleScenario ret = new BattleScenario();
		ret.addGroup(group);
		// Random scenario
		int row = Util.rand(10, 15);
		int startx = 7-group.getEnabledStarShips().size();
		int ii = 0;
		for (StarShip ship: group.getEnabledStarShips()){
			ship.setPosition(new Position(startx+ii*2, row));
			ship.setAlignment(Direction.UP);
			ii++;
		}
		
		AssaultGroup enemies = new AssaultGroup();
		enemies.setFaction(Faction.SHADOW);
		boolean simple = section == StarSection.ENEMY || section == StarSection.ENERGY || (section == StarSection.BIG_ENERGY && zoneDifficulty < 2);   
		if (simple){
			int enemiesQ = Util.rand(2, 2+zoneDifficulty*2);
			List<Position> positions = new ArrayList<Position>();
			for (int i = 0; i < enemiesQ; i++){
				String model = selectShip(zoneDifficulty);
				FighterShip ship = ShipFactory.createShip(model);
				ship.setFaction(Faction.SHADOW);
				enemies.addStarShip(ship);
				Position random = null;
				while (true){
					random = new Position(Util.rand(5,10), Util.rand(3,8));
					if (!positions.contains(random)){
						positions.add(random);
						break;
					}
				}
				ship.setPosition(random);
				ship.setAlignment(Direction.DOWN);
				ship.addShipEventListener(enemies);
			}
			ret.setMusic("music/Craig Stern - Exploring the Depths.mp3");
			ret.setPrize(enemiesQ*50);
			if (section == StarSection.ENERGY)
				ret.setPrize(ret.getPrize()+200);
			else if (section == StarSection.BIG_ENERGY)
				ret.setPrize(ret.getPrize()+400);
		} else if (section == StarSection.BIG_ENERGY){
			String spriteGear = selectSpriteGear(zoneDifficulty);
			FighterShip ship = ShipFactory.createShip(spriteGear);
			ship.setFaction(Faction.SHADOW);
			enemies.addStarShip(ship);
			ship.setPosition(new Position(Util.rand(5,10), Util.rand(3,4)));
			ship.setAlignment(Direction.DOWN);
			ship.addShipEventListener(enemies);
			ret.setMusic("music/Craig Stern - The Sorcerer's Tower.mp3");
		} else if (section == StarSection.SHADOW_ENERGY){
			//TODO: Shadow creature battle
		}
		ret.addGroup(enemies);
		ret.setComputerAI(new BasicComputerAI(enemies, Util.rand(2, 4)));
		return ret;
	}
	
	
	
	private static String selectSpriteGear(int zoneDifficulty) {
		return ShipFactory.selectRandomUnusedSpriteGearModel();
	}

	private static String selectShip(int zoneDifficulty) {
		return ShipFactory.selectShipModelByDifficulty(zoneDifficulty);
	}

	public static BattleScenario generateRandomBattleScenario(AssaultGroup group, StarSection section, int zoneDifficulty){
		BattleScenario ret = new BattleScenario();
		ret.addGroup(group);
		// Random scenario
		for (StarShip ship: group.getEnabledStarShips()){
			ship.setPosition(new Position(Util.rand(0,15), Util.rand(10,15)));
			ship.setAlignment(Direction.UP);

		}
		
		//TODO: Implement this
		/* Some possible scenarios
		 *  Normal encounter, ships in formation
		 *  Scrambled ships
		 *  "Pincer", surrounded
		 *  Surprise Attack from behind
		 */
		AssaultGroup enemies = new AssaultGroup();
		enemies.setFaction(Faction.SHADOW);
		for (int i = 0; i < 0; i++){
			String model = "";
			
				model = "GECF12-Hydra";
			FighterShip ship = ShipFactory.createShip(model);
			ship.setFaction(Faction.SHADOW);
			enemies.addStarShip(ship);
			ship.setPosition(new Position(Util.rand(5,10), Util.rand(3,8)));
			ship.setAlignment(Direction.DOWN);
			ship.addShipEventListener(enemies);
		}
		
		FighterShip ship = ShipFactory.createShip("Valar");
		ship.setFaction(Faction.SHADOW);
		enemies.addStarShip(ship);
		ship.setPosition(new Position(Util.rand(5,10), Util.rand(3,8)));
		ship.setAlignment(Direction.DOWN);
		ship.addShipEventListener(enemies);
		ret.addGroup(enemies);
		
		ret.setComputerAI(new BasicComputerAI(enemies, Util.rand(2, 4)));
		
		return ret;
	}
}
