package com.bokemon.model.pokemon;

public enum TYPE {
	BUG,
	DARK,
	DRAGON,
	ELECTRIC,
	FAIRY,
	FIGHTING,
	FIRE,
	FLYING,
	GHOST,
	GRASS,
	GROUND,
	ICE,
	NORMAL,
	POISON,
	PSYCHIC,
	ROCK,
	STEEL,
	WATER,
	;
	
	private TYPE[] weakTo, resistentTo, strongAgainst, weakAgainst;
	
	private TYPE() {
		
	}
	
}
