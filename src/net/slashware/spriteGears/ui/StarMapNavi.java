package net.slashware.spriteGears.ui;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.swing.JLabel;

import net.slashware.spriteGears.entities.StarExpedition;
import net.slashware.spriteGears.entities.StarPath;
import net.slashware.spriteGears.entities.StarZone;
import net.slashware.spriteGears.game.Game;
import net.slashware.spriteGears.generators.AssaultGroupGenerator;
import net.slashware.spriteGears.generators.SimpleZoneMapGenerator;
import net.slashware.util.ImageUtils;
import net.slashware.util.sound.SoundManager;

public class StarMapNavi extends Display{
	private static BufferedImage IMG_SPACEBG;
	private static BufferedImage IMG_BATTLESHIP;
	
	static {
		try {
			IMG_SPACEBG = ImageUtils.createImage("img/spacebg2x.png");
			IMG_BATTLESHIP = ImageUtils.createImage("img/battleShip.gif");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void showStarMapNavi() {
		// Render
		SoundManager.playFile("music/Jorge Boscan - Entry 6.mp3");
		updateStarMapNavi();
	}
	
	public static void updateStarMapNavi(){
		// Prepare..
		StarExpedition expedition = Game.getCurrentGame().getStarExpedition();
		StarZone currentZone = expedition.getCurrentZone();
		List<StarPath> starPaths = currentZone.getPaths();
		
		si.removeAllComponents();
		si.cls();
		si.setFont(FNT_TITLE);
		si.drawImage(0, 0, IMG_SPACEBG);
		si.printAtPixelCentered(256, 36, currentZone.getName()+" Sector", Color.WHITE);
		si.setFont(FNT_TEXT);
		si.printAtPixelCentered(256, 70, "Hyperjump to...", Color.GRAY);
		
		JLabel label = new JLabel("Explore Sector");
		label.setForeground(Color.WHITE);
		label.setFont(FNT_TEXT);
		int stringWidth = si.getStringWidth("Explore Sector");
		label.setLocation(40, 390);
		label.setSize(stringWidth+20, 20);
		label.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				SetupAssaultGroup.showSetupAssaultGroup();

			}
			public void mouseEntered(MouseEvent e) {
				e.getComponent().setForeground(Color.RED);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				e.getComponent().setForeground(Color.WHITE);
			}
		});
		si.add(label);
		
		/*label = new JLabel("Inspect Ship");
		label.setForeground(Color.WHITE);
		label.setFont(FNT_TEXT);
		stringWidth = si.getStringWidth("Inspect Ship");
		label.setLocation(330, 390);
		label.setSize(stringWidth+20, 20);
		label.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				e.getComponent().setForeground(Color.RED);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				e.getComponent().setForeground(Color.WHITE);
			}
		});
		si.add(label);
		*/
		
		int i = 0;
		for (StarPath starPath: starPaths){
			String string = starPath.getTo().getName()+", "+starPath.getCost()+" Light Years";
			label = new JLabel(string);
			label.setForeground(Color.WHITE);
			label.setFont(FNT_TEXT);
			label.addMouseListener(new StarMapMouseListener(starPath));
			stringWidth = si.getStringWidth(string);
			label.setLocation(256 - stringWidth/2, 100+i*40);
			label.setSize(stringWidth+20, 20);
			si.add(label);
			//si.printAtPixelCentered(256,150+i*40, , Color.WHITE);
			i++;
		}
		
		si.drawImage(208, 384, IMG_BATTLESHIP);
		si.refresh();
	}
	
}


class StarMapMouseListener implements MouseListener {
	private StarPath starPath;

	StarMapMouseListener (StarPath starPath){
		this.starPath = starPath;
	}
	
	public void mouseClicked(MouseEvent e) {
		Game.getCurrentGame().getStarExpedition().setCurrentZone(starPath.getTo());
		StarMapNavi.updateStarMapNavi();
	}
	
	public void mouseEntered(MouseEvent e) {
		e.getComponent().setForeground(Color.RED);
	}
	
	public void mouseExited(MouseEvent e) {
		e.getComponent().setForeground(Color.WHITE);
	}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	
}


