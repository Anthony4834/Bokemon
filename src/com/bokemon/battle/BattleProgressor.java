package com.bokemon.battle;

import java.util.ArrayList;

import com.bokemon.battle.BattleEvent.EVENT_TYPE;
import com.bokemon.model.pokemon.move.Move;
import com.bokemon.screen.BattleScreen;

public class BattleProgressor {
	private BattleScreen screen;
	
	public BattleProgressor(BattleScreen s) {
		this.screen = s;
	}
	
	
	public void attackPokemon() {
		Move move = screen.activePokemon.getMoveSet().get(screen.selected.getNum() - 1);
		setDialog(screen.activePokemon.getName() + " used " + move.getName());
		ArrayList<BattleEvent> instanceEvents = new ArrayList<BattleEvent>();
		
		move.setPp(move.getPp() - 1);
		int attackPower = ((((2 * screen.activePokemon.getLevel()) / 5 + 2) * move.getPower() * (move.getCategory().equals("physical") ? (screen.activePokemon.getAtk() / screen.enemy.getDef()) : (screen.activePokemon.getSpAtk()) / screen.enemy.getSpDef() )) / 50) + 2;
		double rand = Math.random() * (1 - 0.85) + 0.85;
		int dPwr = (int) (attackPower * rand);
		if(move.typeComparison(move, screen.enemy)) {
			dPwr = 0;
			screen.queue.add(new BattleEvent(screen, "It had no effect..", EVENT_TYPE.DIALOG));
			screen.queue.remove().init();
			SELECTED.updateLocations(SELECTED.POSITION.RIGHT);
			return;
		}
		if(Math.random() > 0.875) {
			dPwr = dPwr * 2;
			instanceEvents.add(new BattleEvent(screen, "A critical hit!", EVENT_TYPE.DIALOG));
		}
		if(screen.enemyWeaknesses.contains(move.getType().toString())) {
			dPwr = dPwr * 2;
			instanceEvents.add(new BattleEvent(screen, "It's super effective!", EVENT_TYPE.DIALOG));
		}
		if(screen.enemyStrengths.contains(move.getType().toString())) {
			dPwr = dPwr / 2;
			instanceEvents.add(new BattleEvent(screen, "It's not very effective..", EVENT_TYPE.DIALOG));
		}
		int dHp = screen.enemy.getHp() - dPwr;
		screen.enemy.setHp(dHp > 0 ? dHp : 0);
		screen.queue.add(new BattleEvent(screen, null, EVENT_TYPE.HEALTH_ANIM_ENEMY));
		screen.queue.peek().init();
		for(BattleEvent e : instanceEvents) {
			screen.queue.add(e);
		}
		screen.enemyHpChange = true;
		SELECTED.updateLocations(SELECTED.POSITION.RIGHT);
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
