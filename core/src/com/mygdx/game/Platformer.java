package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Screens.PlatScreen;

public class Platformer extends Game {
	public SpriteBatch batch;

	public final static int V_WIDTH = 384;
	public final static int V_HEIGHT = 544;
	public final static float PPM = 100;

	public final static short DEFAULT_BIT = 1;
	public final static short PLAYER_BIT = 2;
	public final static short PLATFORM_BIT = 4;
	public final static short DOOR_BIT = 8;
	public final static short ENEMY_BIT = 16;



	@Override
	public void create () {
		 batch = new SpriteBatch();
		 setScreen(new PlatScreen(this));
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
