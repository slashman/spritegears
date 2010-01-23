package net.slashware.spriteGears.factory;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.slashware.spriteGears.entities.Faction;
import net.slashware.spriteGears.entities.FighterShip;
import net.slashware.spriteGears.entities.StarShip;
import net.slashware.util.FileUtil;
import net.slashware.util.ImageUtils;
import net.slashware.util.Position;
import net.slashware.util.Util;

public class ShipFactory {
	private static List<String> spriteGears = new ArrayList<String>();
	private static Map<Integer, List<String>> shipModelsByDifficulty = new HashMap<Integer, List<String>>();
	private static List<FighterShipPrototype> starShipsPrototypes;
	static {
		try {
			starShipsPrototypes = new ArrayList<FighterShipPrototype>();
			//Read the basic attributes
			BufferedReader r = FileUtil.getReader("data/shipData.csv");
			Map<String, FighterShipPrototype> starShipsPrototypesHash = new HashMap<String, FighterShipPrototype>();
			String line = r.readLine();
			line = r.readLine(); // Skip first line
			while (line != null){
				String[] row = line.split(";");
				String model = row[0];
				String name = row[1];
				String imageSource = row[2];
				String[] imageBoundsStr = row[3].split(",");
				Rectangle imageBounds = new Rectangle(i(imageBoundsStr[0]),i(imageBoundsStr[1]),i(imageBoundsStr[2]),i(imageBoundsStr[3]));
				boolean isSpriteGear = row[4].equals("true");
				int speed = i(row[5]);
				int attack = i(row[6]);
				int shield = i(row[7]);
				int recover = i(row[8]);
				int price = i(row[9]);
				int difficulty = i(row[10]);
				FighterShipPrototype fighterShip = new FighterShipPrototype(model, name, isSpriteGear, speed,
						attack, shield, recover, price,
						ImageUtils.createImage(imageSource, imageBounds.x, imageBounds.y, imageBounds.width, imageBounds.height));
				starShipsPrototypes.add(fighterShip);
				
				starShipsPrototypesHash.put(name, fighterShip);
				if (isSpriteGear){
					spriteGears.add(fighterShip.getModel());
				} else 
					addShipModel(fighterShip.getModel(), difficulty);
				line = r.readLine(); //Skip a line
			}
			r.close();
			//Read the grids
			r = FileUtil.getReader("data/gridData.dat");
			line = r.readLine();
			List<String> maskList = new ArrayList<String>();
			while (line != null){
				/* Example:
				 
				name,Mantis
				fireMask
				xxx
				xxx
				xxx
				-@-
				movementMask
				-x-
				xxx
				x@x
				xxx
				*/
				FighterShipPrototype ship = starShipsPrototypesHash.get(line);
				if (ship == null){
					System.out.println(line+" is null");
				}
				maskList.clear();
				while (!line.equals("movementMask")){
					maskList.add(line);
					line = r.readLine();
				}
				String[] fireMask = toStringArray(maskList);
				line = r.readLine(); //Skip a line
				maskList.clear();
				while (!line.equals("endShip")){
					maskList.add(line);
					line = r.readLine();
				}
				String[] moveMask = toStringArray(maskList);
				
				ship.setMasks(getMask(fireMask), getMask(moveMask));
				line = r.readLine();
				line = r.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static FighterShip createShip(String model){
		for (FighterShipPrototype prototype: starShipsPrototypes){
			if (prototype.getModel().equals(model)){
				FighterShip ret = new FighterShip(prototype.getModel(), prototype.getShortName(), prototype.isSpriteGear(), prototype.getSpeed(), prototype.getAttack(), prototype.getMaxShield(), prototype.getShieldRecover(), prototype.getPrice(), prototype.getFireMask(), prototype.getMovementMask());
				ret.setImage(prototype.getImage());
				ret.setFaction(Faction.HUMAN);
				return ret;
			}
		}
		return null;
	}
	
	private static void addShipModel(String model, int difficulty) {
		List<String> list = shipModelsByDifficulty.get(difficulty);
		if (list == null){
			list = new ArrayList<String>();
			shipModelsByDifficulty.put(difficulty, list);
		}
		list.add(model);
		
	}

	private static String[] toStringArray(List<String> maskList) {
		String[] ret = new String[maskList.size()];
		int i = 0;
		for (String string: maskList){
			ret[i] = string;
			i++;
		}
		return ret;
	}

	private static int i(String string) {
		return Integer.parseInt(string.trim());
	}

	private static List<Position> getMask(String[] strings) {
		List<Position> ret = new ArrayList<Position>();
		int shipCol = -1;
		int shipRow = -1;
		//Look for the @
		for (int i = 0; i < strings.length; i++){
			String row = strings[i];
			shipCol = row.indexOf('@'); 
			if (shipCol != -1){
				shipRow = i;
				break; 
			}
		}
		
		for (int i = 0; i < strings.length; i++){
			String row = strings[i];
			for (int j = 0; j < row.length(); j++){
				char tile = row.charAt(j);
				if (tile == 'x'){
					int xRel = j - shipCol;
					int yRel = shipRow - i;
					ret.add(new Position(xRel, yRel));
				}
			}
			
		}
		return ret;
	}

	
	private static List<String> usedSpriteGears = new ArrayList<String>();
	public static String selectRandomUnusedSpriteGearModel() {
		int tries = 0;
		while (tries < 1000){
			String randomSpriteGear = (String)Util.randomElementOf(spriteGears);
			if (!usedSpriteGears.contains(randomSpriteGear)){
				usedSpriteGears.add(randomSpriteGear);
				return randomSpriteGear;
			}
			tries++;
		}
		return spriteGears.get(0);
	}

	
	public static String selectShipModelByDifficulty(int difficulty) {
		return (String) Util.randomElementOf(shipModelsByDifficulty.get(difficulty));
	}

	public static StarShip createShipModelByDifficulty(int difficulty) {
		return createShip(selectShipModelByDifficulty(difficulty));
	}


}
