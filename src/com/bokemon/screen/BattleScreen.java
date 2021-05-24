package com.bokemon.screen;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bokemon.Bokemon;
import com.bokemon.Settings;
import com.bokemon.battle.BATTLE_STATE;
import com.bokemon.battle.BattleEvent;
import com.bokemon.battle.BattleEvent.EVENT_TYPE;
import com.bokemon.battle.BattleProgressor;
import com.bokemon.battle.HealthBar;
import com.bokemon.battle.Platform;
import com.bokemon.battle.SELECTED;
import com.bokemon.battle.XpBar;
import com.bokemon.controller.BattleController;
import com.bokemon.model.Camera;
import com.bokemon.model.pokemon.Pokemon;
import com.bokemon.model.pokemon.Pokemon_Sprites;
import com.bokemon.model.pokemon.TYPE;
import com.bokemon.model.pokemon.move.Move;
import com.bokemon.model.world.AVAILABLE_LEVELS;
import com.bokemon.util.Jukebox;
import com.bokemon.util.Pokemon_Data;

public class BattleScreen extends AbstractScreen {
	private Camera camera;
	private SpriteBatch batch;
	public TextureAtlas atlas;
	private TextureAtlas uiAtlas;
	public BattleController controller;
	
	private JSONObject ref;
	private JSONObject typeRef;
	private JSONObject moveRef;
	
	public BitmapFont main = new BitmapFont();
	private BitmapFont sub = new BitmapFont();
	private BitmapFont sub2 = new BitmapFont();
	private BitmapFont name = new BitmapFont();
	
	public char[] targetDialog;
	public char[] targetDialogSpaced;
	public String targetDialogStr;
	public String currentDialog;
	public float dialogTimer = 0;
	public float relativeDialogTimer = 0;
	
	public boolean hpChange = false;
	public boolean enemyHpChange = false;
	public boolean textChanging = false;
	
	public Queue<BattleEvent> queue = new LinkedList<BattleEvent>();
	public BattleEvent nextEvent = queue.peek();
	
	public BATTLE_STATE state;
	public BATTLE_STATE nextState;
	public SELECTED selected;
	public BattleProgressor progressor;
	
	public Pokemon_Sprites sprites = new Pokemon_Sprites();
	public ArrayList<Pokemon> party;
	public Pokemon activePokemon;
	public int activePokemonNum = 0;
	public Pokemon enemy;
	private TextureRegion background;
	private TextureRegion allyPokemonTexture;
	private TextureRegion enemyPokemonTexture;
	
	public TYPE type;
	public ArrayList<String> enemyWeaknesses;
	public ArrayList<String> enemyStrengths;
	public ArrayList<String> allyWeaknesses;
	public ArrayList<String> allyStrengths;
	
	private Platform enemyPlatform;
	private Platform allyPlatform;
	
	public int pokeballShakes = 0;
	
	public XpBar xpBar;
	public HealthBar allyHealthBar;
	public float displayHp;
	public HealthBar enemyHealthBar;
	
	private boolean battleTimerStarted;
	public int battleTimer;
	public boolean enemyHitEffect;
	public boolean allyHitEffect;
	public boolean enemyFlashEffect;
	public boolean allyFlashEffect;
	public String queuedSound;
	
