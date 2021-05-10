package com.bokemon.battle;

import com.badlogic.gdx.graphics.Texture;

public abstract class BattleScreenObject {
	private float x;
	private float y;
	private float sizeX;
	private float sizeY;
	private Texture texture;
	
	public BattleScreenObject(float x, float y, float sizeX, float sizeY, float scale, Texture texture) {
		this.x = x;
		this.y = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.texture = texture;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setSizeX(float sizeX) {
		this.sizeX = sizeX;
	}

	public void setSizeY(float sizeY) {
		this.sizeY = sizeY;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
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
