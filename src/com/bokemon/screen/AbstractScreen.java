package com.bokemon.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.bokemon.Bokemon;

public abstract class AbstractScreen implements Screen {
	
	private Bokemon app;

	public AbstractScreen(Bokemon app) {
		this.app = app;
	}
	
	@Override
	public abstract void dispose();

	@Override
	public abstract void hide();

	@Override
	public abstract void pause();

	@Override
	public abstract void render(float delta);

	@Override
	public abstract void resize(int width, int height);

	@Override
	public abstract void resume();

	@Override
	public abstract void show();
	
	public void initTransition(AbstractScreen from, AbstractScreen to) {
		app.setScreen(to);
	}

	public Bokemon getApp() {
		return app;
	}


}
