package com.bokemon.battle;

import com.badlogic.gdx.Gdx;
import com.bokemon.battle.BattleEvent.EVENT_TYPE;
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
	public static int TIMESTAMP = -1;
	
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
		case DELAY:
			this.delay();
			break;
		case DELAY_HIT_ENEMY:
			this.delayHit(false);
			break;
		case DELAY_HIT_ALLY:
			this.delayHit(true);
			break;
		case CANCEL:
			if(screen.state.getPrevious() == BATTLE_STATE.QUESTION) {
				goBack();
			}
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
				progressor.setDialog("");
				screen.state = BATTLE_STATE.QUESTION_ATTACK;
				break;
			case QUESTION_ATTACK:
				screen.state = BATTLE_STATE.ATTACK;
				progressor.decideOrder(screen.activePokemon, screen.enemy);
				break;
			case ATTACK_ENEMY:
				screen.state = BATTLE_STATE.QUESTION;
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
		DELAY,
		DELAY_HIT_ENEMY,
		DELAY_HIT_ALLY,
		CANCEL
	}
	private void delay() {
		if(TIMESTAMP == -1) {
			TIMESTAMP = screen.battleTimer;
		}
		
		current = this;
		
		if(screen.battleTimer > TIMESTAMP + 50) {
			TIMESTAMP = -1;
			current = null;
			screen.queue.remove();
			if(!progressor.awaitingAttack) {
				reset();
			}
		}
	}
	private void delayHit(Boolean ally) {
		if(TIMESTAMP == -1) {
			System.out.println("**********************************");
			screen.queue.add(new BattleEvent(screen, null, ally ? EVENT_TYPE.HEALTH_ANIM_ALLY : EVENT_TYPE.HEALTH_ANIM_ENEMY));
			TIMESTAMP = screen.battleTimer;
		}
		current = this;
		
		if(screen.battleTimer > TIMESTAMP + 100) {
			if(screen.battleTimer == TIMESTAMP + 105) {
				Jukebox.playSound(screen.queuedSound, 0.03f);
			}
			if(ally)
				screen.allyHitEffect = true;
			else 
				screen.enemyHitEffect = true;
			if(screen.battleTimer > TIMESTAMP + 110) {
				screen.allyHitEffect = false;
				screen.enemyHitEffect = false;
				if(screen.battleTimer < TIMESTAMP + 119) {
					if(screen.battleTimer % 3 == 0) {
						if(ally)
							screen.allyFlashEffect = true;
						else
							screen.enemyFlashEffect = true;
					} else {
						screen.allyFlashEffect = false;
						screen.enemyFlashEffect = false;
					}
				}
			}
			if(screen.battleTimer > TIMESTAMP + 120) {
				current = null;
				screen.allyFlashEffect = false;
				screen.enemyFlashEffect = false;
				screen.queue.remove();
				TIMESTAMP = -1;
				screen.queue.peek().init();
			}
		}
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
				TIMESTAMP = -1;
				if(!screen.queue.isEmpty()) {
					if(screen.queue.peek().getType() == EVENT_TYPE.DELAY ||
					   screen.queue.peek().getType() == EVENT_TYPE.DELAY_HIT_ALLY ||
					   screen.queue.peek().getType() == EVENT_TYPE.DELAY_HIT_ENEMY) {
						screen.queue.peek().init();
					} else {
						screen.queue.remove().init();
					}
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
				} else {
					screen.queue.add(new BattleEvent(screen, null, EVENT_TYPE.DELAY));
				}
				current = null;
				if(!screen.queue.isEmpty()) {
					if(screen.queue.peek().getType() == EVENT_TYPE.DELAY ||
					   screen.queue.peek().getType() == EVENT_TYPE.DELAY_HIT_ALLY ||
				       screen.queue.peek().getType() == EVENT_TYPE.DELAY_HIT_ENEMY) {
						screen.queue.peek().init();
					} else {
						screen.queue.remove().init();
					}
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
	
	private void goBack() {
		if(screen.state.getPrevious() == BATTLE_STATE.QUESTION) {
			reset();
		}
	}
	private void reset() {
		screen.progressor.setDialog("What should \n" + screen.activePokemon.getName() + " do?");
		screen.selected = SELECTED.OPTION_1;
		SELECTED.updateLocations(SELECTED.POSITION.RIGHT);
		screen.state = BATTLE_STATE.QUESTION;
	}
	public Boolean isFinished() {
		return this.finished;
	}
	public EVENT_TYPE getType() {
		return type;
	}
}
