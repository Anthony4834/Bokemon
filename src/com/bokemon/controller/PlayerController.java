package com.bokemon.controller;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.bokemon.model.Actor;
import com.bokemon.model.DIRECTION;
import com.bokemon.screen.GameScreen;

public class PlayerController extends InputAdapter {
	
	private Actor player;
	private GameScreen screen;
	private Boolean frozen = false;
	
	private boolean up, down, left, right;
	
	public PlayerController(Actor p, GameScreen s) {
		this.player = p;
		this.screen = s;
	}

	@Override
	public boolean keyDown(int keycode) {
		if(this.isFrozen()) {
			return false;
		}
		if(keycode == Keys.W) {
			up = true;
		}
		if(keycode == Keys.S) {
			down = true;
		}
		if(keycode == Keys.A) {
			left = true;
		}
		if(keycode == Keys.D) {
			right = true;
		}
		
		return false;
	}
	@Override
	public boolean keyUp(int keycode) {
		if(this.isFrozen()) {
			return false;
		}
		if(keycode == Keys.W) {
			up = false;
		}
		if(keycode == Keys.S) {
			down = false;
		}
		if(keycode == Keys.A) {
			left = false;
		}
		if(keycode == Keys.D) {
			right = false;
		}
		
		return false;
	}
	public void update(float delta) {
		if(this.isFrozen()) {
			return;
		}
		if(up) {
			player.move(DIRECTION.NORTH);
			return;
		}
		if(down) {
			player.move(DIRECTION.SOUTH);
			return;
		}
		if(left) {
			player.move(DIRECTION.WEST);
			return;
		}
		if(right) {
			player.move(DIRECTION.EAST);
			return;
		}
	}
	public void freeze() {
		this.frozen = true;
	}
	public Boolean isFrozen() {
		return this.frozen;
	}
	public Actor getPlayer() {
		return player;
	}
	
}
