package com.bokemon.controller;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.bokemon.battle.BATTLE_STATE;
import com.bokemon.battle.BattleEvent;
import com.bokemon.battle.BattleEvent.EVENT_TYPE;
import com.bokemon.screen.BattleScreen;
import com.bokemon.util.Jukebox;

public class BattleController extends InputAdapter {
	
	private BattleScreen screen;
	private Boolean lockedPartial = false;
	private Boolean lockedFull = false;
	
	public BattleController(BattleScreen screen) {
		this.screen = screen;
	}
	@Override
	public boolean keyDown(int keycode) {
		System.out.println(screen.state);
		if(screen.state != BATTLE_STATE.QUESTION && screen.state != BATTLE_STATE.QUESTION && (lockedFull || screen.enemyHpChange || screen.hpChange || screen.textChanging)) {
			return false;
		}
		if(keycode == Keys.SPACE) {
			Jukebox.playSound("nav");
			if(!screen.queue.isEmpty()) { //CHECK QUEUE
				screen.queue.remove().init();
				return false;
			}
			if(screen.state == BATTLE_STATE.QUESTION) { //CHECK STATE
				switch(screen.selected) {
				case OPTION_1:
					new BattleEvent(screen, "", EVENT_TYPE.CHANGE_STATE).init();
					break;
				case OPTION_2:
					new BattleEvent(screen, "Player throws a POKEBALL", EVENT_TYPE.USE_ITEM).init();
					break;
				case OPTION_3:
					new BattleEvent(screen, null, EVENT_TYPE.SWITCH_POKEMON).init();
					break;
				case OPTION_4:
					new BattleEvent(screen, "Got away safely!", EVENT_TYPE.RUN_AWAY).init();
					break;
				default:
					break;
				}
				return false;
			}
			new BattleEvent(screen, "", EVENT_TYPE.CHANGE_STATE).init(); //PROGRESS BATTLE
			return false;
		}
		if(keycode == Keys.X && screen.state.getPrevious() != null) {
			new BattleEvent(screen, null, EVENT_TYPE.CANCEL).init();
		}
		if(!lockedPartial) {
			if(keycode == Keys.D && screen.selected.getRight() != null && (screen.state == BATTLE_STATE.QUESTION || screen.selected.getRight().getNum() <= screen.activePokemon.getMoveSet().size()) ) {
				Jukebox.playSound("nav");
				screen.selected = screen.selected.getRight();
				return false;
			}
			if(keycode == Keys.A && screen.selected.getLeft() != null && (screen.state == BATTLE_STATE.QUESTION || screen.selected.getLeft().getNum() <= screen.activePokemon.getMoveSet().size()) ) {
				Jukebox.playSound("nav");
				screen.selected = screen.selected.getLeft();
				return false;
			}
			if(keycode == Keys.S && (screen.state == BATTLE_STATE.QUESTION || screen.selected.getDown().getNum() <= screen.activePokemon.getMoveSet().size()) ) {
				if(screen.selected.getDown() != null) {
					Jukebox.playSound("nav");
					screen.selected = screen.selected.getDown();
				}
				return false;
			}
			if(keycode == Keys.W && (screen.state == BATTLE_STATE.QUESTION || screen.selected.getUp().getNum() <= screen.activePokemon.getMoveSet().size()) ) {
				if(screen.selected.getUp() != null) {
					Jukebox.playSound("nav");
					screen.selected = screen.selected.getUp();
				}
				return false;
			}
			if(keycode == Keys.L) {
				screen.state = BATTLE_STATE.ATTACK;
				screen.progressor.decideOrder(screen.activePokemon, screen.enemy);
				return false;
			}
		}
		return false;
	}
	
	public void lockPartial() {
		this.lockedPartial = true;
	}
	public void lockFull() {
		this.lockedFull = true;
	}
	public void unlockPartial() {
		this.lockedPartial = false;
	}
	public void unlockFull() {
		this.lockedFull = false;
	}
	public void unlock() {
		this.lockedFull = false;
		this.lockedPartial = false;
	}
 }
