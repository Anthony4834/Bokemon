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
				screen.selected.updateLocations(SELECTED.POSITION.LEFT);
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
			case FAINT_ENEMY:
			case RUN_AWAY:
				updateDialog(BATTLE_STATE.END);
				break;
			default:
				selected.updateLocations(SELECTED.POSITION.RIGHT);
				updateDialog(BATTLE_STATE.QUESTION);
			}
		}
	} //end update function
	
	public void updateDialog(BATTLE_STATE to) {
		switch(to) {
			case QUESTION:
				screen.selected = SELECTED.OPTION_1;
				setDialog("What should \n" + screen.activePokemon.getName().toUpperCase() + " do?");
				screen.state = BATTLE_STATE.QUESTION;
				break;
			case QUESTION_ATTACK:
				screen.selected = SELECTED.OPTION_1;
				setDialog("");
				screen.state = BATTLE_STATE.QUESTION_ATTACK;
				break;
			case ATTACK:
				setDialog(screen.activePokemon.getName().toUpperCase() + " used \n" + screen.activePokemon.getMoveSet().get(screen.selected.getNum() - 1).getName());
				screen.state = BATTLE_STATE.ATTACK;
				screen.attackPokemon();
				break;
			case ATTACK_CRITICAL:
				setDialog("A critical hit!");
				screen.state = BATTLE_STATE.ATTACK_CRITICAL;
				break;
			case ATTACK_SUPER_EFFECTIVE:
				setDialog("It's super effective!");
				screen.state = BATTLE_STATE.ATTACK_SUPER_EFFECTIVE;
				break;
			case ATTACK_NOT_VERY_EFFECTIVE:
				setDialog("It's not very effective..");
				screen.state = BATTLE_STATE.ATTACK_NOT_VERY_EFFECTIVE;
				break;
			case ATTACK_NO_EFFECT:
				setDialog("It had no effect..");
				screen.state = BATTLE_STATE.ATTACK_NO_EFFECT;
				break;
			case COME_BACK:
				setDialog("Come back " + screen.activePokemon.getName().toUpperCase() + "!");
				screen.state = BATTLE_STATE.COME_BACK;
				break;
			case GO:
				setDialog("Go, " + screen.activePokemon.getName().toUpperCase() + "!");
				screen.state = BATTLE_STATE.GO;
				break;
			case CAPTURE:
				setDialog("Player throws a pokeball!");
				screen.state = BATTLE_STATE.CAPTURE;
				break;
			case CAPTURE_SUCCESS:
				setDialog(screen.enemy.getName().toUpperCase() + " was caught!");
				screen.state = BATTLE_STATE.CAPTURE_SUCCESS;
				break;
			case RUN_AWAY:
				setDialog("Got away safely!");
				screen.state = BATTLE_STATE.RUN_AWAY;
				break;
			case FAINT_ENEMY:
				setDialog("Enemy " + screen.enemy.getName().toUpperCase() + " has fainted!");
				screen.state = BATTLE_STATE.FAINT_ENEMY;
				break;
			case END:
				screen.state = BATTLE_STATE.END;
				screen.endBattle();
				break;
			default:
				setDialog("A wild \n" + screen.enemy.getName().toUpperCase() + " attacks!");
		}
	}
	public void setDialog(String str) {
		screen.currentDialog = "";
		screen.targetDialog = str.toCharArray();
		screen.targetDialogSpaced = getSpaced(screen.targetDialog);
		screen.textChanging = true;
	}
	public char[] getSpaced(char[] arr) {
		String output = "";
		for(char c : arr) {
			output = output + " " + c;
		}
		return output.toCharArray();
	}
}
