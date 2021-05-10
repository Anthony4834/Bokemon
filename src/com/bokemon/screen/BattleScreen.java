package com.bokemon.screen;

import java.util.ArrayList;

import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bokemon.Bokemon;
import com.bokemon.Settings;
import com.bokemon.battle.BATTLE_STATE;
import com.bokemon.battle.HealthBar;
import com.bokemon.battle.Platform;
import com.bokemon.battle.SELECTED;
import com.bokemon.battle.XpBar;
import com.bokemon.controller.BattleController;
import com.bokemon.model.Camera;
import com.bokemon.model.pokemon.Capture_Calculator;
import com.bokemon.model.pokemon.Pokemon;
import com.bokemon.model.pokemon.Pokemon_Sprites;
import com.bokemon.model.world.AVAILABLE_LEVELS;
import com.bokemon.util.Pokemon_Data;

public class BattleScreen extends AbstractScreen {
	private Camera camera;
	private SpriteBatch batch;
	public TextureAtlas atlas;
	private TextureAtlas uiAtlas;
	private BattleController controller;
	private JSONObject ref;
	private BitmapFont main = new BitmapFont();
	private BitmapFont sub = new BitmapFont();
	private BitmapFont name = new BitmapFont();
	private String currentDialog = "";
	public BATTLE_STATE state;
	public SELECTED selected;
	public boolean hpChange = false;
	public boolean enemyHpChange = false;
	private Preferences pD = Bokemon.pokemon_data;
	
	public Pokemon_Sprites sprites = new Pokemon_Sprites();
	private ArrayList<Pokemon> party;
	private Pokemon activePokemon;
	private int activePokemonNum = 0;
	private Pokemon enemy;
	private TextureRegion background;
	private TextureRegion allyPokemonTexture;
	private TextureRegion enemyPokemonTexture;
	
	private Platform enemyPlatform;
	private Platform allyPlatform;
	
	private int pokeballShakes = 0;
	
	private XpBar xpBar;
	private HealthBar allyHealthBar;
	private float displayHp;
	private HealthBar enemyHealthBar;
	
