package com.bokemon.model.pokemon;
import java.util.HashMap;

import org.json.JSONArray;

import com.bokemon.Bokemon;
import com.bokemon.util.PokeApi;

public class Type_Reference {
	public HashMap<String, HashMap<String, JSONArray>> types = new HashMap<String, HashMap<String, JSONArray>>();
	
	private PokeApi api;
	private String[] typeNames = {"bug", "dark", "dragon", "electric", "fairy", "flying", "fighting", "fire", "ghost", "grass", "ground", "ice", "normal", "poison", "psychic", "rock", "steel", "water"};

	public Type_Reference() {
		for(String type : typeNames) {
			api = new PokeApi("type/" + type); //GENERAL
			types.put(type.toUpperCase(), PokeApi.getDamageRelations(api.responseContent.toString()));
		}
		Bokemon.pokemon_data.put(types);
		Bokemon.pokemon_data.flush();
//		api = new PokeApi("type/dark"); //GENERAL
//		types.put("DARK", PokeApi.getDamageRelations(api.responseContent.toString()));
//		
//		api = new PokeApi("type/dragon"); //GENERAL
//		types.put("DRAGON", PokeApi.getDamageRelations(api.responseContent.toString()));
//		
//		api = new PokeApi("type/electric"); //GENERAL
//		types.put("ELECTRIC", PokeApi.getDamageRelations(api.responseContent.toString()));
//		
//		api = new PokeApi("type/fairy"); //GENERAL
//		types.put("FAIRY", PokeApi.getDamageRelations(api.responseContent.toString()));
//		
//		api = new PokeApi("type/fighting"); //GENERAL
//		types.put("FIGHTING", PokeApi.getDamageRelations(api.responseContent.toString()));
//		
//		api = new PokeApi("type/fire"); //GENERAL
//		types.put("FIRE", PokeApi.getDamageRelations(api.responseContent.toString()));
//		
//		api = new PokeApi("type/flying"); //GENERAL
//		types.put("FLYING", PokeApi.getDamageRelations(api.responseContent.toString()));
//		
//		api = new PokeApi("type/ghost"); //GENERAL
//		types.put("GHOST", PokeApi.getDamageRelations(api.responseContent.toString()));
//		
//		api = new PokeApi("type/grass"); //GENERAL
//		types.put("GRASS", PokeApi.getDamageRelations(api.responseContent.toString()));
//		
//		api = new PokeApi("type/ground"); //GENERAL
//		types.put("GROUND", PokeApi.getDamageRelations(api.responseContent.toString()));
//		
//		api = new PokeApi("type/ice"); //GENERAL
//		types.put("ICE", PokeApi.getDamageRelations(api.responseContent.toString()));
//		
//		api = new PokeApi("type/normal"); //GENERAL
//		types.put("NORMAL", PokeApi.getDamageRelations(api.responseContent.toString()));
//		
//		api = new PokeApi("type/poison"); //GENERAL
//		types.put("POISON", PokeApi.getDamageRelations(api.responseContent.toString()));
//		
//		api = new PokeApi("type/psychic"); //GENERAL
//		types.put("PSYCHIC", PokeApi.getDamageRelations(api.responseContent.toString()));
//		
//		api = new PokeApi("type/rock"); //GENERAL
//		types.put("ROCK", PokeApi.getDamageRelations(api.responseContent.toString()));
//		
//		api = new PokeApi("type/steel"); //GENERAL
//		types.put("STEEL", PokeApi.getDamageRelations(api.responseContent.toString()));
//		
//		api = new PokeApi("type/water"); //GENERAL
//		types.put("WATER", PokeApi.getDamageRelations(api.responseContent.toString()));
//		
	}
}
