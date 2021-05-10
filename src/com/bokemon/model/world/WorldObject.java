package com.bokemon.model.world;

import com.badlogic.gdx.graphics.Texture;
import com.bokemon.Settings;

public abstract class WorldObject {
	private float x;
	private float y;
	private float sizeX;
	private float sizeY;
	private float xyScale;
	private Texture texture;
	@SuppressWarnings("unused")
	private TileMap map;
	
	public WorldObject(TileMap map, float x, float y, float sizeX, float sizeY, float scale, Texture texture) {
		this.x = x;
		this.y = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.texture = texture;
		this.map = map;
		this.xyScale = scale;
		
		for(int i=0; i < sizeX / Settings.SCALED_TILE_SIZE; i++ ) {
			map.getTile((int) (x / Settings.SCALED_TILE_SIZE + i), (int) (y / Settings.SCALED_TILE_SIZE)).setObject(this);
			map.getTile((int) (x / Settings.SCALED_TILE_SIZE + i), (int) ((y + sizeY / this.xyScale) / Settings.SCALED_TILE_SIZE)).setObject(this);
		}
		for(int i=0; i < sizeY / this.xyScale / Settings.SCALED_TILE_SIZE; i++ ) {
			map.getTile((int) (x / Settings.SCALED_TILE_SIZE), (int) (y / Settings.SCALED_TILE_SIZE + i)).setObject(this);
			map.getTile((int) ((x + sizeX - Settings.SCALED_TILE_SIZE) / Settings.SCALED_TILE_SIZE), (int) (y / Settings.SCALED_TILE_SIZE + i)).setObject(this);
		}
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getSizeX() {
		return sizeX;
	}

	public float getSizeY() {
		return sizeY;
	}

	public Texture getTexture() {
		return texture;
	}
}
