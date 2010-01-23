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
import net.slashware.spriteGears.entities.StarExpedition;
import net.slashware.spriteGears.entities.StarShip;
import net.slashware.spriteGears.entities.StarZone;
import net.slashware.spriteGears.game.BattleRules;
import net.slashware.spriteGears.game.Game;
import net.slashware.spriteGears.generators.SimpleZoneMapGenerator;
import net.slashware.util.ImageUtils;

public class SetupAssaultGroup extends Display{
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
	public static List<ShipLabel> availableShipsLabels;
	public static List<ShipLabel> currentShipsLabels;
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
	
	public static void showSetupAssaultGroup(){
		si.removeAllComponents();
		si.cls();
		si.setFont(FNT_TITLE);
		si.drawImage(0, 0, IMG_SPACEBG);
		si.printAtPixelCentered(256, 36, "Deploy Assault Group", Color.WHITE);
		si.setFont(FNT_TEXT);
		si.printAtPixelCentered(120, 66, "Available", Color.WHITE);
		si.printAtPixelCentered(340, 66, "Selected", Color.WHITE);
		si.add(availableScrollPane);
		si.add(currentScrollPane);
		
		si.add(descriptionLabel);
		si.add(energyLabel);
		si.add(powerLabel);
		si.add(recoverLabel);
		si.add(speedLabel);
		
		StarExpedition expedition = Game.getCurrentGame().getStarExpedition();
		List<StarShip> availableShips = expedition.getStarShips();
		AssaultGroup currentAssaultGroup = Game.getCurrentGame().getActiveAssaultGroup();
		List<StarShip> currentAssaultGroupShips = null;
		if (currentAssaultGroup != null){
			currentAssaultGroupShips = currentAssaultGroup.getEnabledStarShips();
		}
		availableShipsLabels = new ArrayList<ShipLabel>();
		for (StarShip ship: availableShips){
			ShipLabel label = new ShipLabel(ship);
			availableShipsLabels.add(label);
			
		}
		currentShipsLabels = new ArrayList<ShipLabel>();
		if (currentAssaultGroup != null){
			for (StarShip ship: currentAssaultGroupShips){
				ShipLabel label = new ShipLabel(ship);
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
				if (SetupAssaultGroup.currentShipsLabels.size() == 0){
					return;
				}
				Game.getCurrentGame().setActiveAssaultGroup(createAssaultGroup());
				Game.getCurrentGame().getStarExpedition().partAssaultGroup(Game.getCurrentGame().getActiveAssaultGroup());
				Game.getCurrentGame().getStarExpedition().storeRemaining(getRemainingShips());
				StarZone currentZone = Game.getCurrentGame().getStarExpedition().getCurrentZone();
				if (!currentZone.isMapGenerated()){
					new SimpleZoneMapGenerator().generateZoneMap(currentZone);
				}
				Game.getCurrentGame().getActiveAssaultGroup().setPosition(currentZone.getEntryPosition());
				StarZoneNavi.showStarZoneNavi();
				
			}

			
		});
		si.add(ok);
		
		JLabel cancel = new JLabel("Cancel");
		cancel.setBounds(300, 460, 125, 40);
		cancel.setFont(FNT_TEXT);
		cancel.setForeground(Color.GREEN);
		cancel.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				StarMapNavi.updateStarMapNavi();
			}
		});
		si.add(cancel);		
		
		updateShipLocations();
		si.refresh();
	}
	
	protected static AssaultGroup createAssaultGroup() {
		AssaultGroup ret = new AssaultGroup();
		ret.setFaction(Faction.HUMAN);
		for (ShipLabel label: SetupAssaultGroup.currentShipsLabels){
			ret.addStarShip(label.getShip());
		}
		return ret;
	}
	
	public static List<StarShip> getRemainingShips() {
		List<StarShip> ret = new ArrayList<StarShip>();
		for (ShipLabel label: SetupAssaultGroup.availableShipsLabels){
			ret.add(label.getShip());
		}
		return ret;
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

class ShipSelectionListener extends MouseAdapter {
	
	@Override
	public void mouseEntered(MouseEvent e) {
		StarShip ship = ((ShipLabel)e.getComponent()).getShip();
		SetupAssaultGroup.descriptionLabel.setText(ship.getDescription());
		SetupAssaultGroup.energyLabel.setText("Energy: "+ship.getEnergyDescription());
		SetupAssaultGroup.powerLabel.setText("Power: "+ship.getAttack());
		SetupAssaultGroup.recoverLabel.setText("Recover: "+ship.getShieldRecovery());
		SetupAssaultGroup.speedLabel.setText( "Speed: "+ship.getSpeed());
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
		if (SetupAssaultGroup.currentShipsLabels.contains(e.getComponent())){
			// Remove from Assault Group
			SetupAssaultGroup.currentShipsLabels.remove(e.getComponent());
			SetupAssaultGroup.availableShipsLabels.add((ShipLabel)e.getComponent());
		} else if (SetupAssaultGroup.availableShipsLabels.contains(e.getComponent())){
			// Add to Assault Group
			if (SetupAssaultGroup.currentShipsLabels.size() < BattleRules.getMaxAssaultGroupSize()){
				SetupAssaultGroup.availableShipsLabels.remove(e.getComponent());
				SetupAssaultGroup.currentShipsLabels.add((ShipLabel)e.getComponent());
			}
		}
		SetupAssaultGroup.updateShipLocations();
	}
}




class ShipLabel extends JLabel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private StarShip ship;
	public StarShip getShip() {
		return ship;
	}
	ShipLabel (StarShip ship){
		super (ship.getDescription(), new ImageIcon(ship.getImage()), JLabel.LEFT);
		setFont(SetupAssaultGroup.FNT_TEXT);
		setForeground(Color.WHITE);
		addMouseListener(new ShipSelectionListener());
		setSize(240,32);
		this.ship = ship;
		
	}
}