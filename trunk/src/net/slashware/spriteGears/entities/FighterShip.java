package net.slashware.spriteGears.entities;

import java.util.List;


import net.slashware.spriteGears.action.ShipAI;
import net.slashware.util.Position;

public class FighterShip extends StarShip{
	// Factory Attributes
	private String model;
	private int speed;
	private int attack;
	private List<Position> baseFireMask;
	private int maxShield;
	private int shieldRecovery;
	
	public int getMaxShield() {
		return maxShield;
	}

	// Transient Status
	private int xSpeed, ySpeed;
	private int energy;
	private int shield;
	private List<Position> baseMovementMask;
	private String shortName;
	private boolean isSpriteGear;
	private int price;
	
	
	public int getShield() {
		return shield;
	}

	public void setShield(int shield) {
		this.shield = shield;
	}

	

	public int getXSpeed() {
		return xSpeed;
	}

	public void setXSpeed(int xSpeed) {
		this.xSpeed = xSpeed;
	}

	public int getYSpeed() {
		return ySpeed;
	}

	public void setYSpeed(int ySpeed) {
		this.ySpeed = ySpeed;
	}

	public FighterShip(String model, String shortName, boolean isSpriteGear, int speed, 
			int attack, int maxShield, int shieldRecovery, int price, 
			List<Position> baseFireMask, List<Position> baseMovementMask){
		setActionSelector(new ShipAI(this));
		this.isSpriteGear = isSpriteGear;
		this.model = model;
		this.shortName = shortName;
		this.speed = speed;
		this.attack = attack;
		this.baseFireMask = baseFireMask;
		this.baseMovementMask = baseMovementMask;
		this.maxShield = maxShield;
		this.shield = maxShield;
		this.shieldRecovery = shieldRecovery;
		this.price = price;
	}

	@Override
	public int getSpeed() {
		return speed;
	}

	public String getModel() {
		return model;
	}
	
	public String getShortName(){
		return shortName;
	}


	public void reduceEnergy(int energy) {
		this.energy -= energy;
	}
	
	public boolean hasEnergy(){
		return energy > 0;
	}

	
	public List<Position> getBaseFireMask() {
		return baseFireMask;
	}

	public List<Position> getFireMask() {
		return baseFireMask;
	}
	
	public List<Position> getMovementMask() {
		return baseMovementMask;
	}
	
	@Override
	public String getDescription() {
		return getShortName();
	}

	public int getAttack() {
		return attack;
	}

	@Override
	public void damage(int damage) {
		shield -= damage;
		checkDeath();
	}

	private void checkDeath() {
		if (isDestroyed()){
			disable();
		}
	}
	
	private boolean isDestroyed() {
		return shield < 0;
	}
	
	@Override
	public boolean isSpriteGear() {
		return isSpriteGear;
	}
	
	@Override
	public String getEnergyDescription() {
		return getShield()+"/"+getMaxShield();
	}
	
	@Override
	public int getShieldRecovery() {
		return shieldRecovery;
	}
	
	@Override
	public void recover() {
		if (getShield() < getMaxShield())
			setShield(getShield()+getShieldRecovery());
		if (getShield() > getMaxShield())
			setShield(getMaxShield());
	}
	
	@Override
	public int getPrice() {
		return price;
	}
	
	@Override
	public void recoverAll() {
		setShield(getMaxShield());
	}

	@Override
	public double getProportionalShield() {
		return (double)getShield()/(double)getMaxShield();
	}

}	
