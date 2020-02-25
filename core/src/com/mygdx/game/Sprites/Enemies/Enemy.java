package com.mygdx.game.Sprites.Enemies;

import com.mygdx.game.Sprites.Player;
import com.mygdx.game.Tools.Drawable;
import com.mygdx.game.Tools.Updatable;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Screens.PlatScreen;

public abstract class Enemy extends Sprite implements Updatable, Drawable, Telegraph {
    protected World world;
    protected TiledMap map;
    protected PlatScreen screen;
    protected Body body;
    protected float stateTimer = 0;


    public Enemy(PlatScreen screen){
        this.screen = screen;
        world = screen.getWorld();
        map = screen.getMap();
    }

    abstract protected TextureRegion getFrame(float dt);
    abstract protected void hitON();
    abstract protected void defineEnemy(Rectangle bounds);
    abstract protected void defineAnimations();
    abstract public void attack(Player player);


}
