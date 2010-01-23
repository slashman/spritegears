package net.slashware.spriteGears.entities;

import java.util.List;

import net.slashware.spriteGears.action.SpriteGearAI;
import net.slashware.spriteGears.game.Game;
import net.slashware.spriteGears.ui.Display;
import net.slashware.util.Position;

public class SpriteGear extends StarShip {
	private String name;
	private int speed;
	private int shield;

	public int getShield() {
		return shield;
	}
	public void setShield(int shield) {
		this.shield = shield;
	}
	public SpriteGear(){
		setActionSelector(new SpriteGearAI());
	}
	@Override
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getDescription() {
		return getName();
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
	
	@Override
	public List<Position> getFireMask() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Position> getMovementMask() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean isDestroyed() {
		return false;
	}
	
	@Override
	public boolean isSpriteGear() {
		return true;
	}
	
	@Override
	public String getEnergyDescription() {
		return "NA";
	}
	
	@Override
	public int getShieldRecovery() {
		return 0;
	}
	
	@Override
	public void recover() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public int getAttack() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int getPrice() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
