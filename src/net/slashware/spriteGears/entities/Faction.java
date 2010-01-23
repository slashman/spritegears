package net.slashware.spriteGears.entities;

public enum Faction {
	HUMAN,
	SPRITE,
	SHADOW;

	public String getDescription() {
		switch (this){
		case HUMAN:
			return "Terran";
		case SPRITE:
			return "...";
		case SHADOW:
			return "Shadow";
		}
		// TODO Auto-generated method stub
		return null;
	}
}
