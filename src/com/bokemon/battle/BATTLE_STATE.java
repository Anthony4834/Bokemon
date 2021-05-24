package com.bokemon.battle;

public enum BATTLE_STATE {
	INIT(null),
	QUESTION(null),
	QUESTION_ATTACK(QUESTION), //Attack menu open
	ATTACK(null), //Attack
	ATTACK_ENEMY(null),
	POKEMON_MENU_OPEN(QUESTION),
	COME_BACK(null),
	GO(null),
	CAPTURE(null),
	CAPTURE_SUCCESS(null),
	RUN_AWAY(null),
	FAINT_ENEMY(null),
	END(null),
	;
	
	private BATTLE_STATE prev;
	
	private BATTLE_STATE(BATTLE_STATE prev) {
		this.prev = prev;
	}
	
	public BATTLE_STATE getPrevious() { 
		return this.prev;
	}
}
