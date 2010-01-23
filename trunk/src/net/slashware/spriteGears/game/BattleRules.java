package net.slashware.spriteGears.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.slashware.spriteGears.action.ActionSelector;
import net.slashware.spriteGears.entities.BattleScenario;
import net.slashware.spriteGears.entities.Faction;
import net.slashware.spriteGears.entities.StarShip;
import net.slashware.spriteGears.ui.BattleScreen;
import net.slashware.spriteGears.ui.Display;
import net.slashware.spriteGears.ui.StarMapNavi;
import net.slashware.spriteGears.ui.StarZoneNavi;
import net.slashware.util.CharKey;
import net.slashware.util.Direction;
import net.slashware.util.Position;
import net.slashware.util.Util;

public class BattleRules {
	public static final int BATTLE_ZONE_WIDTH = 16;
	public static final int BATTLE_ZONE_HEIGHT = 16;
	public static final int WIN_SPRITEGEARS = 5;
	

	public static void battleTurn (List<StarShip> ships){
		Game.getCurrentGame().getBattleScenario().getComputerAI().activate();
		Collections.sort(ships, new Comparator<StarShip>() {
			public int compare(StarShip o1, StarShip o2) {
				return o1.getSpeed() - o2.getSpeed();
			};
		});
		for (StarShip ship: ships){
			if (!ship.isDisabled())
				ship.getActionSelector().act();
			ship.recover();
		}
		BattleScreen.availableOrders = getCommandsPerTurn();
		boolean battleOver = checkBattleOver();
		if (!battleOver)
			BattleScreen.updateBattleScreen();
	}
	
	private static boolean checkBattleOver() {
		if (Game.getCurrentGame().getBattleScenario().getEnabledEnemyShipsFor(Faction.HUMAN).size() == 0){
			BattleScreen.addMessage("You won! Press Space to Continue");
			Game.getCurrentGame().getStarExpedition().addMvd(getPrize());
			BattleScreen.hideBattleScreen();
			//Display.si.refresh();
			//Display.si.waitKey(CharKey.SPACE);
			StarZoneNavi.showStarZoneNavi();
			return true;
		} else if (Game.getCurrentGame().getBattleScenario().getEnabledFriendShipsFor(Faction.HUMAN).size() == 0){
			new Thread(new Runnable(){
				@Override
				public void run() {
					BattleScreen.addMessage("You lost... Press Space to Continue");
					BattleScreen.hideBattleScreen();
					//Display.si.refresh();
					//Display.si.waitKey(CharKey.SPACE);
					if (Game.getCurrentGame().getStarExpedition().getStarShips().size()>0)
						StarMapNavi.showStarMapNavi();
					else 
						Display.showGameOver();
						//Display.showWin();
				}
			}).start();
			return true;
		}
		return false;
	}

	private static int getPrize() {
		BattleScenario scenario = Game.getCurrentGame().getBattleScenario();
		return scenario.getPrize();
	}

	public static int getCommandsPerTurn() {
		return 2;
	}

	public static boolean insideBounds(int x, int y){
		return !(x < 0 || x >= BattleRules.BATTLE_ZONE_WIDTH || 
			y < 0 || y >= BattleRules.BATTLE_ZONE_HEIGHT);
	}

	public static boolean canMove(StarShip ship, int x, int y) {
		return insideBounds(x,y) && ship.locateMovementMask()[x][y] && Game.getCurrentGame().getBattleScenario().getEnabledShipAt(x, y) == null;
	}

	
	public static void setPosition(StarShip ship, int x, int y) {
		if (BattleRules.canMove(ship, x, y)){
			ship.setPosition(new Position(x, y));
		}
	}

	public static void turnShip(StarShip currentShip, Direction direction) {
		if (direction != Direction.LEFT && direction != Direction.RIGHT)
			throw new IllegalArgumentException("Invalid direction "+direction);
		switch (direction){
		case LEFT:
			switch (currentShip.getAlignment()){
			case UP:
				currentShip.setAlignment(Direction.LEFT);
				break;
			case LEFT:
				currentShip.setAlignment(Direction.DOWN);
				break;
			case DOWN:
				currentShip.setAlignment(Direction.RIGHT);
				break;
			case RIGHT:
				currentShip.setAlignment(Direction.UP);
				break;
			}
			break;
		case RIGHT:
			switch (currentShip.getAlignment()){
			case UP:
				currentShip.setAlignment(Direction.RIGHT);
				break;
			case LEFT:
				currentShip.setAlignment(Direction.UP);
				break;
			case DOWN:
				currentShip.setAlignment(Direction.LEFT);
				break;
			case RIGHT:
				currentShip.setAlignment(Direction.DOWN);
				break;
			}
			break;
		}
		BattleScreen.updateBattleScreen();
	}

	public static void captureSpriteGear(StarShip spriteGear) {
		// Confirm the spritegear is surrounded
		List<StarShip> surroundingShips = getSurroundingEnemyShips(spriteGear); 
		if (surroundingShips.size() < getSurroundingShipsForCatchingSpriteGear()){
			BattleScreen.addMessage(spriteGear.getDescription()+" breaks free from the energy bound!");
			BattleScreen.availableOrders = 0;
			//return;
		}
		// Calculate the odds of catching the sprite gear
		int odds = getCatchingOdds(spriteGear, surroundingShips);
		
		// Try to catch the spritegear
		if (Util.chance(odds)){
			//Spritegear caught, add to the group and change factions
			BattleScreen.addMessage("You board the "+spriteGear.getDescription()+"!");
			Display.threadWaitKey(CharKey.SPACE);
			Game.getCurrentGame().getBattleScenario().changeGroup(spriteGear, Faction.HUMAN);
			BattleScreen.updateShips();
			BattleScreen.availableOrders = 0;
			Game.getCurrentGame().getStarExpedition().increaseSpriteGears();
			checkGameWin();
			checkBattleOver();
		} else {
			// If failed, lose turn
			BattleScreen.addMessage(spriteGear.getDescription()+" breaks free from the energy bound!");
			BattleScreen.availableOrders = 0;
		}
		
	}

	private static void checkGameWin() {
		
		if (Game.getCurrentGame().getStarExpedition().getSpriteGears() >= WIN_SPRITEGEARS){
			new Thread(new Runnable(){
				@Override
				public void run() {
					Display.showWin();
				}
			}).start();
			
		}
	}

	private static int getCatchingOdds(StarShip spriteGear,
			List<StarShip> surroundingShips) {
		return 50;
	}

	private static List<StarShip> getSurroundingEnemyShips(StarShip ship) {
		List<StarShip> ret = new ArrayList<StarShip>();
		List<StarShip> enemyShips = Game.getCurrentGame().getBattleScenario().getEnabledEnemyShipsFor(ship.getFaction());
		for (StarShip enemyShip: enemyShips){
			if (Math.abs(enemyShip.getPosition().x - ship.getPosition().x) <= 1 ||
				Math.abs(enemyShip.getPosition().y - ship.getPosition().y) <= 1)
				ret.add(enemyShip);
		}
		return ret;
	}

	private static int getSurroundingShipsForCatchingSpriteGear() {
		return 2;
	}

	public static void haltFire(StarShip ship){
		//TODO: Implement this, affects the Ship AI
	}


	public static int getMaxAssaultGroupSize() {
		return 5;
	}

}
