package net.slashware.spriteGears.generators;

import net.slashware.spriteGears.entities.StarMap;
import net.slashware.spriteGears.entities.StarZone;

public class MockStarMapGenerator implements StarMapGenerator {
	public StarMap generateStarmap() {
		StarMap ret = new StarMap();
		StarZone orice = new StarZone();
		orice.setName("Orice");
		StarZone emanysa = new StarZone();
		emanysa.setName("Emanysa");
		StarZone ckyto = new StarZone();
		ckyto.setName("Ckyto");
		
		orice.addPath(emanysa, 10);
		orice.addPath(ckyto, 5);
		emanysa.addPath(orice, 10);
		ckyto.addPath(orice, 5);
		
		ret.setInitialZone(orice);
		return ret;
	}
}
