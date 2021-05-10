package com.bokemon.battle;

public enum BATTLE_STATE {
	INIT,
	QUESTION,
	QUESTION_ATTACK,
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
