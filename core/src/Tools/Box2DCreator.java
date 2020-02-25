package Tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Screens.PlatScreen;

public class Box2DCreator {

    BodyDef bdef;
    FixtureDef fdef;

    PolygonShape polygonShape;
    CircleShape circleShape;

    Vector2 boxPosition;

    PlatScreen screen;
    World world;

    public Box2DCreator(PlatScreen screen) {
        this.screen = screen;
        world = screen.getWorld();
        bdef = new BodyDef();
        fdef = new FixtureDef();
        polygonShape = new PolygonShape();
        circleShape = new CircleShape();
        boxPosition = new Vector2();
    }


    public Fixture createSquareFixture(Body body, float width, float height){
        return createSquareFixture(body, 0, 0 , width, height, 0);
    }
    public Fixture createSquareFixture(Body body, float relativeX, float relativeY, float width, float height, float angle){
        return createSquareFixture(body, relativeX, relativeY, width, height, angle, false, 0, 0, 0);
    }
    public Fixture createSquareFixture(Body body, float relativeX, float relativeY, float width, float height, float angle,
                                       boolean isSensor, float restitution, float friction, float density){
        boxPosition.set(relativeX, relativeY);
        polygonShape.setAsBox(width, height, boxPosition, angle);
        fdef.shape = polygonShape;
        fdef.isSensor = isSensor;
        fdef.restitution = restitution;
        fdef.friction = friction;
        fdef.density = density;
        return body.createFixture(fdef);
    }

    public Filter createFilter(Fixture fixture, short categoryBit, short... maskBits){
        Filter filter = new Filter();

        filter.categoryBits = categoryBit;

        short t = 0;
        for (short i : maskBits)
            t = (short) (t | i);
        filter.maskBits = t;
        return filter;
    }


    public Body createStaticBody(float x, float y) {
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.x = x;
        bdef.position.y = y;
        return world.createBody(bdef);
    }

    public Body createDynamicBody(float x, float y) {
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.x = x;
        bdef.position.y = y;
        return world.createBody(bdef);
    }

}
