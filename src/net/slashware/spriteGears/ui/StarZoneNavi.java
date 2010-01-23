package net.slashware.spriteGears.ui;

import java.awt.Color;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


import net.slashware.spriteGears.entities.AssaultGroup;
import net.slashware.spriteGears.entities.StarExpedition;
import net.slashware.spriteGears.entities.StarSection;
import net.slashware.spriteGears.entities.StarZone;
import net.slashware.spriteGears.game.Game;
import net.slashware.spriteGears.game.ZoneRules;
import net.slashware.util.ImageUtils;
import net.slashware.util.sound.SoundManager;

public class StarZoneNavi extends Display{
	private static BufferedImage IMG_SPACEBG;
	
	static {
		try {
			IMG_SPACEBG = ImageUtils.createImage("img/spacebg2x.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static JLabel groupBox;
	public static JLabel infoBox;
	static {
		infoBox = new JLabel();
		infoBox.setFont(FNT_TEXT);
		infoBox.setForeground(Color.GREEN);
		infoBox.setBounds(520,360,300,40);
	}
	
	
	public static void showStarZoneNavi() {
		// Prepare..
		StarExpedition expedition = Game.getCurrentGame().getStarExpedition();
		StarZone currentZone = expedition.getCurrentZone();
		AssaultGroup group = Game.getCurrentGame().getActiveAssaultGroup();
		
		// Render
		SoundManager.playFile("music/Craig Stern - Funky, Funky Spy.mp3");
		si.removeAllComponents();
		si.cls();
		si.setFont(FNT_TITLE);
		si.drawImage(0, 0, IMG_SPACEBG);
		si.printAtPixelCentered(256, 36, currentZone.getName()+" Sector", Color.WHITE);
		
		for (int x = 0; x < currentZone.getZoneMap().length; x++){
			for (int y = 0; y < currentZone.getZoneMap()[0].length; y++){
				StarSection section = currentZone.getZoneMap()[x][y];
				if (section == null)
					continue;
				//si.drawImage(80+x*32, 60+y*32, section.getImage());
				JLabel box = new JLabel(new ImageIcon(section.getImage()));
				box.addMouseListener(new SectionMouseAdapter(section,x,y));
				box.setBounds(80+x*32,60+y*32, 32, 32);
				si.add(box);
			}
		}
		//si.drawImage(80+group.getPosition().x * 32 - 10, 60+group.getPosition().y*32-4,group.getStarShips().get(0).getImage());
		groupBox = new JLabel(new ImageIcon(group.getEnabledStarShips().get(0).getImage()));
		relocateAssaultGroup();
		si.add(groupBox);
		si.setZOrder(groupBox,0);
		si.add(infoBox);
		si.refresh();
	}
	
	public static void relocateAssaultGroup(){
		AssaultGroup group = Game.getCurrentGame().getActiveAssaultGroup();
		StarZoneNavi.groupBox.setBounds(80+group.getPosition().x * 32 - 10, 60+group.getPosition().y*32-4, 52, 36);
	}
}

class SectionMouseAdapter extends MouseAdapter {
	//private StarSection section;
	private ImageIcon hoverIcon;
	private ImageIcon normalIcon;
	private StarSection section;
	private int x,y;
	SectionMouseAdapter (StarSection section, int x, int y){
		//this.section = section;
		hoverIcon = new ImageIcon(section.getHoverImage());
		normalIcon = new ImageIcon(section.getImage());
		this.section = section;
		this.x = x; 
		this.y = y;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (isValidDestination()){
			ZoneRules.landOn(x,y);
			StarZoneNavi.relocateAssaultGroup();
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		if (isValidDestination()){
			((JLabel)e.getComponent()).setIcon(hoverIcon);
		}
		StarZoneNavi.infoBox.setText(section.getDescription());
	}
	
	private boolean isValidDestination(){
		AssaultGroup group = Game.getCurrentGame().getActiveAssaultGroup();
		return Math.abs(group.getPosition().x - x) < 2 && 
			Math.abs(group.getPosition().y - y) < 2;
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		((JLabel)e.getComponent()).setIcon(normalIcon);
	}
}
