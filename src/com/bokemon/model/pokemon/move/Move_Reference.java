package com.bokemon.model.pokemon.move;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.bokemon.Bokemon;
import com.bokemon.util.PokeApi;

public class Move_Reference {
	public static HashMap<String, HashMap<String, String>> moves = new HashMap<String, HashMap<String, String>>();
	private Json json = new Json();
	Preferences pD = Bokemon.pokemon_data;
	
	private ArrayList<String> moveNames;
	
	public Move_Reference() {
		PokeApi all = new PokeApi("move?limit=480");
		moveNames = PokeApi.getAllMoveNames(all.responseContent.toString());
		
		for(String move : moveNames) {
			constructMove(move);
		}
		
		pD.putString("moves", json.toJson(moves, moves.getClass()));
		pD.flush();
		
	}
	
	public void constructMove(String name) {
		PokeApi moveRef = new PokeApi("move/" + name);
		HashMap<String, String> move = PokeApi.getMoveInfo(moveRef.responseContent.toString());
		
		moves.put(name.toUpperCase(), move);
	}
}
