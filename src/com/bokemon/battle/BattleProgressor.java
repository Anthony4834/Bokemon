package com.bokemon.battle;

import com.bokemon.screen.BattleScreen;

public class BattleProgressor {
	private BattleScreen screen;
	
	public BattleProgressor(BattleScreen s) {
		this.screen = s;
	}
	
	public void update(SELECTED selected) {
		if(screen.state == BATTLE_STATE.QUESTION) {
			switch(selected) {
			case OPTION_1:
				screen.selected.updateLocations();
				updateDialog(BATTLE_STATE.QUESTION_ATTACK);
				break;
			case OPTION_2:
				screen.capturePokemon();
				break;
			case OPTION_3:
				screen.switchPokemon();
				break;
			case OPTION_4:
				updateDialog(BATTLE_STATE.RUN_AWAY);
				break;
			default:
				screen.selected = SELECTED.OPTION_1;
			}
		} else if(screen.state == BATTLE_STATE.QUESTION_ATTACK) {
			screen.attackPokemon();
		} else {
			switch(screen.state) {
			case INIT:
				updateDialog(BATTLE_STATE.GO);
				break;
			case GO:
				updateDialog(BATTLE_STATE.QUESTION);
				break;
			case COME_BACK:
				screen.switchPokemon();
				break;
			case ATTACK:
				screen.attackPokemon();
				break;
			case CAPTURE:
				screen.capturePokemon();
				break;
			case CAPTURE_SUCCESS:
				updateDialog(BATTLE_STATE.END);
				break;
			case FAINT_ENEMY:
				updateDialog(BATTLE_STATE.END);
			default:
				updateDialog(BATTLE_STATE.END);
			}
		}
	} //end update function
	
	public void updateDialog(BATTLE_STATE to) {
		switch(to) {
			case QUESTION:
				screen.selected = SELECTED.OPTION_1;
				screen.currentDialog = "What should \n" + screen.activePokemon.getName().toUpperCase() + " do?";
				screen.state = BATTLE_STATE.QUESTION;
				break;
			case QUESTION_ATTACK:
				screen.selected = SELECTED.OPTION_1;
				screen.currentDialog = "";
				screen.state = BATTLE_STATE.QUESTION_ATTACK;
				break;
			case ATTACK:
				screen.currentDialog = screen.activePokemon.getName().toUpperCase() + " used \n" + screen.activePokemon.getMoveSet().get(screen.selected.getNum() - 1).getName();
				screen.state = BATTLE_STATE.ATTACK;
				break;
			case COME_BACK:
				screen.currentDialog = "Come back " + screen.activePokemon.getName().toUpperCase() + "!";
				screen.state = BATTLE_STATE.COME_BACK;
				break;
			case GO:
				screen.currentDialog = "Go, " + screen.activePokemon.getName().toUpperCase() + "!";
				screen.state = BATTLE_STATE.GO;
				break;
			case CAPTURE:
				screen.currentDialog = "Player throws a pokeball!";
				screen.state = BATTLE_STATE.CAPTURE;
				break;
			case CAPTURE_SUCCESS:
				screen.currentDialog = screen.enemy.getName().toUpperCase() + " was caught!";
				screen.state = BATTLE_STATE.CAPTURE_SUCCESS;
				break;
			case RUN_AWAY:
				screen.currentDialog = "Got away safely!";
				screen.state = BATTLE_STATE.RUN_AWAY;
				break;
			case FAINT_ENEMY:
				screen.currentDialog = "Enemy " + screen.enemy.getName().toUpperCase() + " has fainted!";
				screen.state = BATTLE_STATE.FAINT_ENEMY;
				break;
			case END:
				screen.state = BATTLE_STATE.END;
				screen.endBattle();
				break;
			default:
				screen.currentDialog = "A wild \n" + screen.enemy.getName().toUpperCase() + " attacks!";
		}
	}
}
