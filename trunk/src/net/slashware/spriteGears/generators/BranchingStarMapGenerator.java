package net.slashware.spriteGears.generators;

import java.util.HashMap;
import java.util.Map;

import net.slashware.spriteGears.entities.StarMap;
import net.slashware.spriteGears.entities.StarZone;
import net.slashware.util.Util;

public class BranchingStarMapGenerator implements StarMapGenerator {

	public StarMap generateStarmap() {
		StarMap ret = new StarMap();
		StarZone orice = new StarZone();
		orice.setName("Orice");
		orice.setDifficulty(0);
		addNodes(orice, 0);
		ret.setInitialZone(orice);
		return ret;
	}
	
	private final static int MAX_DEPTH = 3;
	private final static int MAX_LINKS = 3;
	private void addNodes(StarZone zone, int depth){
		if (depth > MAX_DEPTH)
			return;
		
		int zones = Util.rand(1, MAX_LINKS);
		for (int i = 0; i < zones; i++){
			StarZone newZone = new StarZone();
			newZone.setName(generateStarZoneName());
			newZone.setDifficulty(depth+(Util.chance(30)?1:0));
			zone.addPath(newZone, Util.rand(2, 5));
			addNodes(newZone, depth+1);
		}
	}
	private final static String[] first = new String[]{"A", "Ma", "Me", "Bi", "So", "Rei", "Ba", "Ke", "Ho", "Gu", "Ara", "Ame", "Xo"};
	private final static String[] second = new String[]{"ra", "qua", "ki", "je", "lo", "ru", "pa", "ri", "po", "ri"};
	private final static String[] last = new String[]{"rius", "mix", "gres", "hon", "fun", "des", "pis", "gus", "mos", "mas", "us", "uri"};
	
	private Map<String, String> usedNames = new HashMap<String, String>();
	private String generateStarZoneName() {
		//return Util.rand(0, 255)+"A";
		
		int tries = 0;
		while (true){
			String ret =  Util.randomElementOf(first)+Util.randomElementOf(second)+Util.randomElementOf(last);
			if (usedNames.get(ret) != null){
				if (tries < 1000)
					continue;
			}
			usedNames.put(ret, "*");
			return ret;
		}
	}

}
