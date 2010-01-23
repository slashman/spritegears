package net.slashware.spriteGears.factory;

import java.awt.image.BufferedImage;
import java.util.List;

import net.slashware.util.Position;

public class FighterShipPrototype {
	private String model;
	public String getModel() {
		return model;
	}

	public int getSpeed() {
		return speed;
	}

	public int getAttack() {
		return attack;
	}

	public List<Position> getFireMask() {
		return fireMask;
	}

	public BufferedImage getImage() {
		return image;
	}

	private int speed;
	private int attack;
	private int maxShield;
	private int shieldRecover;
	private int price;
	
	public int getPrice() {
		return price;
	}

	public int getShieldRecover() {
		return shieldRecover;
	}

	public int getMaxShield() {
		return maxShield;
	}

	private List<Position> fireMask;
	private List<Position> movementMask;
	public List<Position> getMovementMask() {
		return movementMask;
	}

	private BufferedImage image;
	private String shortName;
	private boolean isSpriteGear;

	public FighterShipPrototype(String model, String shortName, boolean isSpriteGear, int speed, int attack, int maxShield, int shieldRecover, int price,
			BufferedImage image) {
		this.model = model;
		this.shortName = shortName;
		this.shieldRecover = shieldRecover;
		this.isSpriteGear = isSpriteGear;
		this.speed = speed;
		this.attack = attack;
		this.image = image;
		this.maxShield = maxShield;
		this.price = price;
	}
	
	public void setMasks(List<Position> fireMask, List<Position> movementMask){
		this.fireMask = fireMask;
		this.movementMask = movementMask;
		
	}

	public String getShortName() {
		return shortName;
	}

	public boolean isSpriteGear() {
		return isSpriteGear;
	}
	
}
