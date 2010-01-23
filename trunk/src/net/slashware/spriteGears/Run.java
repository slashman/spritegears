package net.slashware.spriteGears;

import java.util.ArrayList;
import java.util.List;

import net.slashware.spriteGears.entities.AssaultGroup;
import net.slashware.spriteGears.entities.SpaceStation;
import net.slashware.spriteGears.entities.StarExpedition;
import net.slashware.spriteGears.entities.StarShip;

import net.slashware.spriteGears.entities.StarMap;
import net.slashware.spriteGears.factory.ShipFactory;
import net.slashware.spriteGears.game.Game;
import net.slashware.spriteGears.generators.BranchingStarMapGenerator;
import net.slashware.spriteGears.generators.MockExpeditionGenerator;
import net.slashware.spriteGears.generators.MockStarMapGenerator;
import net.slashware.spriteGears.generators.MockZoneMapGenerator;
import net.slashware.spriteGears.generators.SimpleZoneMapGenerator;
import net.slashware.spriteGears.generators.TerranZoneMapGenerator;
import net.slashware.spriteGears.ui.BuyShips;
import net.slashware.spriteGears.ui.Display;
import net.slashware.spriteGears.ui.StarMapNavi;
import net.slashware.spriteGears.ui.TitleScreen;
import net.slashware.swing.SwingSystemInterface;
import net.slashware.util.sound.SoundManager;

public class Run {
	private static SwingSystemInterface si;
	public static void main(String[] args){
		si = new SwingSystemInterface();
		Display.initialize(si);
		SoundManager.initialize();
		TitleScreen.showTitleScreen();
	}
	
	public static void game(){
		Game game = new Game();
		Game.setCurrentGame(game);
		
		StarExpedition starExpedition = new StarExpedition();
		starExpedition.setCommander("Slashie");
		starExpedition.setMarved(2000);
		game.setStarExpedition(starExpedition);
		StarMap starMap = new BranchingStarMapGenerator().generateStarmap();
		game.setStarMap(starMap);
		starExpedition.setCurrentZone(starMap.getInitialZone());
		new TerranZoneMapGenerator().generateZoneMap(starMap.getInitialZone());
		game.setSpaceStation(starMap.getInitialZone().getSpaceStation());
		BuyShips.showBuyShips();
		//StarMapNavi.showStarMapNavi();
	}
	
	public static String getVersion(){
		return "0.2";
	}

}
