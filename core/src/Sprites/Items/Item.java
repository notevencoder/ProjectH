package Sprites.Items;

import Tools.Box2DCreator;
import Tools.Drawable;
import Tools.Updatable;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Platformer;
import com.mygdx.game.Screens.PlatScreen;


public abstract class Item extends Sprite implements Updatable, Drawable {

    Body body;
    Rectangle bounds;
    PlatScreen screen;
    World world;
    TiledMap map;
    TextureAtlas atlas;

    Fixture fixture;

    boolean destroyed = false;

    public Item(PlatScreen screen, Rectangle bounds){
        this.screen = screen;
        this.bounds = bounds;
        this.world = screen.getWorld();
        this.map = screen.getMap();
        atlas = new TextureAtlas("Items/Items.atlas");
        defineItem();

    }

    @Override
    public void drawMe(SpriteBatch batch) {
        draw(batch);
    }

    //Вызывается при уничтожении
    public void destroy() {
        world.destroyBody(body);
        PlatScreen.drawQueue.remove(this);
        destroyed = true;
    }

    public void SetCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    //Создаем тело предмета
    private void defineItem(){
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2 )/ Platformer.PPM, (bounds.getY() + bounds.getHeight() / 2) / Platformer.PPM);

        body = world.createBody(bdef);
        shape.setAsBox( (bounds.getWidth() / 2) / Platformer.PPM, (bounds.getHeight() / 2) / Platformer.PPM);
        fdef.shape = shape;
        fdef.isSensor = true;

        fixture = body.createFixture(fdef);

        Box2DCreator creator = screen.getBoxCreator();
        body = creator.createStaticBody((bounds.getX() + bounds.getWidth() / 2 )/ Platformer.PPM, (bounds.getY() + bounds.getHeight() / 2) / Platformer.PPM);
        fixture = creator.createSquareFixture(body, 0, 0, (bounds.getWidth() / 2) / Platformer.PPM,
                (bounds.getHeight() / 2) / Platformer.PPM,0, true, 0,0,0);
    }

    //Вызывается при взятии
    public abstract void onTake();

}
