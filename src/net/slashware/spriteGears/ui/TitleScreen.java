package net.slashware.spriteGears.ui;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.TileObserver;
import java.io.IOException;

import net.slashware.spriteGears.Run;
import net.slashware.util.CharKey;
import net.slashware.util.PropertyFilters;
import net.slashware.util.sound.JLayerMP3Player;
import net.slashware.util.sound.SoundManager;

public class TitleScreen extends Display {
	private static String IMG_TITLE;  
	private static BufferedImage IMG_PICKER;
	static MouseListener listener;
	static {
		try {
			IMG_TITLE = "img/guy2.png";
			IMG_PICKER = PropertyFilters.getImage("img/spriteGears2x.gif", "0,0,52,36");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void showTitleScreen(){		
		SoundManager.playFile("music/Wyvern - upbeat.mp3");
		//((GFXUserInterface)UserInterface.getUI()).messageBox.setVisible(false);
		si.cls();
		si.setFont(FNT_TITLE_BIG);
		si.printAtPixel(120, 130, "SpriteGears", COLOR_BOLD);
		si.setFont(FNT_TITLE);
		si.printAtPixel(120, 150, "Episode I: The Quest for the SpriteGears", Color.WHITE);
		si.drawImage(543,190, IMG_TITLE);
		//si.drawImage(215,60,IMG_TITLE_NAME);
		si.setFont(FNT_TEXT);
		si.printAtPixel(280, 400, "[Click to start]", Color.GREEN);
		si.printAtPixel(60, 555, "Developed by Santiago Zapata 2009-2010", Color.WHITE);
		si.printAtPixel(60, 580, "TigSource Assemblee Compo", Color.BLUE);
		si.printAtPixel(60, 530, "Version "+Run.getVersion(), Color.WHITE);
		listener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				si.removeMouseListener(listener);
				
				Run.game();
			}
		};
		si.addMouseListener(listener);
		si.refresh();
		
		
		
	}

}
