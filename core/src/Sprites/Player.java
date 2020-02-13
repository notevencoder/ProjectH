package Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Platformer;
import com.mygdx.game.Screens.PlatScreen;

public class Player extends Sprite {
        public World world;
        public Body b2body;
        TextureRegion idle;

        public Player(World world, PlatScreen screen){
            super(screen.getAtlas().findRegion("01-King Human/Idle (78x58)"));
            this.world = world;
            definePlayer();
            idle = new TextureRegion(getTexture(), 0, 0, 78 , 58);
            setBounds(0,0, 78 / Platformer.PPM, 58 / Platformer.PPM);
            setRegion(idle);
        }

        public void update(float dt){
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
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
