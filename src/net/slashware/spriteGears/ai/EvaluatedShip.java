package net.slashware.spriteGears.ai;

import java.util.Comparator;

import net.slashware.spriteGears.entities.StarShip;

public class EvaluatedShip {
	private StarShip ship;
	private int evaluation;
	public EvaluatedShip(StarShip ship, int evaluation) {
		this.ship = ship;
		this.evaluation = evaluation;
	}
	
	public EvaluatedShip(StarShip ship) {
		this.ship = ship;
	}
	
	public StarShip getShip() {
		return ship;
	}
	public void setShip(StarShip ship) {
		this.ship = ship;
	}
	public int getEvaluation() {
		return evaluation;
	}
	public void setEvaluation(int evaluation) {
		this.evaluation = evaluation;
	}
	
	static class AscendingComparator implements Comparator<EvaluatedShip>{
		public int compare(EvaluatedShip ship1, EvaluatedShip ship2) {
			return ship1.getEvaluation()-ship2.getEvaluation();
		}
	}

	public final static Comparator<EvaluatedShip> DESCENDING_COMPARATOR = new  Comparator<EvaluatedShip> () {
		public int compare(EvaluatedShip ship1, EvaluatedShip ship2) {
			return ship2.getEvaluation()-ship1.getEvaluation();
		}
	};
	
	public final static Comparator<EvaluatedShip> ASCENDING_COMPARATOR = new  Comparator<EvaluatedShip> () {
		public int compare(EvaluatedShip ship1, EvaluatedShip ship2) {
			return ship1.getEvaluation()-ship2.getEvaluation();
		}
	};
	
	
}