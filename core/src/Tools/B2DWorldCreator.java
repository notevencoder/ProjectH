package Tools;

import Sprites.Door;
import Sprites.Platforms;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Platformer;
import com.mygdx.game.Screens.PlatScreen;

import java.awt.*;

public class B2DWorldCreator {
   private World world;
   private TiledMap map;
   private Body body;
   private BodyDef bdef;
   private FixtureDef fdef;
   private PolygonShape shape;

    public B2DWorldCreator(PlatScreen screen){
        this.map = screen.getMap();
        this.world = screen.getWorld();
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

            new Door(screen, rect);
        }
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Platforms(screen, rect);
        }

    }







}
