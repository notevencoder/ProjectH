package Tools;

import Sprites.Door;
import Sprites.Platforms;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Platformer;

import java.awt.*;

public class B2DWorldCreator {
    World world;
    TiledMap map;
    Body body;
    BodyDef bdef;
    FixtureDef fdef;
    PolygonShape shape;

    public B2DWorldCreator(World world, TiledMap map){
        this.map = map;
        this.world = world;
        bdef = new BodyDef();
        shape = new PolygonShape();
        fdef = new FixtureDef();

        // Ground
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2) / Platformer.PPM, (rect.getY() + rect.getHeight() / 2) / Platformer.PPM);

            body = world.createBody(bdef);

            shape.setAsBox((rect.getWidth() / 2) / Platformer.PPM,(rect.getHeight() / 2) / Platformer.PPM);
            fdef.shape = shape;
            fdef.friction = 0;
            body.createFixture(fdef).setUserData("ground");
        }

        // InteractiveObjects
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Door(world, map, rect);
        }
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Platforms(world, map, rect);
        }

    }







}
