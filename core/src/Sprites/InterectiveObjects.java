package Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.bullet.collision._btMprSimplex_t;
import com.mygdx.game.Platformer;

public abstract class InterectiveObjects {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;
    public InterectiveObjects(World world, TiledMap map, Rectangle bounds){
        this.bounds = bounds;
        this.map = map;
        this.world = world;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2 )/ Platformer.PPM, (bounds.getY() + bounds.getHeight() / 2) / Platformer.PPM);

        body = world.createBody(bdef);
        shape.setAsBox((bounds.getWidth() / 2) / Platformer.PPM, (bounds.getHeight() / 2) / Platformer.PPM);
        fdef.shape = shape;

        fixture = body.createFixture(fdef);

    }

    public void SetCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);

    }

}
