package net.slashware.util;

public enum Direction {
	UP,
	DOWN,
	LEFT,
	RIGHT,
	UPRIGHT,
	UPLEFT,
	DOWNRIGHT,
	DOWNLEFT,
	SELF;
	
	public final static Position VARUP = new Position(0,-1);
	public final static Position VARDN = new Position(0,1);
	public final static Position VARLF = new Position(-1,0);
	public final static Position VARRG = new Position(1,0);
	public final static Position VARUR = new Position(1,-1);
	public final static Position VARUL = new Position(-1,-1);
	public final static Position VARDR = new Position(1,1);
	public final static Position VARDL = new Position(-1,1);
	public final static Position VARSL = new Position(0,0);

	public Position toVariation(){
		switch (this){
			case UP:
			return VARUP;
			case DOWN:
			return VARDN;
			case LEFT:
			return VARLF;
			case RIGHT:
			return VARRG;
			case UPRIGHT:
			return VARUR;
			case UPLEFT:
			return VARUL;
			case DOWNRIGHT:
			return VARDR;
			case DOWNLEFT:
			return VARDL;
			case SELF:
			return VARSL;
			default:
			return null;
		}
	}
	public static Direction getGeneralDirection(Position from, Position to){
		if (from.x == to.x)
			if (from.y > to.y)
				return UP;
			else if (from.y < to.y)
				return DOWN;
			else return SELF;
		else if (from.x > to.x)
			if (from.y > to.y)
				return UPLEFT;
			else if (from.y < to.y)
				return DOWNLEFT;
			else return LEFT;
		else {
			if (from.y > to.y)
				return UPRIGHT;
			else if (from.y < to.y)
				return DOWNRIGHT;
			else return RIGHT;
		}
	}
	public String getDescription() {
		switch (this){
		case UP:
		return "Up";
		case DOWN:
		return "Down";
		case LEFT:
		return "Left";
		case RIGHT:
		return "Right";
		case UPRIGHT:
		return "Up Right";
		case UPLEFT:
		return "Up Left";
		case DOWNRIGHT:
		return "Down Right";
		case DOWNLEFT:
		return "Down Left";
		case SELF:
		return "Self";
		default:
		return null;
	}
	}
}
