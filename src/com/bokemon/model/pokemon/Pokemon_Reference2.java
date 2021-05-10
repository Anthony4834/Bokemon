package com.bokemon.model.pokemon;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.bokemon.Bokemon;
import com.bokemon.util.PokeApi;

public class Pokemon_Reference2 {
	public static HashMap<String, HashMap<String, String>> pokemon = new HashMap<String, HashMap<String, String>>();
	public static Json json = new Json();
	Preferences pD = Bokemon.pokemon_data;
	
	private ArrayList<String> pokemonNames;

	public Pokemon_Reference2() {
		PokeApi all = new PokeApi("pokedex/1");
		
		pokemonNames = PokeApi.getAllPokemonNames(all.responseContent.toString());
		
		
		for(int i = 0; i < pokemonNames.size(); i++) {
			constructPokemon(pokemonNames.get(i));
		}
		
		pD.putString("pokemon", json.toJson(pokemon, pokemon.getClass()));
		pD.flush();
		
	}
	
	private void constructPokemon(String name) {
		PokeApi api = new PokeApi("pokemon-species/" + name); //GENERAL
		PokeApi subApi = new PokeApi(PokeApi.getSubApi(api.responseContent.toString(), "details")); //POKEMON DETAILS
		HashMap<String, String> poke = PokeApi.getRates(api.responseContent.toString(), //RATES
				PokeApi.getStats(subApi.responseContent.toString())); //STATS
		subApi = new PokeApi(PokeApi.getSubApi(api.responseContent.toString(), "evolution"));
		String evolution = PokeApi.getEvolution(subApi.responseContent.toString()); //EVO
		poke.put("NEXT_EVO", evolution != null ? evolution.toUpperCase() : null);
		
		pokemon.put(name.toUpperCase(), poke);
	}
	public void printAllNames() {
		int count = 0;
		for(int i = 0; i < pokemonNames.size(); i++) {
			System.out.println(pokemonNames.get(i));
			count++;
		}
		System.out.println(count);
	}
}
