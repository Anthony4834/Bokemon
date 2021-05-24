package com.bokemon.battle;

import java.util.ArrayList;

import com.bokemon.Bokemon;
import com.bokemon.battle.BattleEvent.EVENT_TYPE;
import com.bokemon.battle.SELECTED.POSITION;
import com.bokemon.model.pokemon.Capture_Calculator;
import com.bokemon.model.pokemon.Pokemon;
import com.bokemon.model.pokemon.move.Move;
import com.bokemon.screen.BattleScreen;

public class BattleProgressor {
	private BattleScreen screen;
	
	public Boolean awaitingAttack;
	public Boolean allyAwaitingAttack;
	public Move awaitingMove;
	
	public BattleProgressor(BattleScreen s) {
		this.screen = s;
		this.awaitingAttack = false;
	}
	
	public void decideOrder(Pokemon ally, Pokemon enemy) {
		Move move_ally = ally.getMoveSet().get(screen.selected.getNum() - 1);
		Move move_enemy = enemy.getMoveSet().get( (int) (Math.random() * enemy.getMoveSet().size())); 
		
		awaitingAttack = true;
		
		if(move_ally.getPriority() > move_enemy.getPriority()) {
			attackPokemon(ally, enemy, move_ally);
			allyAwaitingAttack = false;
			awaitingMove = move_enemy;
		} else if(move_enemy.getPriority() > move_ally.getPriority()) {
			attackPokemon(enemy, ally, move_enemy);
			allyAwaitingAttack = true;
			awaitingMove = move_ally;
		} else {
			if(ally.getSpd() >= enemy.getSpd()) {
				attackPokemon(ally, enemy, move_ally);
				allyAwaitingAttack = false;
				awaitingMove = move_enemy;
			} else {
				attackPokemon(enemy, ally, move_enemy);
				allyAwaitingAttack = true;
				awaitingMove = move_ally;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void attackPokemon(Pokemon attacker, Pokemon target, Move move) {
		int dPwr;
		ArrayList<Object> damageStats;
		ArrayList<BattleEvent> instanceEvents;
		
		setDialog(attacker.getName() + " used " + move.getName());
		
		dPwr = calculateBaseDamage(attacker, target, move);
		
		damageStats = initDamageModifiers(attacker, target, move, dPwr);
		instanceEvents = (ArrayList<BattleEvent>) damageStats.get(1);
		dPwr = (int) damageStats.get(0);
		
		int dHp = target.getHp() - dPwr;
		target.setHp(dHp > 0 ? dHp : 0);
		
		screen.queue.add(new BattleEvent(screen, null, attacker == screen.enemy ? EVENT_TYPE.DELAY_HIT_ALLY : EVENT_TYPE.DELAY_HIT_ENEMY));
		screen.queue.peek().init();
		for(BattleEvent e : instanceEvents) {
			screen.queue.add(e);
		}
		move.setPp(move.getPp() - 1);
		if(target == screen.enemy) {
			screen.enemyHpChange = true;
			SELECTED.updateLocations(SELECTED.POSITION.RIGHT);
			Bokemon.prefs.putInteger(String.format("poke%s_mv%s_pp", screen.activePokemonNum + 1, screen.selected.getNum()), move.getPp());
		} else {
			System.out.print(move.getName());
			screen.hpChange = true;
		}
		
		BattleEvent.TIMESTAMP = -1;
		SELECTED.updateLocations(POSITION.RIGHT);
	}
	private int calculateBaseDamage(Pokemon attacker, Pokemon target, Move move) {
		double attackPower = ((double) (((((2 * attacker.getLevel()) / 5 + 2) * move.getPower() * (move.getCategory().equals("physical") ? (attacker.getAtk() / screen.enemy.getDef()) : (attacker.getSpAtk()) / target.getSpDef() )) / 50) + 2));
		double rand = Math.random() * (1 - 0.85) + 0.85;
		
		return (int) (attackPower * rand);
	}
	private ArrayList<Object> initDamageModifiers (Pokemon attacker, Pokemon target, Move move, float dPwr) {
		ArrayList<Object> output = new ArrayList<Object>();
		ArrayList<BattleEvent> instanceEvents = new ArrayList<BattleEvent>();
		
		if(move.typeComparison(move, target)) {
			dPwr = 0;
			screen.queue.add(new BattleEvent(screen, "It had no effect..", EVENT_TYPE.DIALOG));
			screen.queue.remove().init();
			SELECTED.updateLocations(SELECTED.POSITION.RIGHT);
			output.add(0);
			output.add(instanceEvents);
			return output;
		}
		screen.queuedSound = "hit";
		if(Math.random() > 0.875) {
			dPwr = dPwr * 2;
			instanceEvents.add(new BattleEvent(screen, "A critical hit!", EVENT_TYPE.DIALOG));
		}
		if(target == screen.enemy) { //IF TARGET = ENEMY
			if(screen.enemyWeaknesses.contains(move.getType().toString())) {
				dPwr = dPwr * 2;
				screen.queuedSound = "hit_strong";
				instanceEvents.add(new BattleEvent(screen, "It's super effective!", EVENT_TYPE.DIALOG));
			}
			if(screen.enemyStrengths.contains(move.getType().toString())) {
				dPwr = dPwr / 2;
				screen.queuedSound = "hit_weak";
				instanceEvents.add(new BattleEvent(screen, "It's not very effective..", EVENT_TYPE.DIALOG));
			}
		} else { //IF TARGET = PLAYER
			if(screen.allyWeaknesses.contains(move.getType().toString())) {
				dPwr = dPwr * 2;
				instanceEvents.add(new BattleEvent(screen, "It's super effective!", EVENT_TYPE.DIALOG));
			}
			if(screen.allyStrengths.contains(move.getType().toString())) {
				dPwr = dPwr / 2;
				instanceEvents.add(new BattleEvent(screen, "It's not very effective..", EVENT_TYPE.DIALOG));
			}
		}
		output.add((int) dPwr);
		output.add(instanceEvents);
		return output;
	}
	public void switchPokemon() {
		if(BattleEvent.switchCounter == 0) {
			switchPokemon_1();
		} else {
			switchPokemon_2();
		}
	}
	private void switchPokemon_1() {
		new BattleEvent(screen, "Come back " + screen.activePokemon.getName() + "!", EVENT_TYPE.DIALOG).init();
		
		BattleEvent.switchCounter += 1;
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
		
		BattleEvent.switchCounter = 0;
	}
	public void capturePokemon() {
		if(BattleEvent.captureCounter == 0) {
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