	private boolean loaded = false;
	
	
	public BattleScreen(Bokemon app, String enemyName, boolean isWild) {
		super(app);
		
		battleTimerStarted = false;
		battleTimer = 0;
		
		toggleTimer(true);
		
		enemyHitEffect = false;
		allyHitEffect = false;
		enemyFlashEffect = false;
		allyFlashEffect = false;
		
		
		ref = Pokemon_Data.get("pokemon");
		typeRef = Pokemon_Data.get("types");
		moveRef = Pokemon_Data.get("moves");
		
		this.enemy = new Pokemon(ref.getJSONObject("KADABRA"));
		
		System.out.print("******************" + enemyName);

		if(isWild) {
			enemy.setLevel( (int) (Math.random() * (AVAILABLE_LEVELS.PALLET_TOWN.getMax() - AVAILABLE_LEVELS.PALLET_TOWN.getMin()) ) + AVAILABLE_LEVELS.PALLET_TOWN.getMin() );
			enemy.setMoveSet(buildWildMoveSet(enemy));
			System.out.println(enemy.getMoveSet());
		}
		enemy.updateValues();
		enemy.setHp(enemy.getMaxHp());
		
		controller = new BattleController(this);
		atlas = sprites.atlas;
		uiAtlas = sprites.manager.get("res/graphics_packed/ui/ui_textures.atlas");
		
		party = buildParty();
		activePokemon = party.get(0);
		
		enemyWeaknesses = enemy.getWeaknesses(typeRef, enemy.getTypes());
		enemyStrengths = enemy.getStrengths(typeRef, enemy.getTypes());
		allyWeaknesses = activePokemon.getWeaknesses(typeRef, activePokemon.getTypes());
		allyStrengths = activePokemon.getStrengths(typeRef, activePokemon.getTypes());
		
		allyPokemonTexture = atlas.findRegion("pokemon_back_sprites/" + activePokemon.getId());
		enemyPokemonTexture = atlas.findRegion("pokemon_sprites/" + enemy.getId());
		
		this.state = BATTLE_STATE.INIT;
		this.selected = SELECTED.OPTION_1;
		selected.constructRelations();
		this.progressor = new BattleProgressor(this);

		
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
		
		
		main.getData().setScale(4f);
		main.getData().setLineHeight(20f);
		main.setColor(Color.DARK_GRAY);
		sub.getData().setScale(4.5f);
		sub.setColor(Color.DARK_GRAY);
		sub2.getData().setScale(3.5f);
		sub2.setColor(Color.DARK_GRAY);
		name.getData().setScale(enemy.getName().length() < 12 ? 4f : 3f);
		name.setColor(Color.DARK_GRAY);
		
		targetDialogStr = "A wild " + enemy.getName() + " appeared!";
		targetDialog = targetDialogStr.toCharArray();
		targetDialogSpaced = progressor.getSpaced(targetDialog);
		currentDialog = "";
		textChanging = true;
		System.out.println(state.toString());
		queue.add(new BattleEvent(this, "Go, " + activePokemon.getName() + "!", EVENT_TYPE.DIALOG));
		queue.add(new BattleEvent(this, "What should \n" + activePokemon.getName() + " do?", EVENT_TYPE.CHANGE_STATE));
		
		for(int i = 0; i < enemy.getMoveSet().size(); i++) {
			System.out.println(enemy.getMoveSet().get(i).getName());
		}
	}
	public void toggleTimer(Boolean starting) {
		if(starting) {
			battleTimerStarted = true;
		} else {
			battleTimer = 0;
			battleTimerStarted = false;
		}
	}
	private ArrayList<Pokemon> buildParty() {
		ArrayList<Pokemon> output = new ArrayList<Pokemon>();
		for(int i = 1; i <= 6; i++) {
			String name = Bokemon.prefs.getString(String.format("poke%s", i), null);
			if(name != null) {
				Pokemon toAdd = new Pokemon(ref.getJSONObject(name));
				
				toAdd.setMoveSet(buildMoveSet(true, i));
				toAdd.setLevel(Integer.valueOf(Bokemon.prefs.getString(String.format("poke%s_lv", i), "4")));
				toAdd.updateValues();
				toAdd.setHp(Integer.valueOf(Bokemon.prefs.getString(String.format("poke%s_hp", i), String.valueOf(toAdd.getMaxHp()))));
				output.add(toAdd);
			}
		}
		return output;
	}
	private ArrayList<Move> buildMoveSet(Boolean ally, int pokemon) {
		ArrayList<Move> output = new ArrayList<Move>();
		
		for(int i = 1; i <= 4; i++) {
			String moveName = Bokemon.prefs.getString(String.format("poke%s_mv%s", pokemon, i), null);
			if(moveName == null) {
				continue;
			}
			Move move = new Move(moveRef.getJSONObject(moveName));
			move.setPp(Bokemon.prefs.getInteger(String.format("poke%s_mv%s_pp", pokemon, i), move.getMax_pp()));
			
			output.add(move);
		}
		
		if(output.size() < 1) {
			output.add(new Move(moveRef.getJSONObject("TACKLE")));
		}
		
		return output;
	}
	public ArrayList<Move> buildWildMoveSet(Pokemon enemy) {
		ArrayList<Move> output = new ArrayList<Move>();
		ArrayList<String> possible = enemy.getPossibleMoves();
		System.out.println("POSSIBLE: " + String.valueOf(possible.size()));
		
		int i = 0;
		while(i <= possible.size() && i < 4) {
			int rand = (int) Math.random() * possible.size();
			String moveName = possible.get(rand).toUpperCase();
			System.out.println(moveName);
			
			Move move = new Move(moveRef.getJSONObject(moveName));
			possible.remove(rand);
			move.setPp(move.getMax_pp());
			
			output.add(move);
			System.out.println(i);
			i++;
		}
		
		return output;
	}
	public void update(Pokemon active) { //SAFE
		this.activePokemon = active;
		activePokemon.updateValues();
		
		displayHp = activePokemon.getHp();
		allyHealthBar.setSizeX((int) (HealthBar.max * (activePokemon.getHpPercentage() / 100)) );
		
		allyPokemonTexture = atlas.findRegion("pokemon_back_sprites/" + activePokemon.getId());
	}

	
	public void endBattle() {
		Jukebox.current.dispose();
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
			if(BattleEvent.current != null) {
				controller.lockFull();
				if(BattleEvent.current.getType() == BattleEvent.EVENT_TYPE.HEALTH_ANIM_ALLY ||
				   BattleEvent.current.getType() == BattleEvent.EVENT_TYPE.HEALTH_ANIM_ENEMY ||
				   BattleEvent.current.getType() == BattleEvent.EVENT_TYPE.DELAY_HIT_ENEMY ||
				   BattleEvent.current.getType() == BattleEvent.EVENT_TYPE.DELAY_HIT_ALLY ||
				   BattleEvent.current.getType() == BattleEvent.EVENT_TYPE.DELAY) {
					queue.peek().init();
				}
			} else {
				controller.unlock();
			}
			if(queue.isEmpty() && progressor.awaitingAttack && textChanging == false && enemy.getHp() > 0 && activePokemon.getHp() > 0) {
				if(progressor.allyAwaitingAttack) {
					progressor.attackPokemon(activePokemon, enemy, progressor.awaitingMove);
				} else {
					progressor.attackPokemon(enemy, activePokemon, progressor.awaitingMove);
				}
				progressor.awaitingAttack = false;
			}
			batch.begin();
			if(battleTimerStarted) {
				battleTimer++;
			}
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
						this.loaded = true;
						this.show();
					}
			if(!enemyFlashEffect) {
				batch.draw(enemyPokemonTexture,
						enemyHitEffect ? (float) (enemyPlatform.getX() + 1.5*Settings.SCALED_TILE_SIZE) : enemyPlatform.getX() + 1*Settings.SCALED_TILE_SIZE,
						((float) (4.3 * enemyPlatform.getSizeY()) / Settings.SCALED_TILE_SIZE)*Settings.SCALED_TILE_SIZE,
						enemy.getSize(),
						enemy.getSize());
			}
			if(this.enemyHitEffect) {
				batch.draw(atlas.findRegion("hit"),
						enemyPlatform.getX() + (float) 1.2*Settings.SCALED_TILE_SIZE,
						((float) (5.3 * enemyPlatform.getSizeY()) / Settings.SCALED_TILE_SIZE)*Settings.SCALED_TILE_SIZE,
						100,
						93);
			}
			
