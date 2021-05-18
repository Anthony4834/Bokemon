package com.bokemon.battle;

import com.badlogic.gdx.Gdx;
import com.bokemon.Bokemon;
import com.bokemon.controller.BattleController;
import com.bokemon.model.pokemon.Capture_Calculator;
import com.bokemon.model.pokemon.Pokemon;
import com.bokemon.screen.BattleScreen;

public class BattleEvent {
	private static int switchCounter = 0;
	private static int captureCounter = 0;
	
	private BattleProgressor progressor;
	private BattleScreen screen;
	@SuppressWarnings("unused")
	private BattleController controller;
	private String dialog;
	private EVENT_TYPE type;
	private boolean finished = true;
	public static BattleEvent current;
	
	public BattleEvent(BattleScreen s, String dialog, EVENT_TYPE type) {
		this.screen = s;
		this.dialog = dialog;
		this.type = type;
		this.controller = screen.controller;
		this.progressor = screen.progressor;
	}
	public void init() {
		if(dialog != null) {
			screen.progressor.setDialog(dialog);
		}
		switch(this.type) {
		case HEALTH_ANIM_ENEMY:
			this.enemyHealthAnim();
			break;
		case CHANGE_STATE:
			this.changeBattleState(screen.state);
			break;
		case USE_ITEM:
			screen.state = BATTLE_STATE.CAPTURE;
			this.capturePokemon();
			break;
		case SWITCH_POKEMON:
			screen.state = BATTLE_STATE.COME_BACK;
			if(switchCounter == 0) {
				screen.queue.add(new BattleEvent(screen, null, EVENT_TYPE.SWITCH_POKEMON));
			}
			this.switchPokemon();
			break;
		case RUN_AWAY:
			screen.state = BATTLE_STATE.END;
			screen.queue.add(new BattleEvent(screen, null, EVENT_TYPE.CHANGE_STATE));
			break;
		default:
			break;
		}
	}
	private void changeBattleState(BATTLE_STATE state) {
		switch(state) {
			case QUESTION:
				SELECTED.updateLocations(SELECTED.POSITION.LEFT);
				screen.selected = SELECTED.OPTION_1;
				screen.progressor.setDialog("");
				screen.state = BATTLE_STATE.QUESTION_ATTACK;
				break;
			case QUESTION_ATTACK:
				screen.state = BATTLE_STATE.ATTACK;
				progressor.attackPokemon();
				break;
			case END:
				screen.endBattle();
				break;
			default:
				reset();
				break;
		}
	}
	public enum EVENT_TYPE {
		HEALTH_ANIM_ENEMY,
		HEALTH_ANIM_FRIENDLY,
		CHANGE_STATE,
		SWITCH_POKEMON,
		RUN_AWAY,
		USE_ITEM,
		DIALOG
	}
	
	private void enemyHealthAnim() {
		float hpBarDelta = Gdx.graphics.getDeltaTime();
		float speed = 100;
		float dSizeX;
		this.finished = false;
		current  = this;
		
		if(screen.enemyHealthBar.getSizeX() < HealthBar.max * (screen.enemy.getHpPercentage() / 100)) {
			dSizeX = screen.enemyHealthBar.getSizeX() + hpBarDelta*speed;
			if(dSizeX >= HealthBar.max * (screen.enemy.getHpPercentage() / 100)) {
				screen.enemyHealthBar.setSizeX((float) (HealthBar.max * (screen.enemy.getHpPercentage() / 100)) );
				this.screen.enemyHpChange = false;
				this.finished = true;
				screen.queue.remove();
				current = null;
				if(!screen.queue.isEmpty()) {
					screen.queue.remove().init();
				} else {
					new BattleEvent(screen, null, EVENT_TYPE.CHANGE_STATE).init();
				}
			} else {
				screen.enemyHealthBar.setSizeX(dSizeX);
			}
		} else {
			dSizeX = screen.enemyHealthBar.getSizeX() - hpBarDelta*speed;
			if(dSizeX <= HealthBar.max * (screen.enemy.getHpPercentage() / 100) || dSizeX < 0) {
				screen.enemyHealthBar.setSizeX((float) (HealthBar.max * (screen.enemy.getHpPercentage() / 100)) );
				this.screen.enemyHpChange = false;
				this.finished = true;
				screen.queue.remove();
				if(screen.enemy.getHp() == 0) {
					screen.queue.add(new BattleEvent(screen, screen.enemy.getName() + " fainted!", EVENT_TYPE.DIALOG));
					screen.state = BATTLE_STATE.END;
				}
				current = null;
				if(!screen.queue.isEmpty()) {
					screen.queue.remove().init();
				} else {
					new BattleEvent(screen, null, EVENT_TYPE.CHANGE_STATE).init();
				}
			} else {
				screen.enemyHealthBar.setSizeX(dSizeX);
			}
		}
		screen.enemyHealthBar.colorCheck();
	}
	
