package com.bokemon.battle;

public enum BATTLE_STATE {
	INIT,
	QUESTION,
	QUESTION_ATTACK, //Attack menu open
	ATTACK,
	COME_BACK,
	GO,
	CAPTURE,
	CAPTURE_SUCCESS,
	RUN_AWAY,
	FAINT_ENEMY,
	END,
	;
	
	private BATTLE_STATE() {
		
	}
}
