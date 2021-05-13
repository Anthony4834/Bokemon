package com.bokemon.model.pokemon.move;

import org.json.JSONObject;

import com.bokemon.model.pokemon.TYPE;

public class Move {
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
}