	public void switchPokemon() {
		if(switchCounter == 0) {
			switchPokemon_1();
		} else {
			switchPokemon_2();
		}
	}
	private void switchPokemon_1() {
		new BattleEvent(screen, "Come back " + screen.activePokemon.getName() + "!", EVENT_TYPE.DIALOG).init();
		
		switchCounter += 1;
	}
	private void switchPokemon_2() {
		Pokemon to;
		if(screen.activePokemonNum + 1 == screen.party.size()) {
			to = screen.party.get(0);
			screen.update(to);
			screen.activePokemonNum = 0;
		} else {
			to = screen.party.get(screen.activePokemonNum + 1);
			screen.update(to);
			screen.activePokemonNum += 1;
		}
		new BattleEvent(screen, "Go, " + to.getName() + "!", EVENT_TYPE.DIALOG).init();
		
		switchCounter = 0;
	}
	public void capturePokemon() {
		if(captureCounter == 0) {
			capturePokemon_1();
		}
	}
	private void capturePokemon_1() {
		Boolean success = false;
		int a = Capture_Calculator.genChances(screen.enemy);
		if(Capture_Calculator.attemptCapture(a)) {
			calculateShakes(true, a);
			success = true;
			System.out.println("CAUGHT");
		} else {
			calculateShakes(false, a);
			System.out.println(String.format("FAILED - [%s] shakes", screen.pokeballShakes));
		}
		
		if(success) {
			screen.queue.add(new BattleEvent(screen, "Success! " + screen.enemy.getName() + " was caught!", EVENT_TYPE.DIALOG));
			if(screen.party.size() < 6) {
				Bokemon.prefs.putString(String.format("poke%s", screen.party.size() + 1), screen.enemy.getName().toUpperCase());
				Bokemon.prefs.putInteger(String.format("poke%s_lv", screen.party.size() + 1), screen.enemy.getLevel());
				Bokemon.prefs.putInteger(String.format("poke%s_hp", screen.party.size() + 1), screen.enemy.getHp());
				Bokemon.prefs.flush();
			}
			screen.state = BATTLE_STATE.END;
			screen.queue.add(new BattleEvent(screen, null, EVENT_TYPE.CHANGE_STATE));
		} else {
			screen.queue.add(new BattleEvent(screen, "Oh no! The POKEMON Broke free!", EVENT_TYPE.DIALOG));
		}
	}
	public void calculateShakes(Boolean caught, int a) {
		int output = 0;
		if(caught) {
			output += 3;
		} else {
			for(int i = 0; i < 3; i++) {
				if(!Capture_Calculator.shake(a)) {
					output++;
				}
			}
		}
		
		screen.pokeballShakes = output;
	}
	private void reset() {
		screen.progressor.setDialog("What should \n" + screen.activePokemon.getName() + " do?");
		screen.selected = SELECTED.OPTION_1;
		screen.state = BATTLE_STATE.QUESTION;
	}
	public Boolean isFinished() {
		return this.finished;
	}
	public EVENT_TYPE getType() {
		return type;
	}
}
