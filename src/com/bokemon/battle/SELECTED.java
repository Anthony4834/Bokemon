package com.bokemon.battle;

public enum SELECTED {
	ATTACK(22.5, 4.5),
	BAG(31.5, 4.5),
	SWITCH(22.5, 2.5),
	RUN(31.5, 2.5),
	;
	
	private float x, y;

	private SELECTED(double x, double y) {
		this.x = (float) x;
		this.y = (float) y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
}
