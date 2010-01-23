package net.slashware.spriteGears.game;

import net.slashware.spriteGears.entities.AssaultGroup;
import net.slashware.spriteGears.entities.BattleScenario;
import net.slashware.spriteGears.entities.SpaceStation;
import net.slashware.spriteGears.entities.StarExpedition;
import net.slashware.spriteGears.entities.StarMap;

public class Game {
	private StarMap starMap;
	private StarExpedition starExpedition;
	private AssaultGroup activeAssaultGroup;
	private BattleScenario battleScenario;
	private SpaceStation spaceStation;
	
	public void setSpaceStation(SpaceStation spaceStation) {
		this.spaceStation = spaceStation;
	}
	public BattleScenario getBattleScenario() {
		return battleScenario;
	}
	private static Game currentGame;
	
	public StarMap getStarMap() {
		return starMap;
	}
	public void setStarMap(StarMap starMap) {
		this.starMap = starMap;
	}
	public StarExpedition getStarExpedition() {
		return starExpedition;
	}
	public void setStarExpedition(StarExpedition starExpedition) {
		this.starExpedition = starExpedition;
	}
	
	public static void setCurrentGame(Game game) {
		currentGame = game;
	}
	
	public static Game getCurrentGame(){
		return currentGame;
	}
	public AssaultGroup getActiveAssaultGroup() {
		return activeAssaultGroup;
	}
	public void setActiveAssaultGroup(AssaultGroup activeAssaultGroup) {
		this.activeAssaultGroup = activeAssaultGroup;
	}
	public void setBattleScenario(BattleScenario scenario) {
		this.battleScenario = scenario;
	}
	public static void delay(int i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public SpaceStation getSpaceStation() {
		return spaceStation;
	}
}
