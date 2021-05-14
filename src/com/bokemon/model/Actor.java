package com.bokemon.model;

import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.bokemon.Bokemon;
import com.bokemon.model.pokemon.Pokemon;
import com.bokemon.model.pokemon.RARITY;
import com.bokemon.model.world.AVAILABLE_POKEMON;
import com.bokemon.model.world.TERRAIN;
import com.bokemon.model.world.Tile;
import com.bokemon.model.world.TileMap;
import com.bokemon.screen.BattleScreen;
import com.bokemon.screen.GameScreen;
import com.bokemon.util.AnimationSet;

public class Actor {
	private GameScreen screen;
	private TileMap map;
	private int x;
	private int y;
	private DIRECTION facing;
	
	private Boolean startingBattle = false;
	
	private float worldX, worldY;
	private int srcX, srcY;
	private int destX, destY;
	private float animTimer;
	private float ANIM_TIME = 0.35f;
	
	private float walkTimer;
	private boolean moveRequestThisFrame;
	
	private ACTOR_STATE state;
	
	private AnimationSet animations;
	
	public Actor(TileMap map, int x, int y, AnimationSet animations, GameScreen screen) {
		this.screen = screen;
		this.map = map;
		this.x = x;
		this.y = y;
		this.animations = animations;
		this.worldX = x;
		this.worldY = y;
		map.getTile(x,  y).setActor(this);
		
		this.state = ACTOR_STATE.STANDING;
		this.facing = DIRECTION.NORTH;
	}
	
	public enum ACTOR_STATE {
		WALKING,
		STANDING,
		RUNNING,
		;
	}
	
	public void update(float delta) { //SAFE
		if(state == ACTOR_STATE.WALKING) {
			animTimer += delta;
			walkTimer += delta;
			worldX = Interpolation.linear.apply(srcX, destX, animTimer / ANIM_TIME);
			worldY = Interpolation.linear.apply(srcY, destY, animTimer / ANIM_TIME);
			if(animTimer > ANIM_TIME) {
				float leftOverTime = animTimer - ANIM_TIME;
				walkTimer -= leftOverTime;
				finishMove();
				if(moveRequestThisFrame ) {
					move(facing);
				} else {
					walkTimer = 0f;
				}
			}
		}
		moveRequestThisFrame = false;
	}

	public boolean move(DIRECTION dir) { //SAFE
		if(state == ACTOR_STATE.WALKING) {
			if(facing == dir) {
				moveRequestThisFrame = true;
			}
			return false;
		}
		if(x+dir.getDx() >= map.getWidth() || x+dir.getDx() < 0 || y+dir.getDy() >= map.getHeight() || y+dir.getDy() < 0) {
			return false;
		}
		if(map.getTile(x+dir.getDx(), y+dir.getDy()).getActor() != null || map.getTile(x+dir.getDx(), y+dir.getDy()).getObject() != null) {
			this.facing = dir;
			return false;
		}
		initMove(dir);
		map.getTile(x,  y).setActor(null);
		x += dir.getDx();
		y += dir.getDy();
		map.getTile(x, y).setActor(this);
		checkGrass(map.getTile(x, y));
		return true;
	}
	
	private Boolean checkGrass(Tile tile) {
		System.out.println(tile.getTerrain());
		if(tile.getTerrain() == TERRAIN.TALL_GRASS) {
			double num = Math.random();
			if(num > 0.85) {
				this.screen.controller.freeze();
				Bokemon.musicRef.playMusic(Bokemon.musicRef.battle);
				double num2 = Math.random();
				this.screen.initBattle(num2, AVAILABLE_POKEMON.PALLET_TOWN);
				return true;
			}
		}
		return false;
	}
	
	public Pokemon initBattle(double num, AVAILABLE_POKEMON avail) {
		System.out.println(Bokemon.apiCalls);
		Bokemon.prefs.putInteger("playerX", this.getX());
		Bokemon.prefs.putInteger("playerY", this.getY());
		Bokemon.prefs.putString("poke1", "MEWTWO");
		Bokemon.prefs.putString("poke1_lv", "100");
		Bokemon.prefs.putString("poke1_hp", "4");
		Bokemon.prefs.putString("poke1_mv1", "TACKLE");
		Bokemon.prefs.putString("poke1_mv2", "HYDRO-PUMP");
		Bokemon.prefs.putString("poke1_mv3", "BITE");
		Bokemon.prefs.putString("poke1_mv4", "GIGA-DRAIN");
		Bokemon.prefs.putString("poke2", "CHARMANDER");
		Bokemon.prefs.putString("poke2_lv", "23");
		Bokemon.prefs.putString("poke2_mv1", "TACKLE");
		Bokemon.prefs.putString("poke3", "ABRA");
		Bokemon.prefs.putString("poke3_lv", "14");
		Bokemon.prefs.putString("poke3_hp", "25");
		Bokemon.prefs.putString("poke3_mv1", "TACKLE");
		
		if(num < RARITY.COMMON.getMax()) {
			Integer r = (int) Math.floor((Math.random() * (avail.COMMON.length)));
			
			this.screen.initTransition(
					this.screen, 
					new BattleScreen(this.screen.getApp(), avail.COMMON[r], true)
			);
			
			System.out.println(String.format("Found a %s!", avail.COMMON[r]));
			return null;
		} else if(num < RARITY.UNCOMMON.getMax())  {
			Integer r = (int) Math.floor((Math.random() * (avail.UNCOMMON.length)));
			
			this.screen.initTransition(
					this.screen, 
					new BattleScreen(this.screen.getApp(), avail.UNCOMMON[r], true)
			);
			
			System.out.println(String.format("Found a %s!", avail.UNCOMMON[r]));
			return null;
		} else if(num < RARITY.RARE.getMax())  {
			
			this.screen.initTransition(
					this.screen, 
					new BattleScreen(this.screen.getApp(), avail.RARE[0], true)
			);
			
			System.out.println(String.format("Found a %s!", avail.RARE[0]));
			return null;
		} else {
			
			this.screen.initTransition(
					this.screen, 
					new BattleScreen(this.screen.getApp(), avail.ULTRA[0], true)
			);
			
			System.out.println(String.format("Found a %s!", avail.ULTRA[0]));
			return null;
		}
	}
	
	private void initMove(DIRECTION dir) { //SAFE
		this.facing = dir;
		this.srcX = x;
		this.srcY = y;
		this.destX = x+dir.getDx();
		this.destY = y+dir.getDy();
		this.worldX = x;
		this.worldY = y;
		animTimer = 0f;
		state = ACTOR_STATE.WALKING;
	}
	private void finishMove() {
		state = ACTOR_STATE.STANDING;
		this.worldX = destX;
		this.worldY = destY;
		this.srcX = 0;
		this.srcY = 0;
		this.destX = 0;
		this.destY = 0;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public float getWorldX() {
		return worldX;
	}

	public float getWorldY() {
		return worldY;
	}
	
	public TextureRegion getSprite() {
		if(state == ACTOR_STATE.WALKING) {
			return animations.getWalking(facing).getKeyFrame(walkTimer);
		} else if(state == ACTOR_STATE.STANDING) {
			return animations.getStanding(facing);
		}
		return animations.getStanding(DIRECTION.NORTH);
	}
}
