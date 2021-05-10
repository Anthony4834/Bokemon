package com.bokemon.controller;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.bokemon.battle.BATTLE_STATE;
import com.bokemon.battle.SELECTED;
import com.bokemon.screen.BattleScreen;

public class BattleController extends InputAdapter {
	
	private BattleScreen screen;
	
	public BattleController(BattleScreen screen) {
		this.screen = screen;
	}
	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.D) {
			switch(screen.selected) {
				case ATTACK:
					screen.selected = SELECTED.BAG;
					break;
				case BAG:
					screen.selected = SELECTED.SWITCH;
					break;
				case SWITCH:
					screen.selected = SELECTED.RUN;
					break;
				case RUN:
					break;
				default:
					screen.selected = SELECTED.ATTACK;
			}
		}
		if(keycode == Keys.A) {		//RUN
			switch(screen.selected) {
				case ATTACK:
					break;
				case RUN:
					screen.selected = SELECTED.SWITCH;
					break;
				case SWITCH:
					screen.selected = SELECTED.BAG;
					break;
				case BAG:
					screen.selected = SELECTED.ATTACK;
					break;
				default:
					screen.selected = SELECTED.ATTACK;
			}
		}
		if(keycode == Keys.S || keycode == Keys.W) {		//RUN
			switch(screen.selected) {
				case ATTACK:
					screen.selected = SELECTED.SWITCH;
					break;
				case SWITCH:
					screen.selected = SELECTED.ATTACK;
					break;
				case BAG:
					screen.selected = SELECTED.RUN;
					break;
				case RUN:
					screen.selected = SELECTED.BAG;
					break;
				default:
					screen.selected = SELECTED.ATTACK;
			}
		}
		if(keycode == Keys.SPACE) {
			if(screen.enemyHpChange || screen.hpChange) {
				return false;
			}
			if(screen.state == BATTLE_STATE.QUESTION) {
				switch(screen.selected) {
					case ATTACK:
						screen.attackPokemon();
						break;
					case RUN:
						screen.updateDialog(BATTLE_STATE.RUN_AWAY);
						break;
					case SWITCH:
						screen.switchPokemon();
						break;
					case BAG:
						screen.capturePokemon();
						break;
					default:
						screen.selected = SELECTED.ATTACK;
				}
			} else {
				switch(screen.state) {
					case INIT:
						screen.updateDialog(BATTLE_STATE.GO);
						break;
					case GO:
						screen.updateDialog(BATTLE_STATE.QUESTION);
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
						screen.updateDialog(BATTLE_STATE.END);
						break;
					case FAINT_ENEMY:
						screen.updateDialog(BATTLE_STATE.END);
					default:
						screen.updateDialog(BATTLE_STATE.END);
				}
			}
		}
		return false;
	}
}
