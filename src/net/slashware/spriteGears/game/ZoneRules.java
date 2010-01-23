package net.slashware.spriteGears.game;

import net.slashware.spriteGears.entities.AssaultGroup;
import net.slashware.spriteGears.entities.BattleScenario;
import net.slashware.spriteGears.entities.StarExpedition;
import net.slashware.spriteGears.entities.StarSection;
import net.slashware.spriteGears.entities.StarZone;
import net.slashware.spriteGears.generators.BattleScenarioGenerator;
import net.slashware.spriteGears.ui.BattleScreen;
import net.slashware.spriteGears.ui.BuyShips;
import net.slashware.spriteGears.ui.Display;
import net.slashware.spriteGears.ui.StarMapNavi;
import net.slashware.util.Position;

public class ZoneRules {
	public static void landOn(int x, int y){
		StarExpedition expedition = Game.getCurrentGame().getStarExpedition();
		StarZone currentZone = expedition.getCurrentZone();
		AssaultGroup group = Game.getCurrentGame().getActiveAssaultGroup();
		StarSection landingSection = currentZone.getZoneMap()[x][y];
		switch (landingSection){
		case VOID:
			group.setPosition(new Position(x,y));
			break;
		case MOTHERSHIP:
			if (Display.showConfirm("Do you want to abandon this zone?")){
				StarMapNavi.showStarMapNavi();
			} else {
				group.setPosition(new Position(x,y));
			}
			break;
		case BIG_ENERGY:
			//TODO: Implement big prize for big energy?
			BattleScenario scenario = BattleScenarioGenerator.generateBattleScenario(group, landingSection, currentZone.getDifficulty());
			Game.getCurrentGame().setBattleScenario(scenario);
			BattleScreen.showBattleScreen();
			group.setPosition(new Position(x,y));
			break;
		case ENERGY:
			
			scenario = BattleScenarioGenerator.generateBattleScenario(group, landingSection, currentZone.getDifficulty());
			Game.getCurrentGame().setBattleScenario(scenario);
			BattleScreen.showBattleScreen();
			group.setPosition(new Position(x,y));
			
			break;
		case ENEMY:
			scenario = BattleScenarioGenerator.generateBattleScenario(group, landingSection, currentZone.getDifficulty());
			Game.getCurrentGame().setBattleScenario(scenario);
			BattleScreen.showBattleScreen();
			group.setPosition(new Position(x,y));
			break;
		case TERRAN_SPACE_STATION:
			Game.getCurrentGame().setSpaceStation(currentZone.getSpaceStation());
			BuyShips.showBuyShips();
		}  
	}

	public static boolean isValid(int x, int y) {
		return x >= 0 && x < StarZone.ZONE_WIDTH && y >= 0 && y < StarZone.ZONE_HEIGHT;
	}
}
