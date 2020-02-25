package Tools;

import Sprites.Door;
import Sprites.Enemies.Pig;
import Sprites.Items.Diamond;
import Sprites.Items.Heart;
import Sprites.Platforms;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Platformer;
import com.mygdx.game.Screens.PlatScreen;

public class B2DWorldCreator {
    private World world;
    private TiledMap map;
    private Body body;

    public B2DWorldCreator(PlatScreen screen) {
        this.map = screen.getMap();
        this.world = screen.getWorld();
        Box2DCreator creator = screen.getBoxCreator();

        // Ground
        for (MapObject object : map.getLayers().get("ground").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            body = creator.createStaticBody((rect.getX() + rect.getWidth() / 2) / Platformer.PPM, (rect.getY() + rect.getHeight() / 2) / Platformer.PPM);
            creator.createSquareFixture(body, (rect.getWidth() / 2) / Platformer.PPM, (rect.getHeight() / 2) / Platformer.PPM).setUserData(this);
        }


        // InteractiveObjects
        for (MapObject object : map.getLayers().get("Doors").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Door(screen, rect);
        }
        for (MapObject object : map.getLayers().get("Platforms").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Platforms(screen, rect);
        }

        for (MapObject object : map.getLayers().get("Hearts").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Heart(screen, rect);
        }
        for (MapObject object : map.getLayers().get("Diamonds").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Diamond(screen, rect);
        }
        for (MapObject object : map.getLayers().get("Pigs").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Pig(screen, rect);
        }


    }


}
