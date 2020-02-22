package Sprites.Enemies;

import Sprites.Player;
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
    private float width, height;


    public Pig(PlatScreen screen, Rectangle bounds){
        super(screen);

        screen.updateQueue.addForever(this);
        screen.drawQueue.add(this, 3);
        defineEnemy(bounds);
        defineAnimations();
        width = bounds.width;
        height = bounds.height;
        setBounds(body.getPosition().x - width / 2 / Platformer.PPM , body.getPosition().y - height / 2 / Platformer.PPM, width / Platformer.PPM, height  / Platformer.PPM);

    }

    @Override
    protected void defineEnemy(Rectangle bounds) {

            BodyDef bdef = new BodyDef();
            FixtureDef fdef = new FixtureDef();
            PolygonShape shape = new PolygonShape();

            bdef.position.set((bounds.x + bounds.width / 2)/ Platformer.PPM, (bounds.y + bounds.height / 2)/ Platformer.PPM);
            bdef.type = BodyDef.BodyType.DynamicBody;

            body = world.createBody(bdef);

            shape.setAsBox(bounds.width / Platformer.PPM / 2, bounds.height / Platformer.PPM / 2);
            fdef.shape = shape;
            fdef.restitution = 0;
            fdef.friction = 0;
            fdef.density = 0;
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
            frames.add(new TextureRegion(region, i*34 + 10,9,20,19));

        animIdle = new Animation(0.1f, frames);
        frames.clear();


    }

    public void attack (Player player){
        player.takeDamage();

    }

    @Override
    protected TextureRegion getFrame(float dt) {
            TextureRegion region = new TextureRegion();


            switch (getState()){
                case IDLE:
                    region = (TextureRegion) animIdle.getKeyFrame(stateTimer, true);
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
        setPosition(body.getPosition().x - width / 2  / Platformer.PPM , body.getPosition().y - height / 2 / Platformer.PPM);
    }
}
