package com.bokemon.battle;

public enum SELECTED {
	OPTION_1(22.5, 4.5),
	OPTION_2(31.5, 4.5),
	OPTION_3(22.5, 2.5),
	OPTION_4(31.5, 2.5),
	;
	
	private float x, y;
	private SELECTED left;
	private SELECTED right;
	private SELECTED up;
	private SELECTED down;
	private Boolean empty = true;

	private SELECTED(double x, double y) {
		this.x = (float) x;
		this.y = (float) y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	public void setX(float x) {
		this.x = x;
	}
	public void setY(float y) {
		this.y = y;
	}
	public SELECTED getLeft() {
		return left;
	}

	public SELECTED getRight() {
		return right;
	}

	public SELECTED getUp() {
		return up;
	}

	public SELECTED getDown() {
		return down;
	}
	public Boolean isEmpty() {
		return empty;
	}
	public void setIsEmpty(Boolean e) {
		empty = e;
	}
	public Integer getNum() {
		return Integer.valueOf(this.toString().substring(7));
	}
	public enum POSITION {
		LEFT,
		RIGHT
	}
	public static void updateLocations(POSITION p) {
		switch(p) {
		case LEFT:
			for(int i = 1; i <= 4; i++) {
				if(i % 2 == 0) {
					SELECTED.valueOf(String.format("OPTION_%s", i)).setX((float) 31.5 - 16);
				} else {
					SELECTED.valueOf(String.format("OPTION_%s", i)).setX((float) 22.5 - 16);
				}
			}
			break;
		case RIGHT:
			for(int i = 1; i <= 4; i++) {
				if(i % 2 == 0) {
					SELECTED.valueOf(String.format("OPTION_%s", i)).setX((float) 31.5);
				} else {
					SELECTED.valueOf(String.format("OPTION_%s", i)).setX((float) 22.5);
				}
			}
		default:
			assert false;
			return;
		}
	}
	public void constructRelations() {
		OPTION_1.down = OPTION_3;
		OPTION_2.down = OPTION_4;
		OPTION_3.down = OPTION_1;
		OPTION_4.down = OPTION_2;
		
		OPTION_1.up = OPTION_3;
		OPTION_2.up = OPTION_4;
		OPTION_3.up = OPTION_1;
		OPTION_4.up = OPTION_2;
		
		OPTION_1.left = null;
		OPTION_2.left = OPTION_1;
		OPTION_3.left = OPTION_2;
		OPTION_4.left = OPTION_3;

		OPTION_1.right = OPTION_2;
		OPTION_2.right = OPTION_3;
		OPTION_3.right = OPTION_4;
		OPTION_4.right = null;
	}
}
