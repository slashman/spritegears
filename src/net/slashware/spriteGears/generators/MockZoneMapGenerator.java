package net.slashware.spriteGears.generators;

import net.slashware.spriteGears.entities.StarSection;
import net.slashware.spriteGears.entities.StarZone;
import net.slashware.util.Position;

public class MockZoneMapGenerator {
	
	private final String[] testMap = new String[]{
		"   E.      ",
		".....      ",
		" S  E    Y ",
		"   ..X.... ",
		"     E..   ",
		"       ..  ",
		"        Y  "
	};
	
	public void generateZoneMap(StarZone zone) {
		for (int y = 0; y < testMap.length; y++){
			for (int x = 0; x < testMap[y].length(); x++){
				char icon = testMap[y].charAt(x);
				switch (icon){
				case ' ':
					break;
				case '.':
					zone.setSection(x, y, StarSection.VOID);
					break;
				case '*':
					zone.setSection(x, y, StarSection.TERRAN_SPACE_STATION);
					break;
				case 'S':
					zone.setSection(x, y, StarSection.MOTHERSHIP);
					zone.setEntryPosition(new Position(x,y));
					break;
				case 'E':
					zone.setSection(x, y, StarSection.ENEMY);
					break;
				case 'X':
					zone.setSection(x, y, StarSection.ENERGY);
					break;
				case 'Y':
					zone.setSection(x, y, StarSection.BIG_ENERGY);
					break;
				}
			}
		}
		
	}
}
