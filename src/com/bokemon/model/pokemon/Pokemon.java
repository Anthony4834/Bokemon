package com.bokemon.model.pokemon;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.bokemon.Settings;

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
	private TYPE type;
	private TextureRegion texture;
	private float size;
	
	public Pokemon(String n, int id, int hp, int atk, int def, int spAtk, int spDef, int spd, int captureRate, int size) {
		this.name = n;
		this.id = id;
		this.maxHp = hp;
		this.hp = hp;
		this.atk = atk;
		this.def = def;
		this.spAtk = spAtk;
		this.spDef = spDef;
		this.spd = spd;
		this.captureRate = captureRate;
		this.size = size*Settings.SCALED_TILE_SIZE;
		this.gender = Math.random() >= 0.5 ? "male" : "female";
		//this.texture = sprites.items.get(n.toUpperCase());
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
		System.out.println(String.format("HP [%s] \n ATK [%s] \n DEF [%s] \n SPATK [%s] \n SPDEF [%s] \n SPD [%s]", this.maxHp, this.atk, this.def, this.spAtk, this.spDef, this.spd));
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
	@Override
	public void read(Json arg0, JsonValue arg1) {
	}
	@Override
	public void write(Json arg0) {
	}
}
