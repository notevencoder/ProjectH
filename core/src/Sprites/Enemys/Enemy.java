package Sprites.Enemys;

import Tools.Drawable;
import Tools.Updatable;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Screens.PlatScreen;

public abstract class Enemy extends Sprite implements Updatable, Drawable {
    protected World world;
    protected TiledMap map;
    protected PlatScreen screen;
    protected Body body;
    protected float stateTimer = 0;

    abstract protected TextureRegion getFrame(float dt);
    abstract protected void hitON();
    abstract protected void defineEnemy(Rectangle bounds);
    abstract protected void defineAnimations();


}
