package com.bokemon.model.pokemon.move;

public enum EFFECT {
	PARALYSIS(0, 0),
	POISON_1(.0265, 0),
	POISON_2(.0265, 0),
	SLEEP(0, 5),
	BURNED(.125, 0),
	FROZEN(0, 5),
	FLINCH(0, 1),
	CONFUSED(0, 5),
	INFATUATED(0, 5),
	LEECH(.0265, 0),
	SCARE(0, 0),
	CHARGE(0, 1),
	;
	
	private int duration;
	private double damage;
	
	EFFECT(double dmg, int duration) {
		this.damage = dmg;
		
		switch(duration) {
			case 1:
				this.duration = 1;
			default:
				this.duration = (int) (Math.random() * (5 - 3) + 3);
		}
	}

	public int getDuration() {
		return duration;
	}

	public double getDamage() {
		return damage;
	}
}
