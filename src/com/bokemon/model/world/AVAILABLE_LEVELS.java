package com.bokemon.model.world;

public enum AVAILABLE_LEVELS {
	
	
	PALLET_TOWN(60, 60),
	;
	
	private int min, max;
	
	private AVAILABLE_LEVELS(int min, int max) {
		this.min = min;
		this.max = max;
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}
}
