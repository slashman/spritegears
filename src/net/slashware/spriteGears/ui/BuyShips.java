package net.slashware.spriteGears.ui;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import net.slashware.spriteGears.entities.AssaultGroup;
import net.slashware.spriteGears.entities.Faction;
import net.slashware.spriteGears.entities.SpaceStation;
import net.slashware.spriteGears.entities.StarExpedition;
import net.slashware.spriteGears.entities.StarShip;
import net.slashware.spriteGears.game.BattleRules;
import net.slashware.spriteGears.game.Game;
import net.slashware.util.ImageUtils;
import net.slashware.util.sound.SoundManager;

public class BuyShips extends Display{
	private static BufferedImage IMG_SPACEBG;
	
	static {
		try {
			IMG_SPACEBG = ImageUtils.createImage("img/spacebg2x.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static JScrollPane availableScrollPane = new JScrollPane();
	static JScrollPane currentScrollPane = new JScrollPane();
	public static List<NoBuyShipLabel> availableShipsLabels;
	public static List<NoBuyShipLabel> currentShipsLabels;
	static JLabel currentCredits = new JLabel();
	public static JLabel descriptionLabel;
	public static JLabel energyLabel;
	public static JLabel powerLabel;
	public static JLabel recoverLabel;
	public static JLabel speedLabel;
	
	static {
		//TODO: Fix this, scrollbars not showing!
		availableScrollPane.setBounds(18,87, 229, 357);
		//availableScrollPane.setLayout(null);
		availableScrollPane.setOpaque(false);
		availableScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		availableScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		currentScrollPane.setBounds(256,87, 229, 357);
		currentScrollPane.setLayout(null);
		currentScrollPane.setOpaque(false);
		currentScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		currentScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		currentCredits.setFont(BuyShips.FNT_TEXT);
		currentCredits.setForeground(Color.GREEN);
		currentCredits.setBounds(275, 440, 350, 40);
		
		descriptionLabel = new JLabel();
		descriptionLabel.setBounds(520,100, 300, 20);
		descriptionLabel.setFont(FNT_TEXT);
		descriptionLabel.setForeground(Color.RED);
		
		energyLabel = new JLabel();
		energyLabel.setFont(FNT_TEXT);
		energyLabel.setForeground(Color.WHITE);
		energyLabel.setBounds(520,120, 300, 20);
		powerLabel = new JLabel();
		powerLabel.setFont(FNT_TEXT);
		powerLabel.setForeground(Color.WHITE);
		powerLabel.setBounds(520,140, 300, 20);
		recoverLabel = new JLabel();
		recoverLabel.setFont(FNT_TEXT);
		recoverLabel.setForeground(Color.WHITE);
		recoverLabel.setBounds(520,160, 300, 20);
		speedLabel = new JLabel();
		speedLabel.setFont(FNT_TEXT);
		speedLabel.setForeground(Color.WHITE);
		speedLabel.setBounds(520,180, 300, 20);
	}
	
	public static void showBuyShips(){
		StarExpedition expedition = Game.getCurrentGame().getStarExpedition();
		SoundManager.playFile("music/Jorge Boscan - Entry 1 Loop.mp3");
		si.removeAllComponents();
		si.cls();
		si.setFont(FNT_TITLE);
		si.drawImage(0, 0, IMG_SPACEBG);
		si.printAtPixelCentered(256, 36, "Buy Ships", Color.WHITE);
		si.setFont(FNT_TEXT);
		currentCredits.setText("Credits: "+expedition.getMarved()+" MVD");
		si.printAtPixelCentered(120, 66, "Inventory", Color.WHITE);
		si.printAtPixelCentered(340, 66, "Assault Group", Color.WHITE);
		si.add(availableScrollPane);
		si.add(currentScrollPane);
		si.add(currentCredits);
		
		si.add(descriptionLabel);
		si.add(energyLabel);
		si.add(powerLabel);
		si.add(recoverLabel);
		si.add(speedLabel);
		
		SpaceStation station = Game.getCurrentGame().getSpaceStation();
		List<StarShip> availableShips = station.getInventory();
		AssaultGroup currentAssaultGroup = Game.getCurrentGame().getActiveAssaultGroup();
		List<StarShip> currentAssaultGroupShips = null;
		if (currentAssaultGroup != null){
			currentAssaultGroupShips = currentAssaultGroup.getEnabledStarShips();
		}
		availableShipsLabels = new ArrayList<NoBuyShipLabel>();
		for (StarShip ship: availableShips){
			BuyShipLabel label = new BuyShipLabel(ship);
			availableShipsLabels.add(label);
			
		}
		currentShipsLabels = new ArrayList<NoBuyShipLabel>();
		if (currentAssaultGroup != null){
			for (StarShip ship: currentAssaultGroupShips){
				NoBuyShipLabel label = new NoBuyShipLabel(ship);
				currentShipsLabels.add(label);
			}
		}
		
		JLabel ok = new JLabel("Ok");
		ok.setBounds(105, 460, 125, 40);
		ok.setFont(FNT_TEXT);
		ok.setForeground(Color.GREEN);
		ok.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (BuyShips.currentShipsLabels.size() == 0){
					return;
				}
				Game.getCurrentGame().getActiveAssaultGroup().setPosition(Game.getCurrentGame().getSpaceStation().getPosition());
				StarZoneNavi.showStarZoneNavi();
			}
		});
		si.add(ok);
		
		updateShipLocations();
		si.refresh();
	}
	
