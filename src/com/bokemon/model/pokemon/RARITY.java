package com.bokemon.model.pokemon;

public enum RARITY {
	
	COMMON(0.0, 0.8),
	UNCOMMON(0.81, 0.95),
	RARE(0.96, 0.99),
	ULTRA(0.991, 0.999),
	;
	
	private double min, max;
	
	private RARITY(double min, double max) {
		this.min = min;
		this.max = max;
	}
	
	public double getMin() {
		return this.min;
	}
	public double getMax() {
		return this.max;
	}
}
