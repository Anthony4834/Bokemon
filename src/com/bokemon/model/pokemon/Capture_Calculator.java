package com.bokemon.model.pokemon;

public abstract class Capture_Calculator {

	public static boolean attemptCapture(int a) {
		
		if( (int) ( (Math.random() * 240) + 1) <= a ) {
			return true;
		}	
		return false;
	}
	public static int genChances(Pokemon enemy) {
		return (int) Math.ceil( ( (3 * enemy.getMaxHp() - 2 * enemy.getHp() ) * enemy.getCaptureRate() ) / ( 3 * enemy.getMaxHp() ) );
	}
	public static boolean shake(int a) {
		int num = (int) ( (Math.random() * 255) + 1);
		
		System.out.println(String.format("Generated: %s - Break number: %s", num, convert(a)));
		
		return num >= convert(a);
	}
	
	private static int convert(int input) {
		if(input >= 255 ) {
			return 225;
		}
		if(input > 241) {
			return 223;
		}
		if(input > 221) {
			return 221;
		}
		if(input > 201) {
			return 216;
		}
		if(input > 181) {
			return 210;
		}
		if(input > 161) {
			return 204;
		}
		if(input > 141) {
			return 197;
		}
		if(input > 121) {
			return 190;
		}
		if(input > 101) {
			return 179;
		}
		if(input > 81) {
			return 170;
		}
		if(input > 61) {
			return 161;
		}
		if(input > 51) {
			return 147;
		}
		if(input > 41) {
			return 139;
		}
		if(input > 31) {
			return 130;
		}
		if(input > 21) {
			return 119;
		}
		if(input > 16) {
			return 104;
		}
		if(input > 11) {
			return 96;
		}
		if(input > 8) {
			return 93;
		}
		if(input > 6) {
			return 73;
		}
		if(input > 5) {
			return 65;
		}
		if(input > 4) {
			return 60;
		}
		if(input > 3) {
			return 54;
		}
		if(input > 2) {
			return 45;
		}
		return 33;
	}
}
