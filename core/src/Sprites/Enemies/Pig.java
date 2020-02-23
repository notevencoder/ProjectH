package Sprites.Enemies;

import Sprites.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Platformer;
import com.mygdx.game.Screens.PlatScreen;


public class Pig extends Enemy{

    private enum State{IDLE,ATTACKING,MOVE};
    private enum Mode {PASSIVE};
    private State curState, prevState;
    private TextureAtlas atlas;
    private Animation animIdle, animMoving, animAttacking, animFalling, curAnim;
    private float width, height;
    private Vector2 areaL, areaR;
    private boolean onRight, attacking;


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
            onRight = false;

            attacking = false;
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

            areaR = new Vector2(body.getPosition().x  , body.getPosition().y);
            areaL = new Vector2(body.getPosition().x - 1, body.getPosition().y);



    }

    public void act(){
        float xCenter = body.getPosition().x + width / 2, yCenter = body.getPosition().y + height / 2;
        //body.setLinearVelocity(-0.2f,0);
        switch (getMode()){
            case PASSIVE:
                if (onRight && body.getPosition().x < areaR.x)
                    body.setLinearVelocity(0.2f,0);
                else if (!onRight && body.getPosition().x + width> areaL.x)
                    body.setLinearVelocity(-0.2f,0);
                break;

        }

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


        region = atlas.findRegion("Attack (34x28)");
        for (int i = 0; i < 5; i++)
            frames.add(new TextureRegion(region, i*34 + 10,9,20,23));

        animAttacking = new Animation(0.1f, frames);
        frames.clear();


        region = atlas.findRegion("Run (34x28)");
        for (int i = 0; i < 6; i++)
            frames.add(new TextureRegion(region, i*34 + 10,9,20,23));

        animMoving = new Animation(0.1f, frames);
        frames.clear();


    }

    public void attack (Player player){
        player.takeDamage();
        attacking = true;
        Gdx.app.log("Pig","ATTACK!!!!");

    }

    @Override
    protected TextureRegion getFrame(float dt) {
            TextureRegion region = new TextureRegion();


            switch (getState(dt)){
                case ATTACKING:
                    curAnim = animAttacking;
                    region = (TextureRegion) animAttacking.getKeyFrame(stateTimer);
                    curState = State.ATTACKING;
                    break;
                case MOVE:
                    curAnim = animMoving;
                    region = (TextureRegion) animMoving.getKeyFrame(stateTimer,true);
                    curState = State.MOVE;
                    break;
                case IDLE:
                    curAnim = animIdle;
                    region = (TextureRegion) animIdle.getKeyFrame(stateTimer, true);
                    curState = State.IDLE;
                    break;

            }

            if (body.getLinearVelocity().x > 0) onRight = true;
            else if (body.getLinearVelocity().x < 0) onRight = false;
            if (onRight && !isFlipX())
                region.flip(true, false);

            if (!onRight && isFlipX())
                region.flip(true, false);


            stateTimer = curState == prevState ? stateTimer + dt : 0;
            if (curAnim.isAnimationFinished(stateTimer)) attacking = false;
            prevState = curState;
            return region;
    }


    protected State getState(float dt) {
        if (attacking)
            return State.ATTACKING;
        if (Math.abs(body.getLinearVelocity().x) > 0) return State.MOVE;
        return State.IDLE;
    }
    protected Mode getMode() {
        return Mode.PASSIVE;
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
        act();
        setRegion(getFrame(dt));

        setPosition(body.getPosition().x - width / 2  / Platformer.PPM , body.getPosition().y - height / 2 / Platformer.PPM);
    }
}
