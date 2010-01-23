package net.slashware.spriteGears.entities;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.slashware.spriteGears.action.ActionSelector;
import net.slashware.spriteGears.game.BattleRules;
import net.slashware.spriteGears.game.ShipEventListener;
import net.slashware.util.Direction;
import net.slashware.util.Position;

public abstract class StarShip implements ShipEventListener{
	private BufferedImage image;
	private Position position;
	private Direction alignment; 
	private ActionSelector actionSelector;
	private List<ShipEventListener> listeners = new ArrayList<ShipEventListener>();
	private boolean disabled;
	
	// Assault Group 
	private Faction faction;
	
	// Transient Status
	private StarShip target;

	public void setFaction(Faction faction) {
		this.faction = faction;
	}
	
	public Faction getFaction() {
		return faction;
	}
	
	public ActionSelector getActionSelector() {
		return actionSelector;
	}

	public void setActionSelector(ActionSelector actionSelector) {
		this.actionSelector = actionSelector;
	}

	public Position getPosition() {
		return position;
	}

	public final void setPosition(Position position) {
		cachedFireMaskInvalid = true;
		cachedMovementMaskInvalid = true;
		this.position = position;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public abstract int getSpeed();
	
	public abstract String getDescription();

	public abstract void damage(int damage);

	private final boolean [][] cachedFireMask  = new boolean[BattleRules.BATTLE_ZONE_WIDTH][BattleRules.BATTLE_ZONE_HEIGHT];
	private boolean cachedFireMaskInvalid = true;
	public boolean[][] locateFireMask() {
		if (!cachedFireMaskInvalid)
			return cachedFireMask;
		cachedFireMaskInvalid = false;
		for (int i = 0; i < cachedFireMask.length; i++){
			Arrays.fill(cachedFireMask[i], false);
		}
		for (Position var: getFireMask()){
			int xPos = getPosition().x+transformX(var.x, var.y);
			int yPos = getPosition().y-transformY(var.x, var.y);
			if (!BattleRules.insideBounds(xPos, yPos))
				continue;
			cachedFireMask [xPos][yPos] = true;
		}
		return cachedFireMask;
	}
	private final boolean [][] cachedMovementMask = new boolean[BattleRules.BATTLE_ZONE_WIDTH][BattleRules.BATTLE_ZONE_HEIGHT];
	private boolean cachedMovementMaskInvalid = true;
	
	public boolean[][] locateMovementMask() {
		if (!cachedMovementMaskInvalid)
			return cachedMovementMask;
		cachedMovementMaskInvalid = false;
		for (int i = 0; i < cachedMovementMask.length; i++){
			Arrays.fill(cachedMovementMask[i], false);
		}
		
		for (Position var: getMovementMask()){
			int xPos = getPosition().x+transformX(var.x, var.y);
			int yPos = getPosition().y-transformY(var.x, var.y);
			if (!BattleRules.insideBounds(xPos, yPos))
				continue;
			cachedMovementMask [xPos][yPos] = true;
		}
		return cachedMovementMask;
	}
	
	public abstract List<Position> getFireMask();
	public abstract List<Position> getMovementMask();

	public Direction getAlignment() {
		return alignment;
	}

	public void setAlignment(Direction alignment) {
		cachedMovementMaskInvalid = true;
		cachedFireMaskInvalid = true;
		this.alignment = alignment;
	}
	
	private int transformX(int varX, int varY){
		switch (alignment){
		case UP:
			return varX;
		case DOWN:
			return -varX;
		case LEFT:
			return -varY;
		case RIGHT:
			return varY;
		}
		return -1;
	}
	
	private int transformY(int varX, int varY){
		switch (alignment){
		case UP:
			return varY;
		case DOWN:
			return -varY;
		case LEFT:
			return -varX;
		case RIGHT:
			return varX;
		}
		return -1;
	}

	public StarShip getTarget() {
		return target;
	}

	public void setTarget(StarShip target) {
		if (target != null)
			addShipEventListener(target);
		this.target = target;
	}
	
	public void addShipEventListener(ShipEventListener listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}
	
	private void fireShipDisabledEvent(){
		for (ShipEventListener listener: listeners){
			listener.shipDisabled(this);
		}
		this.listeners.clear();
	}
	
	public void shipDisabled(StarShip ship) {
		if (getTarget() == ship){
			setTarget(null);
		}
	}

	public void disable() {
		disabled = true;
		fireShipDisabledEvent();
	}

	public boolean isDisabled() {
		return disabled;
	}

	public abstract boolean isSpriteGear();

	public void removeShipEventListener(ShipEventListener listener) {
		listeners.remove(listener);
	}

	public abstract String getEnergyDescription();

	public abstract int getShieldRecovery();

	public abstract void recover();

	public abstract int getAttack();

	public abstract int getPrice();

	public abstract void recoverAll();
	
}