			batch.draw(atlas.findRegion("platform"),  //PLAYER
					allyPlatform.getX(),
					allyPlatform.getY(),
					allyPlatform.getSizeX(), 
					allyPlatform.getSizeY());
					if(allyPlatform.getX() > 12*Settings.SCALED_TILE_SIZE) {
						allyPlatform.setX(allyPlatform.getX() - platformDelta*(speed + 210));
					}
			if(this.loaded) {
				if(textChanging) {
					if(currentDialog.length() < targetDialogSpaced.length) {
						currentDialog = currentDialog + targetDialogSpaced[currentDialog.length()];
					} else {
						textChanging = false;
					}
				}
				if(!allyFlashEffect) {
					batch.draw(allyPokemonTexture,
							allyPlatform.getX() + (allyHitEffect ? (float) 0.5*Settings.SCALED_TILE_SIZE : 1*Settings.SCALED_TILE_SIZE),
							6*Settings.SCALED_TILE_SIZE,
							activePokemon.getSize() + (3*Settings.SCALED_TILE_SIZE),
							activePokemon.getSize() + (3*Settings.SCALED_TILE_SIZE));
				}
				if(this.allyHitEffect) {
					batch.draw(atlas.findRegion("hit"),
							allyPlatform.getX() + (float) 8*Settings.SCALED_TILE_SIZE,
							((float) (3.4 * allyPlatform.getSizeY()) / Settings.SCALED_TILE_SIZE)*Settings.SCALED_TILE_SIZE,
							100,
							93);
				}
			}
			
