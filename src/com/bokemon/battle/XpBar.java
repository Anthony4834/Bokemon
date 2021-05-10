package com.bokemon.battle;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class XpBar extends BattleScreenObject {
	private TextureRegion texture;
	public static int max = 679;
	
	public XpBar(float x, float y) {
		super(x, y, 679, 43, 1, null);
	}
	
	public TextureRegion get() {
		return texture;
	}
	public void set(TextureRegion tex) {
		this.texture = tex;
	}
}
