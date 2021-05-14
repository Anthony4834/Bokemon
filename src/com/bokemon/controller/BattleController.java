package com.bokemon.controller;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.bokemon.battle.BATTLE_STATE;
import com.bokemon.battle.SELECTED;
import com.bokemon.screen.BattleScreen;
import com.bokemon.util.Jukebox;

public class BattleController extends InputAdapter {
	
	private BattleScreen screen;
	
	public BattleController(BattleScreen screen) {
		this.screen = screen;
	}
	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.SPACE) {
			if(screen.enemyHpChange || screen.hpChange || screen.textChanging) {
				return false;
			}
			screen.progressBattle(screen.selected);
			return false;
		}
		if(keycode == Keys.D && screen.selected.getRight() != null && (screen.state == BATTLE_STATE.QUESTION || screen.selected.getRight().getNum() <= screen.activePokemon.getMoveSet().size()) ) {
			screen.selected = screen.selected.getRight();
			return false;
		}
		if(keycode == Keys.A && screen.selected.getLeft() != null && (screen.state == BATTLE_STATE.QUESTION || screen.selected.getLeft().getNum() <= screen.activePokemon.getMoveSet().size()) ) {
			screen.selected = screen.selected.getLeft();
			return false;
		}
		if(keycode == Keys.S && (screen.state == BATTLE_STATE.QUESTION || screen.selected.getDown().getNum() <= screen.activePokemon.getMoveSet().size()) ) {
			if(screen.selected.getDown() != null) {
				screen.selected = screen.selected.getDown();
			}
			return false;
		}
		if(keycode == Keys.W && (screen.state == BATTLE_STATE.QUESTION || screen.selected.getUp().getNum() <= screen.activePokemon.getMoveSet().size()) ) {
			if(screen.selected.getUp() != null) {
				screen.selected = screen.selected.getUp();
			}
			return false;
		}
		return false;
	}
}