			batch.draw(uiAtlas.findRegion("dialoguebox"),
					(float) ((Settings.WINDOW_X / 2) - (Settings.WINDOW_X * 0.8 / 2)),
					(float) 30,
					(float) (Settings.WINDOW_X * 0.8),
					(float) 400);
			main.draw(batch, 
				currentDialog, 
				(float) 6.5*Settings.SCALED_TILE_SIZE, 
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
			}
			if(this.state == BATTLE_STATE.QUESTION || this.state == BATTLE_STATE.QUESTION_ATTACK) {
				batch.draw(uiAtlas.findRegion("arrow"),
						this.selected.getX() * Settings.SCALED_TILE_SIZE,
						this.selected.getY() * Settings.SCALED_TILE_SIZE,
						40,
						40);
			}
			if(this.state == BATTLE_STATE.QUESTION) {
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
			} else if(this.state == BATTLE_STATE.QUESTION_ATTACK) {
				for(int i = 0; i < activePokemon.getMoveSet().size(); i++) {
					if(i == 0) {
						sub.draw(batch, 
							activePokemon.getMoveSet().get(0).getName(), 
							8*Settings.SCALED_TILE_SIZE, 
							(float) 5.5*Settings.SCALED_TILE_SIZE);
					}
					if(i == 1) {
						sub.draw(batch, 
							activePokemon.getMoveSet().get(1).getName(), 
							17*Settings.SCALED_TILE_SIZE, 
							(float) 5.5*Settings.SCALED_TILE_SIZE);
					}
					if(i == 2) {
						sub.draw(batch, 
							activePokemon.getMoveSet().get(2).getName(), 
							8*Settings.SCALED_TILE_SIZE, 
							(float) 3.5*Settings.SCALED_TILE_SIZE);
					}
					if(i == 3) {
						sub.draw(batch, 
							activePokemon.getMoveSet().get(3).getName(), 
							17*Settings.SCALED_TILE_SIZE, 
							(float) 3.5*Settings.SCALED_TILE_SIZE);
					}
				}
				sub2.draw(batch, 
					String.format("PP: %s/%s", activePokemon.getMoveSet().get(selected.getNum() - 1).getPp(), activePokemon.getMoveSet().get(selected.getNum() - 1).getMax_pp()), 
					29*Settings.SCALED_TILE_SIZE, 
					(float) 5.5*Settings.SCALED_TILE_SIZE);
				sub2.draw(batch, 
					String.format("TYPE:"), 
					29*Settings.SCALED_TILE_SIZE, 
					(float) 3.5*Settings.SCALED_TILE_SIZE);
				batch.draw(uiAtlas.findRegion("type_icons/" + activePokemon.getMoveSet().get(selected.getNum() - 1).getType().toString().toLowerCase()),
						(float) 32.2*Settings.SCALED_TILE_SIZE,
						(float) 2.42*Settings.SCALED_TILE_SIZE,
						60,
						60);
			}
			
			batch.end();
		
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
