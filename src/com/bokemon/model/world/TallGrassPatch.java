package com.bokemon.model.world;

public class TallGrassPatch {
	
	private TileMap map;
	
	private float worldX;
	private float worldY;
	private float sizeX;
	private float sizeY;
	
	
	public TallGrassPatch(float x, float y, float sizeX, float sizeY, TileMap map) {
		this.worldX = x;
		this.worldY = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.map = map;
		
		
		int count = 0; //x counter
		int count2 = 0; //y counter
		for(int i=0; i < sizeX * sizeY; i++ ) {
			if(i % sizeX == 0 && i > 0) {
				count = 0;
				count2 += 1;
			}
			map.getTile((int) (x + count), (int) (y + count2)).setTerrain(TERRAIN.TALL_GRASS);
			count += 1;
		}
	}

	public TileMap getMap() {
		return map;
	}


	public float getWorldX() {
		return worldX;
	}


	public float getWorldY() {
		return worldY;
	}


	public float getSizeX() {
		return sizeX;
	}


	public float getSizeY() {
		return sizeY;
	}
}