	private boolean loaded = false;
	
	
	public BattleScreen(Bokemon app, String enemyName, boolean isWild) {
		super(app);
		ref = Pokemon_Data.get();
		
		JSONObject enemyInfo = ref.getJSONObject("SKARMORY");
		this.enemy = new Pokemon(enemyInfo.getJSONObject("ORIGIN_NAME").get("value").toString(), 
				Integer.valueOf(enemyInfo.getJSONObject("ID").get("value").toString()),
				Integer.valueOf(enemyInfo.getJSONObject("BASE_HP").get("value").toString()), 
				Integer.valueOf(enemyInfo.getJSONObject("BASE_ATK").get("value").toString()), 
				Integer.valueOf(enemyInfo.getJSONObject("BASE_DEF").get("value").toString()),
				Integer.valueOf(enemyInfo.getJSONObject("BASE_SPATK").get("value").toString()),
				Integer.valueOf(enemyInfo.getJSONObject("BASE_SPDEF").get("value").toString()),
				Integer.valueOf(enemyInfo.getJSONObject("BASE_SPD").get("value").toString()),
				Integer.valueOf(enemyInfo.getJSONObject("CAPTURE_RATE").get("value").toString()),
				Integer.valueOf(enemyInfo.getJSONObject("SIZE").get("value").toString()));

		if(isWild) {
			enemy.setLevel( (int) (Math.random() * (AVAILABLE_LEVELS.PALLET_TOWN.getMax() - AVAILABLE_LEVELS.PALLET_TOWN.getMin()) ) + AVAILABLE_LEVELS.PALLET_TOWN.getMin() );
		}
		enemy.updateValues();
		enemy.setHp(enemy.getMaxHp());
		
		controller = new BattleController(this);
		atlas = sprites.atlas;
		uiAtlas = sprites.manager.get("res/graphics_packed/ui/ui_textures.atlas");
		
		party = buildParty();
		activePokemon = party.get(0);
		
		allyPokemonTexture = atlas.findRegion("pokemon_back_sprites/" + activePokemon.getId());
		enemyPokemonTexture = atlas.findRegion("pokemon_sprites/" + enemy.getId());
		
		loaded = true;
		
		this.state = BATTLE_STATE.INIT;
		this.selected = SELECTED.ATTACK;

		
		batch = new SpriteBatch();
		background = atlas.findRegion("background");
		enemyPlatform = new Platform(0, 18);
		enemyHealthBar = new HealthBar(0, 0);
		enemyHealthBar.setSizeX((float) (HealthBar.max * (enemy.getHpPercentage() / 100)));
		enemyHealthBar.setSizeY(68);
		enemyHealthBar.colorCheck();
		
		
		allyPlatform = new Platform(Settings.WINDOW_X / Settings.SCALED_TILE_SIZE, 8);
		xpBar = new XpBar(0, 0);
		xpBar.setSizeX(XpBar.max);
		xpBar.set(uiAtlas.findRegion("bar_cyan"));
		allyHealthBar = new HealthBar(0, 0);
		allyHealthBar.setSizeX((float) (HealthBar.max * (activePokemon.getHpPercentage() / 100)));
		allyHealthBar.colorCheck();
		displayHp = activePokemon.getHp();
		
		
		main.getData().setScale(4.5f);
		main.getData().setLineHeight(20f);
		main.setColor(Color.DARK_GRAY);
		sub.getData().setScale(4.5f);
		sub.setColor(Color.DARK_GRAY);
		name.getData().setScale(enemy.getName().length() < 12 ? 4f : 3f);
		name.setColor(Color.DARK_GRAY);
		
		currentDialog = "A wild " + enemy.getName() + " appeared!";

	}
	private ArrayList<Pokemon> buildParty() {
		ArrayList<Pokemon> output = new ArrayList<Pokemon>();
		for(int i = 1; i <= 6; i++) {
			String name = Bokemon.prefs.getString(String.format("poke%s", i), null);
			if(name != null) {
				System.out.println(name);
				JSONObject info = ref.getJSONObject(name);
				Pokemon toAdd = new Pokemon(info.getJSONObject("ORIGIN_NAME").get("value").toString(),
						Integer.valueOf(info.getJSONObject("ID").get("value").toString()),
						Integer.valueOf(info.getJSONObject("BASE_HP").get("value").toString()), 
						Integer.valueOf(info.getJSONObject("BASE_ATK").get("value").toString()), 
						Integer.valueOf(info.getJSONObject("BASE_DEF").get("value").toString()),
						Integer.valueOf(info.getJSONObject("BASE_SPATK").get("value").toString()),
						Integer.valueOf(info.getJSONObject("BASE_SPDEF").get("value").toString()),
						Integer.valueOf(info.getJSONObject("BASE_SPD").get("value").toString()),
						Integer.valueOf(info.getJSONObject("CAPTURE_RATE").get("value").toString()),
						Integer.valueOf(info.getJSONObject("SIZE").get("value").toString()));
				
				toAdd.setLevel(Integer.valueOf(Bokemon.prefs.getString(String.format("poke%s_lv", i), "4")));
				toAdd.updateValues();
				toAdd.setHp(Integer.valueOf(Bokemon.prefs.getString(String.format("poke%s_hp", i), String.valueOf(toAdd.getMaxHp()))));
				toAdd.printLevels();
				output.add(toAdd);
			}
		}
		return output;
	}
	public void healthAnim() {
		float hpBarDelta = Gdx.graphics.getDeltaTime();
		float speed = 180;
		float dSizeX;
		float dHp;
		
		if(allyHealthBar.getSizeX() < HealthBar.max * (activePokemon.getHpPercentage() / 100)) {
			dSizeX = allyHealthBar.getSizeX() + hpBarDelta*speed;
			dHp = displayHp + hpBarDelta * 21;
			if(dSizeX >= HealthBar.max * (activePokemon.getHpPercentage() / 100)) {
				displayHp = activePokemon.getHp();
				allyHealthBar.setSizeX((float) (HealthBar.max * (activePokemon.getHpPercentage() / 100)) );
				this.hpChange = false;
			} else {
				displayHp = dHp > activePokemon.getHp() ? activePokemon.getHp() : dHp;
				allyHealthBar.setSizeX(dSizeX);
			}
		} else {
			dSizeX = allyHealthBar.getSizeX() - hpBarDelta*speed;
			dHp = displayHp - hpBarDelta * 21;
			if(dSizeX <= HealthBar.max * (activePokemon.getHpPercentage() / 100)) {
				displayHp = activePokemon.getHp();
				allyHealthBar.setSizeX((float) (HealthBar.max * (activePokemon.getHpPercentage() / 100)) );
				this.hpChange = false;
			} else {
				displayHp = dHp < activePokemon.getHp() ? activePokemon.getHp() : dHp;
				allyHealthBar.setSizeX(dSizeX);
			}
		}
		allyHealthBar.colorCheck();
	}
	public void enemyHealthAnim() {
		float hpBarDelta = Gdx.graphics.getDeltaTime();
		float speed = 180;
		float dSizeX;
		
		if(enemyHealthBar.getSizeX() < HealthBar.max * (enemy.getHpPercentage() / 100)) {
			dSizeX = enemyHealthBar.getSizeX() + hpBarDelta*speed;
			if(dSizeX >= HealthBar.max * (enemy.getHpPercentage() / 100)) {
				enemyHealthBar.setSizeX((float) (HealthBar.max * (enemy.getHpPercentage() / 100)) );
				this.enemyHpChange = false;
			} else {
				enemyHealthBar.setSizeX(dSizeX);
			}
		} else {
			dSizeX = enemyHealthBar.getSizeX() - hpBarDelta*speed;
			if(dSizeX <= HealthBar.max * (enemy.getHpPercentage() / 100) || dSizeX < 0) {
				enemyHealthBar.setSizeX((float) (HealthBar.max * (enemy.getHpPercentage() / 100)) );
				this.enemyHpChange = false;
				updateDialog(enemy.getHp() > 0 ? BATTLE_STATE.QUESTION : BATTLE_STATE.FAINT_ENEMY);
			} else {
				enemyHealthBar.setSizeX(dSizeX);
			}
		}
		enemyHealthBar.colorCheck();
	}
	public void update(Pokemon active) { //SAFE
		this.activePokemon = active;
		allyPokemonTexture = atlas.findRegion("pokemon_back_sprites/" + activePokemon.getId());
		this.hpChange = true;
		healthAnim();
	}
	public void updateDialog(BATTLE_STATE to) {
		switch(to) {
			case QUESTION:
				this.currentDialog = "What should \n" + this.activePokemon.getName().toUpperCase() + " do?";
				this.state = BATTLE_STATE.QUESTION;
				break;
			case ATTACK:
				this.currentDialog = this.activePokemon.getName().toUpperCase() + " used \nTACKLE";
				this.state = BATTLE_STATE.ATTACK;
				break;
			case COME_BACK:
				this.currentDialog = "Come back " + this.activePokemon.getName().toUpperCase() + "!";
				this.state = BATTLE_STATE.COME_BACK;
				break;
			case GO:
				this.currentDialog = "Go, " + this.activePokemon.getName().toUpperCase() + "!";
				this.state = BATTLE_STATE.GO;
				break;
			case CAPTURE:
				this.currentDialog = "Player throws a pokeball!";
				this.state = BATTLE_STATE.CAPTURE;
				break;
			case CAPTURE_SUCCESS:
				this.currentDialog = enemy.getName().toUpperCase() + " was caught!";
				this.state = BATTLE_STATE.CAPTURE_SUCCESS;
				break;
			case RUN_AWAY:
				this.currentDialog = "Got away safely!";
				this.state = BATTLE_STATE.RUN_AWAY;
				break;
			case FAINT_ENEMY:
				this.currentDialog = "Enemy " + enemy.getName().toUpperCase() + " has fainted!";
				this.state = BATTLE_STATE.FAINT_ENEMY;
				break;
			case END:
				this.state = BATTLE_STATE.END;
				this.endBattle();
				break;
			default:
				this.currentDialog = "A wild \n" + enemy.getName().toUpperCase() + " attacks!";
		}
	}
	public void attackPokemon() {
		if(this.state == BATTLE_STATE.ATTACK) {
			int attackPower = ((((2 * activePokemon.getLevel()) / 5 + 2) * 40 * (activePokemon.getAtk() / enemy.getDef())) / 50) + 2;
			double rand = Math.random() * (1 - 0.85) + 0.85;
			int dPwr = (int) (attackPower * rand);
			System.out.println(dPwr);
			
			int dHp = enemy.getHp() - dPwr;
			enemy.setHp(dHp > 0 ? dHp : 0);
			enemyHealthAnim();
			enemyHpChange = true;
		} else {
			this.updateDialog(BATTLE_STATE.ATTACK);
		}
	}
	public void switchPokemon() { //SAFE
		if(this.state == BATTLE_STATE.COME_BACK) {
			if(this.activePokemon == party.get(party.size() - 1)) {
				this.activePokemonNum = 0;
				update(party.get(0));
			} else {
				update(party.get(activePokemonNum + 1));
				this.activePokemonNum += 1;
			}
			this.updateDialog(BATTLE_STATE.GO);
		} else {
			this.updateDialog(BATTLE_STATE.COME_BACK);
		}
	}
	public void capturePokemon() {
		int a = Capture_Calculator.genChances(enemy);
		if(Capture_Calculator.attemptCapture(a)) {
			calculateShakes(true, a);
			System.out.println("CAUGHT");
		} else {
			calculateShakes(false, a);
			System.out.println(String.format("FAILED - [%s] shakes", this.pokeballShakes));
		}
//		if(this.state == BATTLE_STATE.CAPTURE) {
//			if(party.size() < 6) {
//				Bokemon.prefs.putString(String.format("poke%s", party.size() + 1), enemy.getName().toUpperCase());
//				System.out.println(Bokemon.prefs.getString(String.format("poke%s", party.size() + 1), null));
//				Bokemon.prefs.flush();
//			}
//			this.updateDialog(BATTLE_STATE.CAPTURE_SUCCESS);
//		} else {
//			System.out.println(party.size());
//			this.updateDialog(BATTLE_STATE.CAPTURE);
//		}
	}
	public void calculateShakes(Boolean caught, int a) {
		int output = 0;
		if(caught) {
			output += 3;
		} else {
			for(int i = 0; i < 3; i++) {
				if(!Capture_Calculator.shake(a)) {
					output++;
				}
			}
		}
		
		this.pokeballShakes = output;
	}
	public void endBattle() {
		this.initTransition(this, new GameScreen(this.getApp()));
	}
	@Override
	public void dispose() {
		batch.dispose();
		main.dispose();
		sub.dispose();
		allyPlatform.getTexture().dispose();
		enemyPlatform.getTexture().dispose();
		background.getTexture().dispose();
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void render(float delta) {
		if(loaded == true) {
			batch.begin();
			
			batch.draw(background, 
					0,
					0,
					Settings.WINDOW_X, 
					Settings.WINDOW_Y);
			batch.draw(atlas.findRegion("platform"), //ENEMY
					enemyPlatform.getX(),
					enemyPlatform.getY(),
					enemyPlatform.getSizeX(), 
					enemyPlatform.getSizeY());
					float platformDelta = Gdx.graphics.getDeltaTime();
					float speed = 800;
					if(enemyPlatform.getX() < 28*Settings.SCALED_TILE_SIZE) {
						enemyPlatform.setX(enemyPlatform.getX() + platformDelta*speed);
					} else {
						this.show();
					}
			batch.draw(enemyPokemonTexture,
					enemyPlatform.getX() + 1*Settings.SCALED_TILE_SIZE,
					((float) (4.3 * enemyPlatform.getSizeY()) / Settings.SCALED_TILE_SIZE)*Settings.SCALED_TILE_SIZE,
					enemy.getSize(),
					enemy.getSize());
			
			batch.draw(atlas.findRegion("platform"),  //PLAYER
					allyPlatform.getX(),
					allyPlatform.getY(),
					allyPlatform.getSizeX(), 
					allyPlatform.getSizeY());
					if(allyPlatform.getX() > 12*Settings.SCALED_TILE_SIZE) {
						allyPlatform.setX(allyPlatform.getX() - platformDelta*(speed + 210));
					}
			if(this.state != BATTLE_STATE.INIT) {
				batch.draw(allyPokemonTexture,
						allyPlatform.getX() + 1*Settings.SCALED_TILE_SIZE,
						6*Settings.SCALED_TILE_SIZE,
						activePokemon.getSize() + (3*Settings.SCALED_TILE_SIZE),
						activePokemon.getSize() + (3*Settings.SCALED_TILE_SIZE));
			}
			
			batch.draw(uiAtlas.findRegion("dialoguebox"),
					(float) ((Settings.WINDOW_X / 2) - (Settings.WINDOW_X * 0.8 / 2)),
					(float) 30,
					(float) (Settings.WINDOW_X * 0.8),
					(float) 400);
			main.draw(batch, 
					currentDialog, 
					8*Settings.SCALED_TILE_SIZE, 
					(float) 5.5*Settings.SCALED_TILE_SIZE);
			batch.draw(uiAtlas.findRegion("battleinfobox_friendly"),
					(float) (allyPlatform.getX() + allyPlatform.getSizeX() + Settings.SCALED_TILE_SIZE),
					(float) (allyPlatform.getY() + allyPlatform.getSizeY() / 1.5),
					800,
					264);
			batch.draw(uiAtlas.findRegion("battleinfobox_enemy"),
					(float) (enemyPlatform.getX() - enemyPlatform.getSizeX() - 4 * Settings.SCALED_TILE_SIZE),
					(float) (enemyPlatform.getY() + enemyPlatform.getSizeY() / 1.4),
					800,
					220);
			/*************************************************** 
			 * 
			 *					 ALLY INFO
			 * 
			 * *************************************************/
			if(state != BATTLE_STATE.INIT) {
				name.draw(batch, 
						activePokemon.getName().toUpperCase(), 
						(float) (allyPlatform.getX() + allyPlatform.getSizeX() + 200),
						(float) (allyPlatform.getY() + (allyPlatform.getSizeY() / 1.5) + 220));
				name.draw(batch, 
						(CharSequence) String.format("%s/%s", (int) displayHp, activePokemon.getMaxHp()), 
						(float) (allyPlatform.getX() + allyPlatform.getSizeX() + 500),
						(float) (allyPlatform.getY() + (allyPlatform.getSizeY() / 1.5) + 100));
				name.draw(batch, 
						(CharSequence) String.format("Lv%s", (int) activePokemon.getLevel()), 
						(float) (allyPlatform.getX() + allyPlatform.getSizeX() + 670),
						(float) (allyPlatform.getY() + (allyPlatform.getSizeY() / 1.5) + 220));
				batch.draw(allyHealthBar.getTexture(), 
						(float) (allyPlatform.getX() + allyPlatform.getSizeX() * 1.695),
						(float) (allyPlatform.getY() + allyPlatform.getSizeY() * 1.2),
						(float) (allyHealthBar.getSizeX()),
						allyHealthBar.getSizeY());
						if(this.hpChange) {
							healthAnim();
						}
				batch.draw(xpBar.get(), 
						(float) (allyPlatform.getX() + allyPlatform.getSizeX() * 1.255),
						(float) (allyPlatform.getY() + allyPlatform.getSizeY() / 1.469),
						(float) (xpBar.getSizeX()),
						xpBar.getSizeY());
			/*************************************************** 
			 * 												   *
			 *					 ENEMY INFO					   *
			 * 												   *
			 * *************************************************/
				name.draw(batch, 
						enemy.getName().toUpperCase(), 
						(float) (enemyPlatform.getX() - enemyPlatform.getSizeX() - 120),
						(float) (enemyPlatform.getY() + enemyPlatform.getSizeY() * 1.65));
				name.draw(batch, 
						(CharSequence) String.format("Lv%s", (int) enemy.getLevel()), 
						(float) (enemyPlatform.getX() - enemyPlatform.getSizeX() + 360),
						(float) (enemyPlatform.getY() + enemyPlatform.getSizeY() * 1.65));
				batch.draw(enemyHealthBar.getTexture(), 
						(float) (enemyPlatform.getX() - enemyPlatform.getSizeX() * 0.744),
						(float) (enemyPlatform.getY() + enemyPlatform.getSizeY() * 0.887),
						(float) (enemyHealthBar.getSizeX()),
						enemyHealthBar.getSizeY());
						if(this.enemyHpChange) {
							enemyHealthAnim();
						}
			}
			if(this.state == BATTLE_STATE.QUESTION) {
				batch.draw(uiAtlas.findRegion("arrow"),
						this.selected.getX() * Settings.SCALED_TILE_SIZE,
						this.selected.getY() * Settings.SCALED_TILE_SIZE,
						40,
						40);
				sub.draw(batch, 
						"ATTACK", 
						24*Settings.SCALED_TILE_SIZE, 
						(float) 5.5*Settings.SCALED_TILE_SIZE);
				sub.draw(batch, 
						"BAG", 
						33*Settings.SCALED_TILE_SIZE, 
						(float) 5.5*Settings.SCALED_TILE_SIZE);
				sub.draw(batch, 
						"POKEMON", 
						24*Settings.SCALED_TILE_SIZE, 
						(float) 3.5*Settings.SCALED_TILE_SIZE);
				sub.draw(batch, 
						"RUN", 
						33*Settings.SCALED_TILE_SIZE, 
						(float) 3.5*Settings.SCALED_TILE_SIZE);
			}
			
			batch.end();
		}
	}
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void resume() {
	}

	@Override
	public void show() {
		if(loaded == true) {
			Gdx.input.setInputProcessor(controller);
		}
	}
	@Override
	public Bokemon getApp() {
		return super.getApp();
	}

}
