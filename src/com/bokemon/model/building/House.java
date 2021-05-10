package com.bokemon.model.building;

import com.badlogic.gdx.graphics.Texture;
import com.bokemon.model.world.TileMap;
import com.bokemon.model.world.WorldObject;

public class House extends WorldObject {
	private float enterPointX;
	private float enterPointY;
	
	public House(
			float scale,
			float worldX,
			float worldY, 
			float sizeX,
			float sizeY,
			float enterPointX,
			float enterPointY,
			Texture texture,
			TileMap map ) {
		
		super(map, worldX, worldY, sizeX, sizeY, scale, texture);
		
		this.enterPointX = enterPointX;
		this.enterPointY = enterPointY;
	}
}
