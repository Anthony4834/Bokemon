package com.bokemon.model.pokemon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.bokemon.Settings;
import com.bokemon.model.pokemon.move.Move;
import com.bokemon.model.pokemon.move.Move_Reference;

public class Pokemon implements Serializable {
	private String name;
	private String gender; //true = male, false = female
	private int id;
	private int maxHp;
	private int hp;
	private int atk;
	private int def;
	private int spAtk;
	private int spDef;
	private int spd;
	private int captureRate;
	private int level;
	private int xp;
	private RARITY rarity;
	private JSONArray types;
	private TextureRegion texture;
	private float size;
	private Json json = new Json();
	private ArrayList<Move> moveSet;
	private JSONArray movesLearnedByLevel;
	
	public Pokemon(JSONObject info) {
		name = info.getJSONObject("ORIGIN_NAME").get("value").toString();
		id = Integer.valueOf(info.getJSONObject("ID").get("value").toString());
		maxHp = Integer.valueOf(info.getJSONObject("BASE_HP").get("value").toString()); 
		atk = Integer.valueOf(info.getJSONObject("BASE_ATK").get("value").toString()); 
		def = Integer.valueOf(info.getJSONObject("BASE_DEF").get("value").toString());
		spAtk = Integer.valueOf(info.getJSONObject("BASE_SPATK").get("value").toString());
		spDef = Integer.valueOf(info.getJSONObject("BASE_SPDEF").get("value").toString());
		spd = Integer.valueOf(info.getJSONObject("BASE_SPD").get("value").toString());
		types = json.fromJson(JSONArray.class, info.getJSONObject("TYPES").getString("value"));
		captureRate = Integer.valueOf(info.getJSONObject("CAPTURE_RATE").get("value").toString());
		size = Settings.SCALED_TILE_SIZE * Integer.valueOf(info.getJSONObject("SIZE").get("value").toString());
		gender = Math.random() >= 0.5 ? "male" : "female";
		movesLearnedByLevel = this.getMovesLearnedByLevel(info.getJSONObject("MOVES_LEARNED"));
	}
	public void updateValues() {
		this.maxHp = (int) (((1+(2*this.maxHp)+1+100)*this.level)/100)+10;
		this.atk = (int) (((1+(2*this.atk)+(1))*this.level)/100)+5;
		this.def = (int) (((1+(2*this.def)+(1))*this.level)/100)+5;
		this.spAtk = (int) (((1+(2*this.spAtk)+(1))*this.level)/100)+5;
		this.spDef = (int) (((1+(2*this.spDef)+(1))*this.level)/100)+5;
		this.spd = (int) (((1+(2*this.spd)+(1))*this.level)/100)+5;
	}
	public void printLevels() {
		System.out.println(String.format(" HP [%s] \n ATK [%s] \n DEF [%s] \n SPATK [%s] \n SPDEF [%s] \n SPD [%s]", this.maxHp, this.atk, this.def, this.spAtk, this.spDef, this.spd));
	}
	public String getName() {
		return name;
	}

	public String getGender() {
		return gender;
	}

	public int getHp() {
		return hp;
	}

	public int getAtk() {
		return atk;
	}

	public int getDef() {
		return def;
	}
	public double getHpPercentage() {
		return (this.getHp() * 100 / this.getMaxHp());
	}

	public void setName(String name) {
		this.name = name.toUpperCase();
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public void setAtk(int atk) {
		this.atk = atk;
	}
	public void setDef(int def) {
		this.def = def;
	}
	public void setRarity(RARITY rarity) {
		this.rarity = rarity;
	}
	public RARITY getRarity() {
		return rarity;
	}
	public TextureRegion getTexture() {
		return texture;
	}
	public float getSize() {
		return size;
	}
	public int getMaxHp() {
		return maxHp;
	}
	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getXp() {
		return xp;
	}
	public void setXp(int xp) {
		this.xp = xp;
	}
	public int getId() {
		return id;
	}
	public int getCaptureRate() {
		return captureRate;
	}
	public void setCaptureRate(int captureRate) {
		this.captureRate = captureRate;
	}
	public int getSpAtk() {
		return spAtk;
	}
	public void setSpAtk(int spAtk) {
		this.spAtk = spAtk;
	}
	public int getSpDef() {
		return spDef;
	}
	public void setSpDef(int spDef) {
		this.spDef = spDef;
	}
	public int getSpd() {
		return spd;
	}
	public void setSpd(int spd) {
		this.spd = spd;
	}
	@SuppressWarnings("unchecked")
	public ArrayList<String> getPossibleMoves() {
		ArrayList<String> output = new ArrayList<String>();
		
		for(int i = 0; i < movesLearnedByLevel.length(); i++) {
			Array<Object> current = (Array<Object>) movesLearnedByLevel.get(i);
			if((float) current.get(1) <= level) {
				String moveName = (String) current.get(0);
				output.add(moveName);
			}
		}
		
		return output;
		
	}
	@SuppressWarnings("unchecked")
	private JSONArray getMovesLearnedByLevel(JSONObject ref) {
		HashMap<String, JSONArray> byLevelWrapper = (HashMap<String, JSONArray>) json.fromJson(HashMap.class, ref.getString("value")).get("map");
		JSONArray byLevel = byLevelWrapper.get("BY_LEVEL");
		
		return byLevel;
	}
	public Boolean isType(String type) {
		for(int i = 0; i < types.length(); i++) {
			if(types.getJSONObject(i).getJSONObject("type").getString("name").toUpperCase().equals(type.toUpperCase())) {
				return true;
			}
		}
		return false;
	}
	public JSONArray getTypes() {
		return types;
	}
	public ArrayList<String> getWeaknesses(JSONObject ref,  JSONArray toEval) {
		ArrayList<String> output = new ArrayList<String>(); 
		
		for(int i = 0; i < toEval.length(); i++) {
			String type = toEval.getJSONObject(i).getJSONObject("type").getString("name").toUpperCase();
			JSONArray weaknesses = ref.getJSONObject(type).getJSONObject("DOUBLE_FROM").getJSONArray("myArrayList");
			for(int e = 0; e < weaknesses.length(); e++) {
				output.add(weaknesses.getJSONObject(e).getJSONObject("map").getJSONObject("name").getString("value").toUpperCase());
			}
		}
		
		return output;
	}
	public ArrayList<String> getStrengths(JSONObject ref,  JSONArray toEval) {
		ArrayList<String> output = new ArrayList<String>();
		
		for(int i = 0; i < toEval.length(); i++) {
			if(toEval.getJSONObject(i).getJSONObject("type").getString("name").equals("normal")) {
				continue;
			}
			String type = toEval.getJSONObject(i).getJSONObject("type").getString("name").toUpperCase();
			JSONArray strengths = ref.getJSONObject(type).getJSONObject("HALF_FROM").getJSONArray("myArrayList");
			for(int e = 0; e < strengths.length(); e++) {
				output.add(strengths.getJSONObject(e).getJSONObject("map").getJSONObject("name").getString("value").toUpperCase());
			}
		}
		
		return output;
	}
	public ArrayList<Move> getMoveSet() {
		return moveSet;
	}
	public void setMoveSet(ArrayList<Move> moveSet) {
		this.moveSet = moveSet;
	}
	@Override
	public void read(Json arg0, JsonValue arg1) {
	}
	@Override
	public void write(Json arg0) {
	}
}
