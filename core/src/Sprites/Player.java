package Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Platformer;
import com.mygdx.game.Screens.PlatScreen;

public class Player extends Sprite {
        public World world;
        public Body b2body;

        public Player(World world){
            this.world = world;
            definePlayer();
        }

        public void definePlayer(){
            BodyDef bdef = new BodyDef();

            bdef.position.set(200 / Platformer.PPM,100 / Platformer.PPM);
            bdef.type = BodyDef.BodyType.DynamicBody;
            b2body = world.createBody(bdef);

            FixtureDef fdef = new FixtureDef();
            CircleShape shape = new CircleShape();
            shape.setRadius(10 / Platformer.PPM);

            fdef.shape = shape;
            b2body.createFixture(fdef);
        }

}
