package Sprites.Enemys;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Platformer;
import com.mygdx.game.Screens.PlatScreen;


public class Pig extends Enemy{

    private enum State{IDLE};
    private State curState, prevState;
    private TextureAtlas atlas;
    private Animation animIdle, animMoving, animAttacking, animFalling;


    public Pig(PlatScreen screen, Rectangle bounds){
        this.screen = screen;
        world = screen.getWorld();
        map = screen.getMap();
        screen.updateQueue.add(this);
        defineEnemy(bounds);
        defineAnimations();

    }

    @Override
    protected void defineEnemy(Rectangle bounds) {

            BodyDef bdef = new BodyDef();
            FixtureDef fdef = new FixtureDef();
            PolygonShape shape = new PolygonShape();

            bdef.position.set(bounds.getX(), bounds.getY());
            bdef.type = BodyDef.BodyType.StaticBody;

            body = world.createBody(bdef);

            shape.setAsBox(bounds.width, bounds.height);
            fdef.shape = shape;
            fdef.filter.categoryBits = Platformer.ENEMY_BIT;
            fdef.filter.maskBits = Platformer.DEFAULT_BIT | Platformer.PLAYER_BIT;

            body.createFixture(fdef).setUserData(this);



    }

    @Override
    protected void defineAnimations() {
        atlas = new TextureAtlas("Pig/Pig.pack");
        TextureRegion region = new TextureRegion();
        Array<TextureRegion> frames = new Array<TextureRegion>();

        region = atlas.findRegion("Idle (34x28)");

        for (int i = 0; i < 11; i++)
            frames.add(new TextureRegion(region, i*34,0,34,28));

        animIdle = new Animation(0.1f, frames);
        frames.clear();


    }

    @Override
    protected TextureRegion getFrame(float dt) {
            TextureRegion region = new TextureRegion();


            switch (getState()){
                case IDLE:
                    region = (TextureRegion) animIdle.getKeyFrame(stateTimer);
                    curState = State.IDLE;
                    break;

            }

            stateTimer = curState == prevState ? stateTimer + dt : 0;
            prevState = curState;
            return region;
    }


    protected State getState() {
        return State.IDLE;
    }

    @Override
    protected void hitON() {

    }

    @Override
    public void drawMe(SpriteBatch batch) {
        this.draw(batch);
    }

    @Override
    public void update(float dt) {
        setRegion(getFrame(dt));
    }
}
