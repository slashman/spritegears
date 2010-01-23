package net.slashware.spriteGears.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.slashware.spriteGears.ai.EvaluatedShip;
import net.slashware.spriteGears.entities.FighterShip;
import net.slashware.spriteGears.entities.StarShip;
import net.slashware.spriteGears.game.BattleRules;
import net.slashware.spriteGears.game.Game;
import net.slashware.spriteGears.ui.BattleScreen;
import net.slashware.spriteGears.ui.Display;
import net.slashware.util.Position;
import net.slashware.util.Util;

/**
 * Ships do the following:
 *  - Fire if an enemy is in range
 * 
 * Ships move automatically if they move in the same direction twice
 * Ships consume energy on every turn if their engines are on
 * 
 * @author Slash
 *
 */
public class ShipAI extends ActionSelector{
	private FighterShip ship;
	
	public ShipAI(FighterShip ship){
		this.ship = ship;
	}

	@Override
	public void act() {
		//Implicit actions
		ship.getPosition().y += ship.getYSpeed();
		ship.getPosition().x += ship.getXSpeed();
		ship.reduceEnergy(1);
		
		// Hostile Actions
		if (!isTargetInRange()){
			//Target is gone
			ship.setTarget(null);
		} 
			
		if (ship.getTarget() == null){
			// Try to acquire target from the firing zone
			getTarget();
		}
		
		if (ship.getTarget() != null) {
			//BattleScreen.addMessage(ship.getDescription()+", firing at "+ship.getTarget().getDescription());
			//Fire
			fireAtTarget();
		} else {
			//BattleScreen.addMessage(ship.getDescription()+", no enemies in range");
		}
	}

	private void fireAtTarget() {
		if (ship.getTarget() == null)
			Display.si.showMessage("No target");
		int damage = ship.getAttack();
		BattleScreen.showBlast(ship.getTarget().getPosition());
		BattleScreen.addMessage(ship.getTarget().getDescription()+" is hit for "+damage+" damage!");
		StarShip target = ship.getTarget();
		target.damage(damage);
		if (target.isDisabled()){
			BattleScreen.addMessage(target.getDescription()+" is destroyed!");
			ship.setTarget(null);
		}
		
	}

	private void getTarget() {
		List<StarShip> nearEnemies = new ArrayList<StarShip>(); 
		List<StarShip> enemies = Game.getCurrentGame().getBattleScenario().getEnabledEnemyShipsFor(ship.getFaction());
		boolean[][] fireMask = ship.locateFireMask();
		// Exclude ships outside scanner range
		for (StarShip enemy: enemies){
			if (fireMask[enemy.getPosition().x][enemy.getPosition().y]){
				nearEnemies.add(enemy);
			}
		}
		
		if (nearEnemies.size() == 0){
			//Nothing to do.
			return;
		}

		// Evaluate each target for distance, coverage (already being attacked) and power
		List<EvaluatedShip> evaluatedShips = new ArrayList<EvaluatedShip>();
		for (StarShip enemy: nearEnemies){
			evaluatedShips.add(evaluateEnemy(enemy));
		}

 		// Lock into the best target
		Collections.sort(evaluatedShips, EvaluatedShip.DESCENDING_COMPARATOR);
		ship.setTarget(evaluatedShips.get(0).getShip());
	}
	
	/**
	 * Evaluate each target for distance, coverage (already being attacked) and power
	 * TODO: Implement Coverage and Power, distance only for now
	 * @param enemy
	 * @return
	 */
	private EvaluatedShip evaluateEnemy(StarShip enemy) {
		EvaluatedShip ret = new EvaluatedShip(enemy);
		ret.setEvaluation(Position.distance(ship.getPosition(), enemy.getPosition()));
		return ret;
	}
	


	private boolean isTargetInRange() {
		if (ship.getTarget() == null)
			return false;
		boolean[][] fireMask = ship.locateFireMask();
		return fireMask[ship.getTarget().getPosition().x][ship.getTarget().getPosition().y];
	}
	
}
