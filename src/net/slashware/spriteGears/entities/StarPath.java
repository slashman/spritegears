package net.slashware.spriteGears.entities;

public class StarPath {
	public StarPath(StarZone to, int cost){
		setCost(cost);
		setTo(to);
	}
	
	private StarZone to;
	private int cost;
	public StarZone getTo() {
		return to;
	}
	public void setTo(StarZone to) {
		this.to = to;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	
}
