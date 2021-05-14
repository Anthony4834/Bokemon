package com.bokemon.util;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Jukebox {
	private static HashMap<String, Music> music;
	private static HashMap<String, Sound> sounds;
	public static Music current;
	
	static {
		current = null;
		music = new HashMap<String, Music>();
		sounds = new HashMap<String, Sound>();
	}
	public static void init() {
		music.put("pallet_town", Gdx.audio.newMusic(Gdx.files.internal("res/sounds/pallet_town.mp3")));
		music.put("wild_battle", Gdx.audio.newMusic(Gdx.files.internal("res/sounds/wild_battle.mp3")));
		
		sounds.put("select", Gdx.audio.newSound(Gdx.files.internal("res/sounds/select.ogg")));
	}
	
	public static void playMusic(String s) {
		if(current != null) {
			current.pause();
		}
		Music m = music.get(s);
		
		current = m;
		current.play();
		current.setLooping(true);
		current.setVolume((float) 0.05);
	}
	public static void playSound(String s) {
		sounds.get(s).play(1f);
	}
}
