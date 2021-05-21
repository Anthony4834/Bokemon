package com.bokemon.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.badlogic.gdx.utils.Json;
import com.bokemon.Bokemon;

public class PokeApi {
	private static HttpURLConnection connection;
	private BufferedReader reader;
	private String line;
	public StringBuffer responseContent = new StringBuffer();
	private static Json json = new Json();
	private static String[] sL = {"POOCHYENA", "WEEDLE", "MEWTWO"};
	private static List<String> shrinkList = new ArrayList<String>(Arrays.asList(sL));
	
	public PokeApi(String query) {
		Bokemon.apiCalls ++;
		System.out.println(Bokemon.apiCalls);
		try {
			URL url = new URL("https://pokeapi.co/api/v2/" + query);
			connection = (HttpURLConnection) url.openConnection();
			
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(200000);
			connection.setReadTimeout(200000);
			
			int status = connection.getResponseCode();
			
			if(status > 299) {
				reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				while((line = reader.readLine()) != null) {
					responseContent.append(line);
				}
				reader.close();
			} else {
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				while((line = reader.readLine()) != null) {
					responseContent.append(line);
				}
				reader.close();
			}
			System.out.println(responseContent);
		} catch(MalformedURLException e) {
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		} finally {
			connection.disconnect();
		}
	}
	public static String getSubApi(String responseBody, String type) {
		JSONObject result = new JSONObject(responseBody);
		
		switch(type) {
			case "details":
				JSONArray resultArr = result.getJSONArray("varieties");
				return resultArr.getJSONObject(0).getJSONObject("pokemon").getString("url").substring(26);
			case "evolution":
				return result.getJSONObject("evolution_chain").getString("url").substring(26);
			default:
				return null;
		}
		
	}
	
