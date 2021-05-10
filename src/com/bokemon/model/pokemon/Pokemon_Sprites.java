package com.bokemon.model.pokemon;

import java.util.Hashtable;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Pokemon_Sprites {
	public Hashtable<String, TextureRegion[]> items = new Hashtable<String, TextureRegion[]>();
	
	public AssetManager manager = new AssetManager();
	public TextureAtlas atlas;	
	
	public Pokemon_Sprites() {
		manager.load("res/graphics_packed/battle/battle_textures.atlas", TextureAtlas.class);
		manager.load("res/graphics_packed/ui/ui_textures.atlas", TextureAtlas.class);
		manager.finishLoading();
		
		atlas = manager.get("res/graphics_packed/battle/battle_textures.atlas", TextureAtlas.class);
	}
}
