package com.bokemon.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.bokemon.Bokemon;
import com.bokemon.Settings;
import com.bokemon.controller.PlayerController;
import com.bokemon.model.Actor;
import com.bokemon.model.Camera;
import com.bokemon.model.building.Door;
import com.bokemon.model.building.House;
import com.bokemon.model.world.TERRAIN;
import com.bokemon.model.world.TallGrassPatch;
import com.bokemon.model.world.TileMap;
import com.bokemon.util.AnimationSet;

public class GameScreen extends AbstractScreen {
	private Actor player;
	private House house1;
	private Door house1Door;
	private TileMap map;
	@SuppressWarnings("unused")
	private TallGrassPatch tallGrassPatch;
	private PlayerController controller;
	private Camera camera;
	public  SpriteBatch batch;
	private Texture grass1;
	private Texture grass2;
	private Texture tallGrassTexture;
	private Texture houseTexture1;
	private Texture doorTexture1;

	public GameScreen(Bokemon app) {
		super(app);
		
		grass1 = new Texture("res/graphics_unpacked/tiles/grass1.png");
		grass2 = new Texture("res/graphics_unpacked/tiles/grass2.png");
		tallGrassTexture = new Texture("res/graphics_unpacked/tiles/tall_grass.png");
		houseTexture1 = new Texture("res/graphics_unpacked/tiles/small_house.png");
		doorTexture1 = new Texture("res/graphics_unpacked/tiles/woodenDoor_0.png");
		
		batch = new SpriteBatch();
		
		TextureAtlas atlas = app.getAssetManager().get("res/graphics_packed/tiles/tile_textures.atlas", TextureAtlas.class);	
		AnimationSet animations = new AnimationSet(
				new Animation(0.5f / 2.6f, atlas.findRegions("brendan_walk_north"), PlayMode.LOOP_PINGPONG),
				new Animation(0.5f / 2.6f, atlas.findRegions("brendan_walk_south"), PlayMode.LOOP_PINGPONG),
				new Animation(0.5f / 2.6f, atlas.findRegions("brendan_walk_east"), PlayMode.LOOP_PINGPONG),
				new Animation(0.5f / 2.6f, atlas.findRegions("brendan_walk_west"), PlayMode.LOOP_PINGPONG),
				atlas.findRegion("brendan_stand_north"),
				atlas.findRegion("brendan_stand_south"),
				atlas.findRegion("brendan_stand_east"),
				atlas.findRegion("brendan_stand_west"));
		
		map = new TileMap(20, 20 );
		
		house1 = new House(2, 10*Settings.SCALED_TILE_SIZE, 5*Settings.SCALED_TILE_SIZE, 5*Settings.SCALED_TILE_SIZE, 5*Settings.SCALED_TILE_SIZE, 4*Settings.SCALED_TILE_SIZE, 4*Settings.SCALED_TILE_SIZE, houseTexture1, map);
		house1Door = new Door(house1, (float) 0.5,  13*Settings.SCALED_TILE_SIZE, 5*Settings.SCALED_TILE_SIZE, Settings.SCALED_TILE_SIZE, Settings.SCALED_TILE_SIZE, doorTexture1, map);
		
		tallGrassPatch = new TallGrassPatch(9, 3, 6, 4, map);
		
		player = new Actor(map, Bokemon.prefs.getInteger("playerX", 2), Bokemon.prefs.getInteger("playerY", 10), animations, this);
		
		controller = new PlayerController(player);
		
		camera = new Camera();
		
		
	}

	@Override
	public void dispose() {
		batch.dispose();
		grass1.dispose();
		grass2.dispose();
		houseTexture1.dispose();
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		
	}
	@Override
	public void render(float delta) {
		controller.update(delta);
		
		player.update(delta);
		camera.update(player.getWorldX()+0.5f, player.getWorldY()+0.5f);
		
		batch.begin();
		
		float worldStartX = Gdx.graphics.getWidth() / 2 - camera.getCameraX() * Settings.SCALED_TILE_SIZE;
		float worldStartY = Gdx.graphics.getHeight() / 2 - camera.getCameraY() * Settings.SCALED_TILE_SIZE;
		
		for(int x = 0; x < map.getWidth(); x++) {
			for(int y=0; y < map.getHeight(); y++) {
				Texture render;
				if(map.getTile(x, y).getTerrain() == TERRAIN.GRASS_1) {
					render = grass1;
				} else {
					render = grass2;
				}
				batch.draw(render,
						worldStartX+x*Settings.SCALED_TILE_SIZE,
						worldStartY+y*Settings.SCALED_TILE_SIZE,
						Settings.SCALED_TILE_SIZE,
						Settings.SCALED_TILE_SIZE);
				if(map.getTile(x, y).getTerrain() == TERRAIN.TALL_GRASS && map.getTile(x, y).getObject() == null) {
					batch.draw(tallGrassTexture,
							worldStartX+x*Settings.SCALED_TILE_SIZE,
							worldStartY+y*Settings.SCALED_TILE_SIZE,
							Settings.SCALED_TILE_SIZE,
							Settings.SCALED_TILE_SIZE);
				}
			}
		}
		
		batch.draw(player.getSprite(), 
				worldStartX+player.getWorldX() * Settings.SCALED_TILE_SIZE, 
				worldStartY+player.getWorldY() * Settings.SCALED_TILE_SIZE, 
				Settings.SCALED_TILE_SIZE, 
				Settings.SCALED_TILE_SIZE *1.5f);
		
		batch.draw(doorTexture1,
				worldStartX+house1Door.getX(),
				worldStartY+house1Door.getY(),
				house1Door.getSizeX(),
				house1Door.getSizeY() * 2);
		batch.draw(houseTexture1,
				worldStartX+house1.getX(),
				worldStartY+house1.getY(),
				house1.getSizeX(),
				house1.getSizeY());
		
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
		Gdx.input.setInputProcessor(controller);
	}

}
