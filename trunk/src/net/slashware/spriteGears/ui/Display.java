package net.slashware.spriteGears.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.slashware.spriteGears.Run;
import net.slashware.spriteGears.game.BattleRules;
import net.slashware.swing.SwingSystemInterface;
import net.slashware.util.CharKey;
import net.slashware.util.PropertyFilters;
import net.slashware.util.sound.SoundManager;


public class Display {
	public static SwingSystemInterface si;
	protected static Font FNT_TEXT;
	protected static Font FNT_TITLE;
	protected static Font FNT_TITLE_BIG;
	protected static Color COLOR_BOLD;
	

	public static void initialize(SwingSystemInterface si){
		try {
			Display.si = si;
			si.setTitle("SpriteGears "+Run.getVersion()+" - Santiago Zapata 2009-2010");
			si.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			FNT_TEXT = PropertyFilters.getFont("adoqun.ttf", "28");
			FNT_TITLE = PropertyFilters.getFont("adoqun.ttf", "36");
			FNT_TITLE_BIG = PropertyFilters.getFont("adoqun.ttf", "64");
			COLOR_BOLD = Color.red;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public static boolean showConfirm(String string) {
		//return si.showMessagePrompt(string);
		//TODO: Change the UI
		return true;
	}



	public static void showGameOver() {
		SoundManager.playFile("music/Craig Stern - Lullaby.mp3");
		si.cls();
		si.printAtPixel(40, 120, "Game Over... Press space");
		si.refresh();
		si.waitKey(CharKey.SPACE);
		TitleScreen.showTitleScreen();
	}


	static int currentKeyCode;
	public static void threadWaitKey(int keyCode) {
		currentKeyCode = keyCode;
		new Thread(
				new Runnable(){
					public void run() {
						si.waitKey(currentKeyCode);
					};
				}
		).start();
		
		
	}



	public static void showWin() {
		SoundManager.playFile("music/Craig Stern - We Are In Serious Trouble.mp3");
		si.cls();
		si.setFont(FNT_TEXT);
		si.drawImage(470,100, "img/guy1.png");
		si.printAtPixel(20, 40, "Boarding the "+BattleRules.WIN_SPRITEGEARS+" Ancient Sprite Gear,");
		si.printAtPixel(20, 60, "you are now ready to fight the creature of chaos");
		si.printAtPixel(20, 80, "whom treaten to engulf the galaxy in terror absolute.");
		si.printAtPixel(20, 100, "Who knows what awaits you in episode # 2!", Color.GREEN);
		
		si.printAtPixel(20, 130, "Congratulations! Press Space to Exit.", Color.red);
		si.refresh();
		new Thread(
				new Runnable(){
					public void run() {
						si.waitKey(CharKey.SPACE);
						System.exit(0);
					};
				}
		).start();
		
		
		
	}
	
	
}
