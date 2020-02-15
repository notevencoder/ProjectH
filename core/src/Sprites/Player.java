package Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Platformer;
import com.mygdx.game.Screens.PlatScreen;

public class Player extends Sprite {
    public World world;
    public Body b2body;

    TextureRegion idle;
    public final float WIDTH = 20 / Platformer.PPM, HEIGHT = 20 / Platformer.PPM;

    // объявляем переменные для Анимации
    public enum State {STANDING, JUMPING, RUNNING, FALLING, ATTACKING,ENTER_THE_DOOR}


    private State currentState;
    private State previousState;
    private Animation animationIdle, animationRun, animationFall, animationJump, animationGround,
            animationAttack, animationDead, animationDoorIn, animationDoorOut, animationHit, entertheDoor;
    private float stateTimer;
    private boolean runningRight;
    public boolean stepped = false;
    private boolean attacking = false;
    private Animation currentAnimation;

    private static boolean entering;


    private static InteractiveObjects canInteractWith = null;
    private static InteractiveObjects interactingWithNow = null;

    public Player(PlatScreen screen) {
        //super(screen.getAtlas().findRegion("Run (78x58)"));

        this.world = screen.getWorld();
        entering = false;
        this.world = world;

        definePlayer();
        defineAnimations(screen);

        setBounds(0, 0, 78 / Platformer.PPM, 58 / Platformer.PPM);
    }

    public static void setCanInteractWithNow(InteractiveObjects objects){
        canInteractWith = (InteractiveObjects) objects;
    }

    public static InteractiveObjects getCanInteractWithNow(InteractiveObjects objects){
        return canInteractWith;
    }

