package com.bokemon.model.world;
public class TileMap {
	
	private int width, height;
	private Tile[][] tiles;
	
	public TileMap(int w, int h) {
		this.width = w;
		this.height = h;
		
		tiles = new Tile[width][height];
		for(int x=0; x < w; x++) {
			for(int y=0; y < h; y++) {
				if(Math.random() > 0.5d) {
					tiles[x][y] = new Tile(TERRAIN.GRASS_1);
				} else {
					tiles[x][y] = new Tile(TERRAIN.GRASS_2);
				}
			}
		}
	}
	
	public Tile getTile(int x, int y) {
		return tiles[x][y];
	}
	
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
}
