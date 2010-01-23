package net.slashware.spriteGears.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

import net.slashware.spriteGears.entities.AssaultGroup;
import net.slashware.spriteGears.entities.Faction;
import net.slashware.spriteGears.entities.StarShip;
import net.slashware.spriteGears.game.BattleRules;
import net.slashware.spriteGears.game.Game;
import net.slashware.swing.SwingInformBox;
import net.slashware.util.Direction;
import net.slashware.util.ImageUtils;
import net.slashware.util.Position;
import net.slashware.util.sound.SoundManager;

public class BattleScreen extends Display{
	enum MouseMode {
		SHOWRANGES,
		SELECT_MOVEMENT,
		SELECT_TARGET
	}
	
	public static MouseMode mouseMode = MouseMode.SHOWRANGES;
	public static StarShip currentShip;
	
	private static final class BattleScreenMouseMotionAdapter extends MouseMotionAdapter {
		@Override
		public void mouseMoved(MouseEvent e) {
			
			switch (mouseMode){
			case SHOWRANGES:
				updateBattleScreen();
				// Hovering over ship reveals its fire range and allow issuing orders
				int clickX = (int)Math.floor(e.getX() / 32.0f);
				int clickY = (int)Math.floor(e.getY() / 32.0f);
				StarShip ship = Game.getCurrentGame().getBattleScenario().getEnabledShipAt(clickX, clickY);
				if (ship == null){
					// Clean
				} else {
					//Show ship range
					BattleScreen.drawFireRange(ship);
					BattleScreen.drawMovementRange(ship);
					BattleScreen.showShipStats(ship);
					Display.si.refresh();
	
				}
				break;
			case SELECT_MOVEMENT:
				//Wait for the click... the movement grid must be layed out already
				break;
			}
			//BattleRules.battleTurn(ships);
		}
	}
	private static final class BattleScreenMouseAdapter extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			int clickX = (int)Math.floor(e.getX() / 32.0f);
			int clickY = (int)Math.floor(e.getY() / 32.0f);
			switch (mouseMode){
			case SHOWRANGES:
				//Check if clicked "End Turn"
				if (e.getX() > 514 && e.getX() < 514+128 && e.getY() > 537 && e.getY() < 537+32){
					BattleRules.battleTurn(ships);
					BattleScreen.addMessage("------------");
					return;
				}
				if (availableOrders > 0){
					StarShip ship = Game.getCurrentGame().getBattleScenario().getEnabledShipAt(clickX, clickY);
					if (ship != null){
						if (ship.getFaction() == Faction.HUMAN){
							//Ship commands menu
							BattleScreen.currentShip = ship;
							setMenuActions(ship);
							Display.si.showPopupMenu(BattleScreen.shipCommands, ship.getPosition().x*32+32, ship.getPosition().y*32-16);
						}
					}
				}
				break;
			case SELECT_MOVEMENT:
				//Check if destination is valid, and move there
				BattleRules.setPosition(BattleScreen.currentShip, clickX, clickY);
				availableOrders --;
				BattleScreen.currentShip = null;
				mouseMode = MouseMode.SHOWRANGES;
				updateBattleScreen();
			}
		}
	}

	private static BufferedImage IMG_SPACEBG;
	private static BufferedImage FIRERANGE_IMG;
	private static BufferedImage MOVEMENTRANGE_IMG;
	private static SwingInformBox messageBox;
	private static SwingInformBox commandBox;
	
	static {
		try {
			IMG_SPACEBG = ImageUtils.createImage("img/spacebg2x.png");
			FIRERANGE_IMG = ImageUtils.createImage("img/icons2x.gif", 34, 34, 32, 32);
			MOVEMENTRANGE_IMG = ImageUtils.createImage("img/icons2x.gif", 102, 34, 32, 32);
			messageBox = new SwingInformBox();
			messageBox.setBounds(526,11,266,280);
			messageBox.setForeground(Color.GREEN);
			messageBox.setBackground(Color.BLACK);
			messageBox.setFont(FNT_TEXT);
			messageBox.setEditable(false);
			messageBox.setVisible(true);
			messageBox.setOpaque(false);
			messageBox.setLineWrap(true);
			messageBox.setWrapStyleWord(true);
			
			commandBox = new SwingInformBox();
			commandBox.setBounds(3,520,506,80);
			commandBox.setForeground(Color.RED);
			commandBox.setBackground(Color.BLACK);
			commandBox.setFont(FNT_TEXT);
			commandBox.setEditable(false);
			commandBox.setVisible(true);
			commandBox.setOpaque(false);
			commandBox.setLineWrap(true);
			commandBox.setWrapStyleWord(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static JPopupMenu shipCommands = new JPopupMenu();
	static {
		// Create the menu items
		UIDefaults defaults = UIManager.getDefaults();
		defaults.put("MenuItem.selectionBackground", new ColorUIResource(Color.BLACK));
		defaults.put("MenuItem.selectionForeground", new ColorUIResource(Color.GREEN));
		defaults.put("MenuItem.background", new ColorUIResource(Color.BLACK));
		defaults.put("MenuItem.foreground", new ColorUIResource(Color.WHITE));
		defaults.put("MenuItem.font", new FontUIResource(Display.FNT_TEXT));
		defaults.put("MenuItem.borderPainted", false);
		shipCommands.setBackground(Color.BLACK);
		shipCommands.setBorderPainted(true);
		shipCommands.setBorder(new LineBorder(Color.GREEN, 2));
	}
	
	private static void setMenuActions(StarShip ship){
		shipCommands.removeAll();
		shipCommands.add(menuActions.get("MOVE"));
		//shipCommands.add(menuActions.get("LOCK_TARGET"));
		shipCommands.add(menuActions.get("TURN_LEFT"));
		shipCommands.add(menuActions.get("TURN_RIGHT"));
		List<StarShip> spriteGears = Game.getCurrentGame().getBattleScenario().getEnabledEnemySpriteGears();
		for (StarShip spriteGear: spriteGears){
			if (isSurroundedBy(spriteGear, ship)){
				CaptureSpriteGearActionListener.spriteGear = spriteGear;
				JMenuItem menuItem = menuActions.get("CAPTURE_SPRITEGEAR");
				menuItem.setText("Capture "+spriteGear.getDescription());
				shipCommands.add(menuItem);
			}
		}
		
	}
	
	private static boolean isSurroundedBy(StarShip spriteGear, StarShip ship) {
		List<StarShip> surroundingShips = BattleRules.getSurroundingEnemyShips(spriteGear);
		return surroundingShips.contains(ship);
	}

	private final static Map<String, JMenuItem> menuActions = new HashMap<String, JMenuItem>();
	static {
		JMenuItem move = new JMenuItem("Move");
		JMenuItem lockTarget = new JMenuItem("Lock Target");
		JMenuItem turnLeft = new JMenuItem("Turn Left");
		JMenuItem turnRight = new JMenuItem("Turn Right");
		JMenuItem captureSpriteGear = new JMenuItem("Capture SpriteGear");
		menuActions.put("MOVE", move);
		menuActions.put("LOCK_TARGET", lockTarget);
		menuActions.put("TURN_LEFT", turnLeft);
		menuActions.put("TURN_RIGHT", turnRight);
		menuActions.put("CAPTURE_SPRITEGEAR", captureSpriteGear);

		
		move.addActionListener(new MoveActionListener());
		turnLeft.addActionListener(new TurnLeftActionListener());
		turnRight.addActionListener(new TurnRightActionListener());	
		captureSpriteGear.addActionListener(new CaptureSpriteGearActionListener());
		

	}
	
	public static int availableOrders = 0;
	public static List<StarShip> ships;
	public static MouseListener battleScreenMouseListener;
	public static MouseMotionListener battleScreenMouseMotionListener;
	
	public static void showBattleScreen(){
		//TODO: Add obstacles to the maps (Asteroids)
		// Prepare..
		SoundManager.playFile(Game.getCurrentGame().getBattleScenario().getMusic());
		updateShips();
		Display.si.removeAllComponents();
		Display.si.add(messageBox);
		Display.si.add(commandBox);
		if (battleScreenMouseListener != null)
			si.removeMouseListener(battleScreenMouseListener);
		battleScreenMouseListener = new BattleScreenMouseAdapter();
		battleScreenMouseMotionListener = new BattleScreenMouseMotionAdapter();
		si.addMouseListener(battleScreenMouseListener);
		si.addMouseMotionListener(battleScreenMouseMotionListener);
		
		// Render
		
		BattleScreen.availableOrders = BattleRules.getCommandsPerTurn();
		updateBattleScreen();
		//BattleRules.battleTurn(ships);
	}
	
	public static void showShipStats(StarShip ship) {
		si.drawImage(626, 300, ship.getImage());
		si.printAtPixelCentered(658, 360, ship.getAlignment().getDescription(), Color.WHITE);
		si.printAtPixelCentered(658, 380, ship.getFaction().getDescription()+" "+ship.getDescription(), Color.RED);
		si.printAtPixelCentered(658, 400, "Energy: "+ship.getEnergyDescription(), Color.WHITE);
		si.printAtPixelCentered(658, 420, "Power: "+ship.getAttack(), Color.WHITE);
		si.printAtPixelCentered(658, 440, "Recover: "+ship.getShieldRecovery(), Color.WHITE);
		si.printAtPixelCentered(658, 460, "Speed: "+ship.getSpeed(), Color.WHITE);
	}

	public static void drawFireRange(StarShip ship) {
		boolean[][] fireRange = ship.locateFireMask();
		for (int x = 0; x < fireRange.length; x++){
			for (int y = 0; y < fireRange[0].length; y++){
				if (fireRange[x][y]){
					Display.si.drawImage(x*32,y*32, FIRERANGE_IMG);
				}
			}
		}
	}

	public static void drawMovementRange(StarShip ship) {
		boolean[][] movementRange = ship.locateMovementMask();
		for (int x = 0; x < movementRange.length; x++){
			for (int y = 0; y < movementRange[0].length; y++){
				if (movementRange[x][y]){
					Display.si.drawImage(x*32,y*32, MOVEMENTRANGE_IMG);
				}
			}
		}
	}

	protected static void showCommand(String string) {
		commandBox.setText(string);
	}

	public static void updateShips(){
		ships = new ArrayList<StarShip>();
		for (AssaultGroup group: Game.getCurrentGame().getBattleScenario().getGroups()){
			ships.addAll(group.getEnabledStarShips());
		}
	}
	
	public static void updateBattleScreen(){
		si.cls();
		si.setFont(FNT_TITLE);
		si.drawImage(0, 0, IMG_SPACEBG);
		for (StarShip ship: ships){
			BufferedImage shipImage = getFlippedImage(ship);
			
			
			si.drawImage(ship.getPosition().x * 32 - 10, ship.getPosition().y*32 - 4, shipImage);
		}
		si.printAtPixel(520, 528, "CMD: "+availableOrders);
		si.printAtPixel(520, 558, "End Turn", Color.RED);
		si.refresh();
	}
	
	
	private static BufferedImage getFlippedImage(StarShip ship) {
		switch (ship.getAlignment()){
		case UP:
			return ship.getImage();
		case DOWN:
			return ImageUtils.hFlip(ship.getImage());
		case LEFT:
			return ImageUtils.rotate(ship.getImage(), Math.PI * (3.0d/ 2.0d));
		case RIGHT:
			return ImageUtils.rotate(ship.getImage(), Math.PI / 2.0d);
		}
		return null;
	}

	public static void addMessage(String message){
		messageBox.addText(message);
	}

	public static void showBlast(Position position) {
		//TODO: Fix this.. doesn't want to draw the image, probably threading issue. Can't get stuck fixing it
		/*addMessage("Boom!");
		si.saveBuffer();
		si.drawImage(position.x*32,position.y*32, FIRERANGE_IMG);
		si.refresh();
		Game.delay(500);
		si.restore();*/
	}

	public static void hideBattleScreen() {
		Display.si.removeAllComponents();
		if (battleScreenMouseListener != null)
			si.removeMouseListener(battleScreenMouseListener);
		if (battleScreenMouseMotionListener != null)
			si.removeMouseMotionListener(battleScreenMouseMotionListener);
		messageBox.clear();
		si.cls();
		si.refresh();
	}
}

class TurnRightActionListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		if (BattleScreen.availableOrders > 0){
			BattleRules.turnShip(BattleScreen.currentShip, Direction.RIGHT);
			BattleScreen.availableOrders --;
		}
	}
}
class TurnLeftActionListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		if (BattleScreen.availableOrders > 0){
			BattleRules.turnShip(BattleScreen.currentShip, Direction.LEFT);
			BattleScreen.availableOrders --;
		}
	}
}
class MoveActionListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		if (BattleScreen.availableOrders > 0){
			BattleScreen.updateBattleScreen();

			StarShip ship = BattleScreen.currentShip;
			BattleScreen.showCommand("Select a destination for "+ship.getDescription());
			//Draw grid
			BattleScreen.drawMovementRange(ship);
			Display.si.refresh();
			//Change mouse mode
			BattleScreen.mouseMode = BattleScreen.MouseMode.SELECT_MOVEMENT;
		} else {
			BattleScreen.showCommand("You can't issue more orders for this turn!");
		}
	}
}

class CaptureSpriteGearActionListener implements ActionListener {
	public static StarShip spriteGear;

	public void actionPerformed(ActionEvent e) {
		if (spriteGear == null){
			return;
		}
		BattleRules.captureSpriteGear(spriteGear);
	}
}