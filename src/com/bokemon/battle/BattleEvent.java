package com.bokemon.battle;

import com.badlogic.gdx.Gdx;
import com.bokemon.controller.BattleController;
import com.bokemon.model.pokemon.Pokemon;
import com.bokemon.screen.BattleScreen;
import com.bokemon.util.Jukebox;

public class BattleEvent {
	public static int switchCounter = 0;
	public static int captureCounter = 0;
	
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
			this.healthAnim(false);
			break;
		case HEALTH_ANIM_ALLY:
			this.healthAnim(true);
			break;
		case CHANGE_STATE:
			this.changeBattleState(screen.state);
			break;
		case USE_ITEM:
			screen.state = BATTLE_STATE.CAPTURE;
			progressor.capturePokemon();
			break;
		case SWITCH_POKEMON:
			screen.state = BATTLE_STATE.COME_BACK;
			if(switchCounter == 0) {
				screen.queue.add(new BattleEvent(screen, null, EVENT_TYPE.SWITCH_POKEMON));
			}
			progressor.switchPokemon();
			break;
		case RUN_AWAY:
			screen.state = BATTLE_STATE.END;
			screen.queue.add(new BattleEvent(screen, null, EVENT_TYPE.CHANGE_STATE));
			break;
		case DELAY_HIT:
			this.delayHit();
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
				progressor.attackPokemon(screen.activePokemon, screen.enemy);
				break;
			case ENEMY_ATTACK:
				screen.state = BATTLE_STATE.QUESTION;
				System.out.println("UCK");
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
		HEALTH_ANIM_ALLY,
		CHANGE_STATE,
		SWITCH_POKEMON,
		RUN_AWAY,
		USE_ITEM,
		DIALOG,
		DELAY_HIT
	}
	
	private void delayHit() {
		current = this;
		this.finished = false;
		BattleEvent next = screen.queue.peek();
		if(screen.battleTimer < 5) {
			screen.toggleTimer(true);
		}
		
		if(screen.battleTimer > 100) {
			if(next != null) {
				if(screen.battleTimer == 105) {
					Jukebox.playSound(screen.queuedSound, 0.03f);
				}
				screen.hitEffect = true;
				if(screen.battleTimer > 110) {
					screen.hitEffect = false;
					screen.toggleTimer(false);
					screen.queue.remove();
					next.init();
				}
			}
		}
	}
	
	private void hitAnim() {
		screen.toggleTimer(true);
		
		
	}
	
	private void healthAnim(Boolean ally) {
		Pokemon target = ally ? screen.activePokemon : screen.enemy;
		HealthBar hB = ally ? screen.allyHealthBar : screen.enemyHealthBar;
		
		float hpBarDelta = Gdx.graphics.getDeltaTime();
		float speed = 100;
		float dSizeX;
		float dHp;
		
		this.finished = false;
		current = this;
		
		if(hB.getSizeX() < HealthBar.max * (target.getHpPercentage() / 100)) {
			dSizeX = hB.getSizeX() + hpBarDelta*speed;
			dHp = screen.displayHp + hpBarDelta * 21;
			if(dSizeX >= HealthBar.max * (target.getHpPercentage() / 100)) {
				hB.setSizeX((float) (HealthBar.max * (target.getHpPercentage() / 100)) );
				if(ally) {
					screen.displayHp = target.getHp();
					this.screen.hpChange = false;
				} else {
					this.screen.enemyHpChange = false;
				}
				this.finished = true;
				screen.queue.remove();
				current = null;
				if(!screen.queue.isEmpty()) {
					screen.queue.remove().init();
				} else {
					new BattleEvent(screen, null, EVENT_TYPE.CHANGE_STATE).init();
				}
			} else {
				if(ally) {
					screen.displayHp = dHp > target.getHp() ? target.getHp() : dHp;
				}
				hB.setSizeX(dSizeX);
			}
		} else {
			dSizeX = hB.getSizeX() - hpBarDelta*speed;
			dHp = screen.displayHp - hpBarDelta * 21;
			if(dSizeX <= HealthBar.max * (target.getHpPercentage() / 100) || dSizeX < 0) {
				if(ally) {
					screen.displayHp = target.getHp();
				}
				hB.setSizeX((float) (HealthBar.max * (target.getHpPercentage() / 100)) );
				if(ally) {
					this.screen.hpChange = false;
				} else {
					this.screen.enemyHpChange = false;
				}
				this.finished = true;
				screen.queue.remove();
				if(target.getHp() == 0) {
					screen.queue.add(new BattleEvent(screen, target.getName() + " fainted!", EVENT_TYPE.DIALOG));
					screen.state = BATTLE_STATE.END;
				}
				current = null;
				if(!screen.queue.isEmpty()) {
					screen.queue.remove().init();
				} else {
					new BattleEvent(screen, null, EVENT_TYPE.CHANGE_STATE).init();
				}
			} else {
				if(ally) {
					screen.displayHp = dHp < target.getHp() ? target.getHp() : dHp;
				}
				hB.setSizeX(dSizeX);
			}
		}
		hB.colorCheck();
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
