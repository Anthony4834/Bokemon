package com.bokemon;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.bokemon.model.pokemon.Pokemon_Reference2;
import com.bokemon.model.pokemon.Type_Reference;
import com.bokemon.model.pokemon.move.Move_Reference;
import com.bokemon.screen.BattleScreen;
import com.bokemon.screen.GameScreen;
import com.bokemon.util.Music_Reference;
import com.bokemon.util.transitions.BattleBlinkTransition;
import com.bokemon.util.transitions.BattleBlinkTransitionAccessor;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

public class Bokemon extends Game{
	

	private GameScreen screen;
	
	private AssetManager assetManager;
	private TweenManager tweenManager;
	public static int apiCalls = 0;
	public static Preferences prefs;
	public static Preferences pokemon_data;
	public static Pokemon_Reference2 ref2;
	public static Type_Reference typeRef;
	public static Music_Reference musicRef;
	public static Move_Reference moveRef;

	@Override
	public void create() {
		
		tweenManager = new TweenManager();
		
		Tween.registerAccessor(BattleBlinkTransition.class, new BattleBlinkTransitionAccessor());
		
		assetManager = new AssetManager();
		assetManager.load("res/graphics_packed/tiles/tile_textures.atlas", TextureAtlas.class);
		assetManager.finishLoading();
		
		prefs = Gdx.app.getPreferences("State");
		
		//pokemon_data = Gdx.app.getPreferences("pokemon_data"); // ***ONLY NEEDED WHEN UPDATING
		//moveRef = new Move_Reference();
//		ref2  = new Pokemon_Reference2();
//		typeRef  = new Type_Reference();	
//		System.out.println(pokemon_data.getString("pokemon").length());
		
		musicRef = new Music_Reference();
		
		screen = new GameScreen(this);
		
		this.setScreen(screen);
		
	}
	@Override
	public void render() {
		
//		tweenManager.update(Gdx.graphics.getDeltaTime());
//		if (getScreen() instanceof AbstractScreen) {
//			((AbstractScreen)getScreen()).(Gdx.graphics.getDeltaTime());
//		}
		
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}
	
	public void startTransition(GameScreen from, BattleScreen to) {
		setScreen(to);
	}
	
	public AssetManager getAssetManager() {
		return assetManager;
	}
	public TweenManager getTweenManager() {
		return tweenManager;
	}

}
