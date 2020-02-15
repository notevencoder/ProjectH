package Sprites;

import Tools.Drawable;
import Tools.Updatable;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Platformer;
import com.mygdx.game.Screens.PlatScreen;

public class Enemy extends Sprite implements Updatable, Drawable {
    private World world;
    private TiledMap map;
    private PlatScreen screen;
    private Body body;
    private State currentState, previousState;
    private float stateTimer = 0;
    private boolean setToDestroy, destroyed, fromRight;

    public enum State {IDLE}

    Animation Idle;

    public Enemy(PlatScreen screen) {
        this.screen = screen;
        this.world = screen.getWorld();
        this.map = screen.getMap();
        defineEnemy();
        defineAnimations();

        setBounds(0, 0, 34 / Platformer.PPM, 28 / Platformer.PPM);
        setRegion((TextureRegion) Idle.getKeyFrame(0));
    }

    @Override
    public void update(float dt) {

        if (setToDestroy && !destroyed && screen.getPlayer().attacking && screen.getPlayer().runningRight == fromRight) {
            PlatScreen.drawQueue.remove(this);
            world.destroyBody(body);
            destroyed = true;
        }
        if (!destroyed) {
            stateTimer += dt;
            setPosition(body.getPosition().x - getWidth() / 2 - 3 / Platformer.PPM, body.getPosition().y - getHeight() / 2 + 4 / Platformer.PPM);
            setRegion((TextureRegion) Idle.getKeyFrame(stateTimer, true));
        }
    }
    @Override
    public void drawMe(SpriteBatch batch){
        draw(batch);
    }


    public void destroyFromRight(boolean bool) {
        setToDestroy = bool;
        fromRight = true;
    }
    public void destroyFromLeft(boolean bool) {
        setToDestroy = bool;
        fromRight = false;
    }

    private void defineEnemy() {

        setToDestroy = destroyed = false;
        screen.drawQueue.add(this);

        BodyDef bdef = new BodyDef();
        bdef.position.set(250 / Platformer.PPM, 200 / Platformer.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(8 / Platformer.PPM, 8 / Platformer.PPM);
        fdef.shape = shape;
        fdef.friction = 0;
        fdef.restitution = 0;

        fdef.filter.categoryBits = Platformer.ENEMY_BIT;
        fdef.filter.maskBits = Platformer.DEFAULT_BIT | Platformer.ENEMY_BIT | Platformer.PLAYER_BIT;
        body.createFixture(fdef).setUserData(this);
    }

    public void defineAnimations() {
        TextureAtlas atlas = new TextureAtlas("Sprites/15-Atlases/atlas.atlas");
        currentState = State.IDLE;
        previousState = State.IDLE;
        stateTimer = 0;


        Array<TextureRegion> frames = new Array<TextureRegion>();

        TextureRegion currRegion = atlas.findRegion("03-Pig/Idle (34x28)");
        for (int i = 0; i < 11; i++) {
            frames.add(new TextureRegion(currRegion, i * 34, 0, 34, 28));
        }

        Idle = new Animation(0.1f, frames);
        frames.clear();

    }


}
