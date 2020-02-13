package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Screens.MainMenu;

public class MyGdxGame extends Game {
	public SpriteBatch batch;

	public final static int V_WIDTH = 1024;
	public final static int V_HEIGHT = 768;


	@Override
	public void create () {
		 batch = new SpriteBatch();
		 setScreen(new MainMenu(this));
	}

	@Override
	public void render () {
		super.render();

	}
	
	@Override
	public void dispose () {

		batch.dispose();
	}
}
