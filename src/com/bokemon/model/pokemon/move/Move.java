package com.bokemon.model.pokemon.move;

import org.json.JSONObject;

import com.bokemon.model.pokemon.Pokemon;
import com.bokemon.model.pokemon.TYPE;

public class Move {
	private String name;
	private TYPE type; // 
	private String category; //
	private int power; // 
	private int accuracy; //
	private int pp; //
	private int max_pp; //
	private Boolean makesContact; //
	private int priority;
	private EFFECT effect;
	private double effect_chance;
	
	public Move(String t, String c, int p, int a, int pp, int max_pp, Boolean mC, int prio, String effect, double effect_chance) {
		this.type = TYPE.valueOf(t.toUpperCase());
		this.category = c;
		this.power = p;
		this.accuracy = a;
	    this.pp = pp;
	    this.max_pp = max_pp;
	    this.makesContact = mC;
	    this.priority = prio;
	    this.effect = fetchEffect(effect);
	    this.effect_chance = effect_chance;
	}
	public Move(JSONObject move) {
		makesContact = Boolean.valueOf(move.getJSONObject("MAKES_CONTACT").getString("value"));
		category = String.valueOf(move.getJSONObject("CATEGORY").getString("value"));
		effect = EFFECT.CONFUSED; //String.valueOf(move.getJSONObject("CATEGORY").getString("effect"));
		effect_chance = move.getJSONObject("EFFECT_CHANCE").get("value").equals("null") ? 0 : move.getJSONObject("EFFECT_CHANCE").getInt("value");
		power = move.getJSONObject("POWER").get("value").equals("null") ? 0 : Integer.valueOf(move.getJSONObject("POWER").getInt("value"));
		priority = Integer.valueOf(move.getJSONObject("PRIORITY").getInt("value"));
		type = TYPE.valueOf(move.getJSONObject("TYPE").getString("value").toUpperCase());
		accuracy = move.getJSONObject("ACCURACY").get("value").equals("null") ? 0 : move.getJSONObject("ACCURACY").getInt("value");
		name = String.valueOf(move.getJSONObject("NAME").getString("value"));
		max_pp = Integer.valueOf(move.getJSONObject("MAX_PP").getInt("value"));
	}
	public Boolean typeComparison(Move move, Pokemon target) {
		return ( target.isType("flying") && move.getType().equals(TYPE.GROUND) ) || ( target.isType("ghost") && move.getType().equals(TYPE.NORMAL) ) || ( target.isType("normal") && move.getType().equals(TYPE.GHOST) );
	}
	public int getPp() {
		return pp;
	}

	public void setPp(int pp) {
		this.pp = pp;
	}

	public int getMax_pp() {
		return max_pp;
	}

	public void setMax_pp(int max_pp) {
		this.max_pp = max_pp;
	}

	public TYPE getType() {
		return type;
	}

	public String getCategory() {
		return category;
	}

	public int getPower() {
		return power;
	}

	public int getAccuracy() {
		return accuracy;
	}

	public Boolean getMakesContact() {
		return makesContact;
	}
	private EFFECT fetchEffect(String desc) {
		return null;
	}
	public int getPriority() {
		return priority;
	}

	public EFFECT getEffect() {
		return effect;
	}

	public double getEffect_chance() {
		return effect_chance;
	}
	public String getName() {
		return name;
	}
}
