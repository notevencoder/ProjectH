package com.mygdx.game.Sprites;

import com.mygdx.game.Tools.Box2DCreator;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Platformer;
import com.mygdx.game.Screens.PlatScreen;

public abstract class InteractiveObjects extends Sprite {
    protected World world;

    protected TiledMap map;
    protected TiledMapTile tile;
    protected static Rectangle bounds;
    protected Body body;
    protected Fixture fixture;
    protected PlatScreen screen;
    public InteractiveObjects(PlatScreen screen, Rectangle bounds){
        this.bounds = bounds;
        this.map = screen.getMap();
        this.world = screen.getWorld();
        this.screen = screen;
        Box2DCreator creator = screen.getBoxCreator();

        body = creator.createStaticBody((bounds.getX() + bounds.getWidth() / 2 )/ Platformer.PPM, (bounds.getY() + bounds.getHeight() / 2) / Platformer.PPM);
        fixture = creator.createSquareFixture(body, (bounds.getWidth() / 2) / Platformer.PPM, (bounds.getHeight() / 2) / Platformer.PPM);
    }

    public void SetCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);

    }

    public abstract void Interact(Player player);
}