	public static HashMap<String, String> getStats(String responseBody) {
		JSONObject result = new JSONObject(responseBody);
		JSONArray statsObjs = result.getJSONArray("stats");
		JSONArray types = result.getJSONArray("types");
		JSONArray moves = result.getJSONArray("moves");
		
		
		
		HashMap<String, String> pokemon = new HashMap<String, String>();
		
		pokemon.put("ID", String.valueOf(result.getInt("id"))); //STATS
		pokemon.put("ORIGIN_NAME", result.getString("name").toUpperCase());
		pokemon.put("NAME", result.getString("name").toUpperCase());
		pokemon.put("BASE_HP", String.valueOf(statsObjs.getJSONObject(0).getInt("base_stat")));
		pokemon.put("BASE_ATK", String.valueOf(statsObjs.getJSONObject(1).getInt("base_stat")));
		pokemon.put("BASE_DEF", String.valueOf(statsObjs.getJSONObject(2).getInt("base_stat")));
		pokemon.put("BASE_SPATK", String.valueOf(statsObjs.getJSONObject(3).getInt("base_stat")));
		pokemon.put("BASE_SPDEF", String.valueOf(statsObjs.getJSONObject(4).getInt("base_stat")));
		pokemon.put("BASE_SPD", String.valueOf(statsObjs.getJSONObject(5).getInt("base_stat")));
		pokemon.put("SIZE", shrinkList.contains(pokemon.get("ORIGIN_NAME")) ? "7" : "9");
		pokemon.put("TYPES", json.toJson(types));
		pokemon.put("MOVES_LEARNED", getMovesLearned(moves));
		
		return pokemon;
	}
	public static String getMovesLearned(JSONArray allMoves) {
		JSONObject output = new JSONObject();
		
		JSONArray byLevel = new JSONArray();
		JSONArray byMachine = new JSONArray();
		
		for(int i = 0; i < allMoves.length(); i++) {
			if(allMoves.getJSONObject(i).getJSONArray("version_group_details").getJSONObject(0).getJSONObject("move_learn_method").getString("name").equals("level-up")) {
				String name = allMoves.getJSONObject(i).getJSONObject("move").getString("name");
				String level = String.valueOf(allMoves.getJSONObject(i).getJSONArray("version_group_details").getJSONObject(0).getInt("level_learned_at"));
				
				byLevel.put(new String[] {name, level});
			} else if(allMoves.getJSONObject(i).getJSONArray("version_group_details").getJSONObject(0).getJSONObject("move_learn_method").getString("name").equals("machine")) {
				String name = allMoves.getJSONObject(i).getJSONObject("move").getString("name");
				byMachine.put(name);
			}
		}
		
		output.put("BY_LEVEL", byLevel);
		output.put("BY_MACHINE", byMachine);
		
		return json.toJson(output);
	}
	public static HashMap<String, JSONArray> getDamageRelations(String responseBody) {
		JSONObject result = new JSONObject(responseBody);
		JSONObject stats = result.getJSONObject("damage_relations");
		
		HashMap<String, JSONArray> type = new HashMap<String, JSONArray>();
		
		type.put("DOUBLE_TO", stats.getJSONArray("double_damage_to"));
		type.put("DOUBLE_FROM", stats.getJSONArray("double_damage_from"));
		type.put("HALF_TO", stats.getJSONArray("half_damage_to"));
		type.put("HALF_FROM", stats.getJSONArray("half_damage_from"));
		
		return type;
		
	}
	public static HashMap<String, String> getRates(String responseBody, HashMap<String, String> poke) {
		JSONObject result = new JSONObject(responseBody);
		poke.put("GROWTH_RATE", result.getJSONObject("growth_rate").getString("name"));
		poke.put("CAPTURE_RATE", String.valueOf(result.getInt("capture_rate")));
		
		return poke;
	}
	public static String getEvolution(String responseBody) {
		JSONObject result = new JSONObject(responseBody);
		if(result.getJSONObject("chain").getJSONArray("evolves_to").length() < 1) {
			return null;
		}
		return result.getJSONObject("chain").getJSONArray("evolves_to").getJSONObject(0).getJSONObject("species").getString("name");
	}
	public static ArrayList<String> getAllPokemonNames(String responseBody) {
		JSONObject result = new JSONObject(responseBody);
		JSONArray pokeArray = result.getJSONArray("pokemon_entries");
		ArrayList<String> output = new ArrayList<String>();
		
		for(int i=0; i<pokeArray.length(); i++) {
			output.add(pokeArray.getJSONObject(i).getJSONObject("pokemon_species").getString("name"));
		}
		
		return output;
		
	}
	
	public static ArrayList<String> getAllMoveNames(String responseBody) {
		ArrayList<String> output = new ArrayList<String>();
		
		JSONObject result = new JSONObject(responseBody);
		JSONArray moves = result.getJSONArray("results");
		
		for(int i = 0; i < moves.length(); i++) {
			output.add(moves.getJSONObject(i).getString("name"));
		}
		
		return output;
	}
	
	public static HashMap<String, String> getMoveInfo(String responseBody) {
		HashMap<String, String> output = new HashMap<String, String>();
		
		JSONObject result = new JSONObject(responseBody);
		
		output.put("ACCURACY", String.valueOf(result.get("accuracy")));
		output.put("CATEGORY", String.valueOf(result.getJSONObject("damage_class").get("name")));
		output.put("POWER", String.valueOf(result.get("power")));
		output.put("TYPE", String.valueOf(result.getJSONObject("type").get("name")));
		output.put("MAX_PP", String.valueOf(result.get("pp")));
		output.put("MAKES_CONTACT", String.valueOf(output.get("CATEGORY").equals("physical")));
		output.put("PRIORITY", String.valueOf(result.getInt("priority")));
		output.put("EFFECT", result.getJSONArray("effect_entries").length() > 0 ? String.valueOf(result.getJSONArray("effect_entries").getJSONObject(0).get("short_effect")) : null);
		output.put("EFFECT_CHANCE", String.valueOf(result.get("effect_chance")));
		
		return output;
		
	}
}
