package net.slashware.spriteGears;

import net.slashware.spriteGears.entities.StarExpedition;
import net.slashware.spriteGears.entities.StarMap;
import net.slashware.spriteGears.game.Game;
import net.slashware.spriteGears.generators.BranchingStarMapGenerator;
import net.slashware.spriteGears.generators.TerranZoneMapGenerator;
import net.slashware.spriteGears.ui.BuyShips;
import net.slashware.spriteGears.ui.Display;
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
