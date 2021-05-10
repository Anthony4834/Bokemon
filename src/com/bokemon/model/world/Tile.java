package com.bokemon.model.world;

import com.bokemon.model.Actor;

public class Tile {

	private TERRAIN terrain;
	
	private Actor actor;
	private WorldObject object;
	
	public Tile(TERRAIN terrain) {
		this.terrain = terrain;
	}
	
	public TERRAIN getTerrain() {
		return terrain;
	}
	public void setTerrain(TERRAIN t) {
		this.terrain = t;
	}

	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

	public WorldObject getObject() {
		return object;
	}

	public void setObject(WorldObject object) {
		this.object = object;
	}
	
	
}
