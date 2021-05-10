package com.bokemon.model.world;

public enum AVAILABLE_POKEMON {
	
	
	PALLET_TOWN( 
			new String[] {"WEEDLE", "PIDGEY", "POOCHYENA"}, //COMMON
			new String[] {"SHINX", "PIDGEOTTO", "ABRA", "DEINO", "EEVEE"}, //UNCOMMON
			new String[] {"SKARMORY", "LAPRAS", "DODRIO"}, //RARE
			new String[] {"CHARMANDER", "MEWTWO"} ), //ULTRA
	;
	
	public String[] COMMON, UNCOMMON, RARE, ULTRA;
	
	private AVAILABLE_POKEMON(String[] C, String[] UC, String[] R, String[] U) {
		this.COMMON = C;
		this.UNCOMMON = UC;
		this.RARE = R;
		this.ULTRA = U;
	}
}
