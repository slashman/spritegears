package net.slashware.spriteGears.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.slashware.spriteGears.entities.AssaultGroup;
import net.slashware.spriteGears.entities.StarShip;
import net.slashware.spriteGears.game.BattleRules;
import net.slashware.spriteGears.game.Game;
import net.slashware.util.Direction;
import net.slashware.util.Line;
import net.slashware.util.Position;
import net.slashware.util.Util;

import static net.slashware.util.Direction.*;
/**
 * AI to play the combats
 * Agent has a collection of ships and seeks to destroy the ships of the enemy.
 * Available actions:
 *  Move Ship: restricted by the ship movement range, 
 *  
 * @author Slash
 *
 */
public class BasicComputerAI {
	private AssaultGroup assaultGroup;
	private int bandSize;
	private int availableCommands;
	
	public BasicComputerAI(AssaultGroup assaultGroup, int bandSize){
		this.assaultGroup = assaultGroup;
		this.bandSize = bandSize;
	}
	
	public void activate(){
		availableCommands = BattleRules.getCommandsPerTurn();
		while (availableCommands > 0){
			selectAction();
			availableCommands--;
		}
			
	}
	
	/**
	 * Look for the target to which more ships are closer
	 * Pick the n nearest ships
	 * Remove the ships which are already attacking a target
	 * If all n ships are attacking, end.
	 * Select the ship which is further
	 * "look" at the target from the chosen ship, to see if turning is needed
	 * if not, move as far as possible in direction of the ship
	 */
	public void selectAction(){
		// Look for the target to which more ships are closer
		List<StarShip> enemyShips = Game.getCurrentGame().getBattleScenario().getEnabledEnemyShipsFor(assaultGroup.getFaction());
		if (enemyShips.size() == 0){
			//Naught to do.. check for death
			//Game.getCurrentGame().checkGameOver();
			availableCommands = 0;
			return;
		}
			
		HashMap<Position, EvaluatedShip> evaluatedEnemyShips = new HashMap<Position, EvaluatedShip>();
		for (StarShip enemyShip: enemyShips){
			for (StarShip myShip: assaultGroup.getEnabledStarShips()){
				EvaluatedShip evaluatedShip = evaluatedEnemyShips.get(enemyShip.getPosition());
				if (evaluatedShip == null){
					evaluatedShip = new EvaluatedShip(enemyShip);
					evaluatedEnemyShips.put(enemyShip.getPosition(), evaluatedShip);
				}
				evaluatedShip.setEvaluation(evaluatedShip.getEvaluation() + Position.distance(enemyShip.getPosition(), myShip.getPosition()));
			}
		}
		List<EvaluatedShip> evaluatedEnemyShipsList = new ArrayList<EvaluatedShip>(evaluatedEnemyShips.values());
		Collections.sort(evaluatedEnemyShipsList, EvaluatedShip.ASCENDING_COMPARATOR);
		
		StarShip preferredTarget = evaluatedEnemyShipsList.get(0).getShip();
		
		// Pick the bandsize nearest ships
		// Remove the ships which are already attacking a target

		List <EvaluatedShip> evaluatedOwnShips = new ArrayList<EvaluatedShip>();
		for (StarShip myShip: assaultGroup.getEnabledStarShips()){
			evaluatedOwnShips.add(new EvaluatedShip(myShip, Position.distance(myShip.getPosition(), preferredTarget.getPosition())));
		}
		Collections.sort(evaluatedOwnShips, EvaluatedShip.ASCENDING_COMPARATOR);
		
		List<StarShip> band = new ArrayList<StarShip>();
		for (int i = 0; i < bandSize && i < evaluatedOwnShips.size(); i++){
			if (evaluatedOwnShips.get(i).getShip().getTarget() == null)
				band.add(evaluatedOwnShips.get(i).getShip());
		}
		
		// If all n ships are attacking, end.
		if (band.size() == 0){
			//Lazy Agent. All is well... kill the enemy or die
			availableCommands = 0;
			return;
		}
		
		// Select the ship which is further
		StarShip chosenShip = band.get(band.size()-1);
		
		// "look" at the target from the chosen ship, to see if turning is needed
		Direction generalDirection = Direction.getGeneralDirection(chosenShip.getPosition(), preferredTarget.getPosition());
		Position generalVariation = generalDirection.toVariation();
		Position currentVariation = chosenShip.getAlignment().toVariation();
		if (currentVariation.x != generalVariation.x && currentVariation.y != generalVariation.y){
			Direction rotation = null;
			// Rotate Left or right, as per generalVariation
			switch(chosenShip.getAlignment()){
			case UP:
				if (generalDirection == Direction.UPLEFT || generalDirection == Direction.LEFT || generalDirection == Direction.DOWNLEFT)
					rotation = Direction.LEFT;
				else
					rotation = Direction.RIGHT;
				break;
			case DOWN:
				if (generalDirection == Direction.UPLEFT || generalDirection == Direction.LEFT || generalDirection == DOWNLEFT)
					rotation = Direction.RIGHT;
				else
					rotation = Direction.LEFT;
				break;
			case LEFT:
				if (generalDirection == UPLEFT || generalDirection == UP || generalDirection == UPRIGHT)
					rotation = RIGHT;
				else
					rotation = LEFT;
				break;
			case RIGHT:
				if (generalDirection == UPLEFT || generalDirection == UP || generalDirection == UPRIGHT)
					rotation = LEFT;
				else
					rotation = RIGHT;
				break;
			}
			rotate(chosenShip, rotation);
		} else {
			// if not, move as far as possible in direction of the ship, stopping in front of it
			Line l = new Line(chosenShip.getPosition(), preferredTarget.getPosition());
			boolean [][] movementMask = chosenShip.locateMovementMask();
			Position runner = l.next();
			Position next = l.next();
			while (BattleRules.insideBounds(next.x, next.y) && movementMask[next.x][next.y] && !next.equals(preferredTarget.getPosition())){
				runner = next;
				next = l.next();
			}
			if (runner.equals(chosenShip.getPosition()))
				moveRandomly(chosenShip);
			else if (BattleRules.canMove(chosenShip, runner.x, runner.y))
				move(chosenShip, runner);
			else
				moveRandomly(chosenShip);
		}
	}

	
	private void moveRandomly(StarShip chosenShip) {
		// Get a valid movement in the grid
		@SuppressWarnings("unused")
		Position random = (Position)Util.randomElementOf(chosenShip.getMovementMask());
		BattleRules.setPosition(chosenShip, chosenShip.getPosition().x + random.x, chosenShip.getPosition().y + random.y);
	}

	private void move(StarShip chosenShip, Position runner) {
		BattleRules.setPosition(chosenShip, runner.x, runner.y);
		
	}

	private void rotate(StarShip chosenShip, Direction rotation) {
		BattleRules.turnShip(chosenShip, rotation);
	}
}
