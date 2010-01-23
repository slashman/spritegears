package net.slashware.spriteGears.entities;

import java.awt.Image;
import java.io.IOException;

import net.slashware.util.ImageUtils;

public enum StarSection {
	VOID,
	ENEMY,
	ENERGY,
	BIG_ENERGY,
	MOTHERSHIP,
	TERRAN_SPACE_STATION, SHADOW_ENERGY;
	
	private static Image VOID_IMAGE, ENEMY_IMAGE, ENERGY_IMAGE, BIGENERGY_IMAGE, MOTHERSHIP_IMAGE, TERRAN_SPACE_STATION_IMAGE, SHADOW_ENERGY_IMAGE;
	private static Image VOID_IMAGE_HOVER, ENEMY_IMAGE_HOVER, ENERGY_IMAGE_HOVER, BIGENERGY_IMAGE_HOVER, MOTHERSHIP_IMAGE_HOVER, TERRAN_SPACE_STATION_IMAGE_HOVER, SHADOW_ENERGY_IMAGE_HOVER;
	static {
		try {
			VOID_IMAGE = ImageUtils.createImage("img/icons2x.gif", 0, 34, 32, 32);
			ENEMY_IMAGE = ImageUtils.createImage("img/icons2x.gif", 0, 0, 32, 32);
			ENERGY_IMAGE = ImageUtils.createImage("img/icons2x.gif", 34, 34, 32, 32);
			BIGENERGY_IMAGE = ImageUtils.createImage("img/icons2x.gif", 68, 0, 32, 32);
			MOTHERSHIP_IMAGE = ImageUtils.createImage("img/icons2x.gif", 68, 34, 32, 32);
			TERRAN_SPACE_STATION_IMAGE = ImageUtils.createImage("img/icons2x.gif", 68, 102, 32, 32);
			SHADOW_ENERGY_IMAGE = ImageUtils.createImage("img/icons2x.gif", 340, 0, 32, 32);
			
			VOID_IMAGE_HOVER = ImageUtils.createImage("img/icons2x.gif", 102, 34, 32, 32);
			ENEMY_IMAGE_HOVER = ImageUtils.createImage("img/icons2x.gif", 102, 0, 32, 32);
			ENERGY_IMAGE_HOVER = ImageUtils.createImage("img/icons2x.gif", 136, 34, 32, 32);
			BIGENERGY_IMAGE_HOVER = ImageUtils.createImage("img/icons2x.gif", 170, 0, 32, 32);
			MOTHERSHIP_IMAGE_HOVER = ImageUtils.createImage("img/icons2x.gif", 170, 34, 32, 32);
			TERRAN_SPACE_STATION_IMAGE_HOVER = ImageUtils.createImage("img/icons2x.gif", 170, 34, 32, 32);
			SHADOW_ENERGY_IMAGE_HOVER = ImageUtils.createImage("img/icons2x.gif", 340, 67, 32, 32);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Image getImage(){
		switch (this){
			case ENEMY:
				return ENEMY_IMAGE;
			case ENERGY:
				return ENERGY_IMAGE;
			case VOID:
				return VOID_IMAGE;
			case BIG_ENERGY:
				return BIGENERGY_IMAGE;
			case MOTHERSHIP:
				return MOTHERSHIP_IMAGE;
			case TERRAN_SPACE_STATION:
				return TERRAN_SPACE_STATION_IMAGE;
			case SHADOW_ENERGY:
				return SHADOW_ENERGY_IMAGE;
		}
		return null;
	}
	
	public String getDescription(){
		switch (this){
		case ENEMY:
			return "Enemy Ships";
		case ENERGY:
			return "Energy Field";
		case VOID:
			return "Navigable sector";
		case BIG_ENERGY:
			return "Massive Energy Field";
		case MOTHERSHIP:
			return "Mothership";
		case TERRAN_SPACE_STATION:
			return "Terran Space Station";
		case SHADOW_ENERGY:
			return "Shadow Energy";
		}
		return null;
	}


	public Image getHoverImage() {
		switch (this){
		case ENEMY:
			return ENEMY_IMAGE_HOVER;
		case ENERGY:
			return ENERGY_IMAGE_HOVER;
		case VOID:
			return VOID_IMAGE_HOVER;
		case BIG_ENERGY:
			return BIGENERGY_IMAGE_HOVER;
		case MOTHERSHIP:
			return MOTHERSHIP_IMAGE_HOVER;
		case TERRAN_SPACE_STATION:
			return TERRAN_SPACE_STATION_IMAGE_HOVER;
		case SHADOW_ENERGY:
			return SHADOW_ENERGY_IMAGE_HOVER;
	}
	return null;
	}
	
}
