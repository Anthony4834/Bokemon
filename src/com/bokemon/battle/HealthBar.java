package com.bokemon.battle;

import com.badlogic.gdx.graphics.Texture;

public class HealthBar extends BattleScreenObject {
	public static Texture[] textures = {new Texture("res/graphics_unpacked/ui/bar_green.png"), new Texture("res/graphics_unpacked/ui/bar_yellow.png"), new Texture("res/graphics_unpacked/ui/bar_red.png")};
	public static int max = 337;
	
	public HealthBar(float x, float y) {
		super(x, y, 337, 60, 1, textures[0]);
		this.setTexture(textures[0]);
	}
	public void colorCheck() {
		if(this.getSizeX() > (HealthBar.max * 0.55 )) {
			this.setTexture(textures[0]);
		} else if(this.getSizeX() > (HealthBar.max * 0.25 )) {
			this.setTexture(textures[1]);
		} else {
			this.setTexture(textures[2]);
		}
	}
}
