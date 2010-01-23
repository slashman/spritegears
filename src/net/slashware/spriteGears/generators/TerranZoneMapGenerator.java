package net.slashware.spriteGears.generators;

import net.slashware.spriteGears.entities.SpaceStation;
import net.slashware.spriteGears.entities.StarZone;

public class TerranZoneMapGenerator extends SimpleZoneMapGenerator {
	@Override
	public boolean hasFriendlySpaceStation(StarZone zone) {
		return true;
	}
	
	
}