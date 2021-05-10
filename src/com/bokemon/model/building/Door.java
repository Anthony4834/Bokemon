package com.bokemon.model.building;

import com.badlogic.gdx.graphics.Texture;
import com.bokemon.model.world.TileMap;
import com.bokemon.model.world.WorldObject;

public class Door extends WorldObject {
	
	private House parentStructure;
	
	public Door(
			House parentStructure,
			float scale,
			float worldX,
			float worldY, 
			float sizeX,
			float sizeY,
			Texture texture,
			TileMap map ) {
		
		super(map, worldX, worldY, sizeX, sizeY, scale, texture);
	}
}
