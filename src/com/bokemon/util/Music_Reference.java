package com.bokemon.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class Music_Reference {
	public Music pallet_town;
	public Music battle;
	public static Music current;
	
	public Music_Reference() {
		pallet_town = Gdx.audio.newMusic(Gdx.files.internal("res/sounds/pallet_town.mp3"));
		battle = Gdx.audio.newMusic(Gdx.files.internal("res/sounds/wild_battle.mp3"));
	}
	
	public void playMusic(Music m) {
		if(Music_Reference.current != null) {
			current.pause();
		}
		
		current = m;
		current.play();
		current.setLooping(true);
		current.setVolume((float) 0.05);
	}
}