    public void handleInput(float dt) {
        if (!entering) {
            if (Gdx.input.isKeyPressed(Input.Keys.E) && canInteractWith != null) {
                canInteractWith.Interact(this);
            }
            if (attacking && stateTimer >= animationAttack.getKeyFrames().length * 0.1f)
                attacking = false;
            if (Gdx.input.isKeyJustPressed(Input.Keys.F) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
                attacking = true;

            if ((Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W))
                //&& (getState() != Player.State.FALLING && getState() != State.JUMPING)
            )
                b2body.applyLinearImpulse(new Vector2(0, 4), b2body.getWorldCenter(), true);
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D))
                b2body.setLinearVelocity(2, b2body.getLinearVelocity().y);
            else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))
                b2body.setLinearVelocity(-2, b2body.getLinearVelocity().y);
            else
                b2body.setLinearVelocity(0, b2body.getLinearVelocity().y);
        }
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState(dt);

        TextureRegion region;

        switch (currentState) {
            case ATTACKING:
                region = (TextureRegion) animationAttack.getKeyFrame(stateTimer);
                currentAnimation = animationAttack;
                break;
            case RUNNING:
                region = (TextureRegion) animationRun.getKeyFrame(stateTimer, true);
                currentAnimation = animationRun;
                break;
            case FALLING:
                region = (TextureRegion) animationFall.getKeyFrame(stateTimer, true);
                currentAnimation = animationFall;
                break;
            case JUMPING:
                region = (TextureRegion) animationJump.getKeyFrame(stateTimer, true);
                currentAnimation = animationJump;
                break;
            case ENTER_THE_DOOR:
                region = (TextureRegion) entertheDoor.getKeyFrame(stateTimer);

                currentAnimation = entertheDoor;
                break;
            case STANDING:
            default:
                region = (TextureRegion) animationIdle.getKeyFrame(stateTimer, true);
                currentAnimation = animationIdle;
                break;
        }

        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;

    }

    public State getState(float dt) {
        if (!entering){
            previousState = currentState;
            if (attacking) return State.ATTACKING;
            if (b2body.getLinearVelocity().y > 0) return State.JUMPING;
            if (b2body.getLinearVelocity().y < 0) return State.FALLING;
            if (b2body.getLinearVelocity().x != 0) return State.RUNNING;
            return State.STANDING;
        } else{
            if (((Door)interactingWithNow).getCurrentAnimation().isAnimationFinished(((Door)interactingWithNow).getStateTimer())){
                  return State.ENTER_THE_DOOR;
            } else return State.STANDING;
        }
    }

    public static void  Interact(InteractiveObjects object){
        entering = true;
        interactingWithNow = object;
    }

    public void update(float dt) {
        if (runningRight)
            setPosition(b2body.getPosition().x - getWidth() / 2 + getWidth() / 10, b2body.getPosition().y - getHeight() / 2);
        else
            setPosition(b2body.getPosition().x - getWidth() / 2 - getWidth() / 10, b2body.getPosition().y - getHeight() / 2);
        //
        setRegion(getFrame(dt));

    }


    public void definePlayer() {
        BodyDef bdef = new BodyDef();


        bdef.position.set(200 / Platformer.PPM, 100 / Platformer.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
//            CircleShape shape = new CircleShape();
//            shape.setRadius(10 / Platformer.PPM);
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(WIDTH / 2, HEIGHT / 2);
        fdef.shape = shape;

        fdef.filter.categoryBits = Platformer.PLAYER_BIT;
        fdef.filter.maskBits = Platformer.DEFAULT_BIT | Platformer.ENEMY_BIT | Platformer.PLATFORM_BIT | Platformer.DOOR_BIT;

        b2body.createFixture(fdef).setUserData(this);

        EdgeShape hitBox = new EdgeShape();
        fdef.isSensor = true;
        hitBox.set(new Vector2(2f / Platformer.PPM, 10 / Platformer.PPM), new Vector2(-2f / Platformer.PPM, 10 / Platformer.PPM));
        fdef.shape = hitBox;
        b2body.createFixture(fdef).setUserData("Head");
        hitBox.set(new Vector2(2f / Platformer.PPM, -10 / Platformer.PPM), new Vector2(-2f / Platformer.PPM, -10 / Platformer.PPM));
        fdef.shape = hitBox;
        b2body.createFixture(fdef).setUserData("Legs");
        hitBox.set(new Vector2(10 / Platformer.PPM, -2f / Platformer.PPM), new Vector2(10 / Platformer.PPM, 2f / Platformer.PPM));
        fdef.shape = hitBox;
        b2body.createFixture(fdef).setUserData("Right");
        hitBox.set(new Vector2(-10 / Platformer.PPM, -2f / Platformer.PPM), new Vector2(-10 / Platformer.PPM, 2f / Platformer.PPM));
        fdef.shape = hitBox;
        b2body.createFixture(fdef).setUserData("Left");
        shape.setAsBox(15 / Platformer.PPM, 10 / Platformer.PPM, new Vector2(30 / Platformer.PPM, 0), 0);
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData("AttackRight");
        shape.setAsBox(15 / Platformer.PPM, 10 / Platformer.PPM, new Vector2(-30 / Platformer.PPM, 0), 0);
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData("AttackLeft");
    }

    public void defineAnimations(PlatScreen screen) {
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;

        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        // Анимация бега
        TextureRegion currRegion = screen.getAtlas().findRegion("Run (78x58)");
        for (int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(currRegion, i * 78, 0, 78, 58));

        }

        animationRun = new Animation(0.1f, frames);
        frames.clear();
        // Анимация входа
        currRegion = screen.getAtlas().findRegion("Door In (78x58)");
        for (int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(currRegion, i * 78, 0, 78, 58));

        }
        frames.add(new TextureRegion(currRegion, 0, 0, 0, 0));
        entertheDoor = new Animation(0.1f, frames);
        frames.clear();

        // анимация стояния
        currRegion = screen.getAtlas().findRegion("Idle (78x58)");
        for (int i = 0; i < 11; i++) {
            frames.add(new TextureRegion(currRegion, i * 78, 0, 78, 58));

        }
        animationIdle = new Animation(0.1f, frames);
        frames.clear();

        // Анимация прыжка
        currRegion = screen.getAtlas().findRegion("Jump (78x58)");
        frames.add(new TextureRegion(currRegion, 0, 0, 78, 58));
        animationJump = new Animation(0.1f, frames);
        frames.clear();

        // Анимация падения
        currRegion = screen.getAtlas().findRegion("Fall (78x58)");
        frames.add(new TextureRegion(currRegion, 0, 0, 78, 58));
        animationFall = new Animation(0.1f, frames);
        frames.clear();

        currRegion = screen.getAtlas().findRegion("Attack (78x58)");
        for (int i = 0; i < 3; i++)
            frames.add(new TextureRegion(currRegion, i * 78, 0, 78, 58));
        animationAttack = new Animation(0.1f, frames);
        frames.clear();

    }

    public Animation getCurrentAnimation() {
        return currentAnimation;
    }

    public float getStateTimer() {
        return stateTimer;
    }
}