	public static void updateShipLocations() {
		// Remove all from the scrollpane
		availableScrollPane.removeAll();
		currentScrollPane.removeAll();
		
		int i = 0;
		for (JLabel label: availableShipsLabels){
			label.setLocation(10, 10+i*40);
			availableScrollPane.add(label);
			i++;
		}
		i = 0;
		for (JLabel label: currentShipsLabels){
			label.setLocation(10, 10+i*40);
			currentScrollPane.add(label);
			i++;
		}
		si.validate();
		si.refresh();
	}
}


class BuyShipSelectionListener extends MouseAdapter {
	
	@Override
	public void mouseEntered(MouseEvent e) {
		StarShip ship = ((NoBuyShipLabel)e.getComponent()).getShip();
		BuyShips.descriptionLabel.setText(ship.getDescription());
		BuyShips.energyLabel.setText("Energy: "+ship.getEnergyDescription());
		BuyShips.powerLabel.setText("Power: "+ship.getAttack());
		BuyShips.recoverLabel.setText("Recover: "+ship.getShieldRecovery());
		BuyShips.speedLabel.setText( "Speed: "+ship.getSpeed());
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
		if (BuyShips.currentShipsLabels.contains(e.getComponent())){
			
		} else if (BuyShips.availableShipsLabels.contains(e.getComponent())){
			StarShip ship = ((NoBuyShipLabel)e.getComponent()).getShip();
			if (BuyShips.currentShipsLabels.size() < BattleRules.getMaxAssaultGroupSize()){
				if (Game.getCurrentGame().getStarExpedition().getMarved() >= ship.getPrice()){
					if (Display.showConfirm("Buy "+ship.getDescription()+" for "+ship.getPrice()+"MVD?")){
						//Add to Assault Group
						BuyShips.availableShipsLabels.remove(e.getComponent());
						BuyShips.currentShipsLabels.add((NoBuyShipLabel)e.getComponent());
						StarExpedition expedition = Game.getCurrentGame().getStarExpedition(); 
						expedition.reduceMarved(ship.getPrice());
						//expedition.addStarShip(ship);
						AssaultGroup group = Game.getCurrentGame().getActiveAssaultGroup();
						if (group == null){
							group = new AssaultGroup();
							group.setFaction(Faction.HUMAN);
							group.setPosition(Game.getCurrentGame().getSpaceStation().getPosition());
							Game.getCurrentGame().setActiveAssaultGroup(group);
						}
						Game.getCurrentGame().getActiveAssaultGroup().addStarShip(ship);
						Game.getCurrentGame().getSpaceStation().removeStarShip(ship);
						BuyShips.currentCredits.setText("Credits: "+expedition.getMarved()+" MVD");
					}
				}
			}
		}
		BuyShips.updateShipLocations();
	}
}

class NoBuyShipLabel extends JLabel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private StarShip ship;
	public StarShip getShip() {
		return ship;
	}
	NoBuyShipLabel (StarShip ship){
		super (ship.getDescription(), new ImageIcon(ship.getImage()), JLabel.LEFT);
		setFont(BuyShips.FNT_TEXT);
		setForeground(Color.WHITE);
		addMouseListener(new BuyShipSelectionListener());
		setSize(240,32);
		this.ship = ship;
		
	}
}

class BuyShipLabel extends NoBuyShipLabel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	BuyShipLabel (StarShip ship){
		super (ship);
		setText(ship.getDescription()+"("+ship.getPrice()+")");
	}
}