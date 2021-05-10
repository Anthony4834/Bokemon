package com.bokemon.battle;

import com.badlogic.gdx.graphics.Texture;
import com.bokemon.Settings;

public class Platform extends BattleScreenObject {
	public Platform(float x, float y) {
		super(x*Settings.SCALED_TILE_SIZE, y*Settings.SCALED_TILE_SIZE, 14*Settings.SCALED_TILE_SIZE, 4*Settings.SCALED_TILE_SIZE, 1,  new Texture("res/graphics_unpacked/battle/platform.png"));
	}
}
