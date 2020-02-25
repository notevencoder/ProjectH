package com.mygdx.game.Sprites;

import com.mygdx.game.Tools.Box2DCreator;
import com.mygdx.game.Tools.Drawable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Platformer;
import com.mygdx.game.Screens.PlatScreen;

public class Player extends Sprite implements Drawable, Telegraph {
    public World world;
    public Body b2body;

    TextureRegion idle;
    public final float WIDTH = 20 / Platformer.PPM, HEIGHT = 20 / Platformer.PPM;

    // объявляем переменные для Анимации
    public enum State {STANDING, JUMPING, RUNNING, FALLING, ATTACKING, ENTER_THE_DOOR, DEAD, HITTED , LANDING}

    private PlatScreen screen;
    private State currentState;
    private State previousState;
    private Animation animationIdle, animationRun, animationFall, animationJump, animationGround,
            animationAttack, animationDead, animationDoorIn, animationDoorOut, animationHit, entertheDoor;
    private float stateTimer;
    public boolean runningRight;
    public boolean stepped = false;
    public boolean attacking = false;
    private Sound jumpSound, deathSound;
    private Animation currentAnimation;

    private int lives;

    private static boolean entering, dead, hitted;


    private static InteractiveObjects canInteractWith = null;
    private static InteractiveObjects interactingWithNow = null;

    public Player(PlatScreen screen) {
        //super(screen.getAtlas().findRegion("Run (78x58)"));
        this.screen = screen;
        this.world = screen.getWorld();
        this.world = world;

        entering = false;
        hitted = false;
        lives = 3;
        initSounds();
        definePlayer();
        defineAnimations(screen);

        setBounds(0, 0, 78 / Platformer.PPM, 58 / Platformer.PPM);
    }

    public void update(float dt) {
        if (runningRight)
            setPosition(b2body.getPosition().x - getWidth() / 2 + getWidth() / 10, b2body.getPosition().y - getHeight() / 2 + 3 / Platformer.PPM);
        else
            setPosition(b2body.getPosition().x - getWidth() / 2 - getWidth() / 10, b2body.getPosition().y - getHeight() / 2 + 3 / Platformer.PPM);

        setRegion(getFrame(dt));

    }

    public void attack(){
            attacking = true;
    }

    @Override
    public boolean handleMessage(Telegram msg) {
    if (!entering && !hitted && !dead)
        switch(msg.message){
            case(Platformer.MSG_UP):
                jump();
                return true;

            case(Platformer.MSG_DOWN):
                return true;
            case(Platformer.MSG_RIGHT):
                b2body.setLinearVelocity(2, b2body.getLinearVelocity().y);
                Gdx.app.log("MSG_RIGHT","Got it");
                return true;
            case(Platformer.MSG_LEFT):
                b2body.setLinearVelocity(-2, b2body.getLinearVelocity().y);
                Gdx.app.log("MSG_LEFT","Got it");
                return true;
            case(Platformer.MSG_E):
                canInteractWith.Interact(this);
                b2body.setLinearVelocity(0, 0);
                return true;
            case(Platformer.MSG_F):
                attack();
                return true;
            case (Platformer.MSG_DEFAULT):
                b2body.setLinearVelocity(0,b2body.getLinearVelocity().y);
                Gdx.app.log("MSG_DEFAULT","Got it");
                return true;
        }

        return false;
    }

    public void handleInput(float dt) {
        if (!entering && !dead && !hitted) {
            b2body.setAwake(true);


            if ((Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W))
                //&& (getState() != Player.State.FALLING && getState() != State.JUMPING)
            ) {
                jump();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                b2body.setLinearVelocity(2, b2body.getLinearVelocity().y);
            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
                b2body.setLinearVelocity(-2, b2body.getLinearVelocity().y);
            } else {
                b2body.setLinearVelocity(0, b2body.getLinearVelocity().y);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.E) && canInteractWith != null) {
                canInteractWith.Interact(this);
                b2body.setLinearVelocity(0, 0);
            }
        }
    }

    @Override
    public void drawMe(SpriteBatch batch) {
        draw(batch);
    }
    public void jump(){
        b2body.applyLinearImpulse(new Vector2(0, 4), b2body.getWorldCenter(), true);
        //jumpSound.play();
    }

    public boolean addLives(){
        if (lives < 3){
            lives++;
            return true;
        }
            return false;
    }
    public boolean takeDamage(){
        hitted = true;
        if (lives > 0){
            lives--;
            stateTimer = 0;
            if (lives == 0)
                die();
            return true;
        }
        else dead = true;
        return false;
    }
    public void die(){
        dead = true;
        b2body.setAwake(false);
        deathSound.play();
    }

    public static void Interact(InteractiveObjects object) {
        entering = true;
        interactingWithNow = object;
    }

    public void onEnter() {
        world.destroyBody(b2body);
        PlatScreen.drawQueue.remove(this);
    }

    public static void setCanInteractWithNow(InteractiveObjects objects) {
        canInteractWith = (InteractiveObjects) objects;
    }

    public static InteractiveObjects getCanInteractWithNow(InteractiveObjects objects) {
        return canInteractWith;
    }

    private void initSounds(){
        deathSound = screen.initSound("death.mp3");
        jumpSound = screen.initSound("jump.ogg");
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
            case HITTED:
                region = (TextureRegion) animationHit.getKeyFrame(stateTimer);
                currentAnimation = animationHit;
                break;
            case JUMPING:
                region = (TextureRegion) animationJump.getKeyFrame(stateTimer, true);
                currentAnimation = animationJump;
                break;
            case ENTER_THE_DOOR:
                region = (TextureRegion) entertheDoor.getKeyFrame(stateTimer);
                currentAnimation = entertheDoor;
                break;
            case DEAD:
                region = (TextureRegion) animationDead.getKeyFrame(stateTimer);
                currentAnimation = animationDead;
                break;

            case LANDING:
                region = (TextureRegion) animationGround.getKeyFrame(stateTimer);
                Gdx.app.log("1","2");
                currentAnimation = animationGround;
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
        if (currentAnimation == animationAttack && currentAnimation.isAnimationFinished(stateTimer))
            attacking = false;
        if (hitted && currentAnimation.isAnimationFinished(stateTimer)) hitted = false;
        previousState = currentState;
        return region;

    }

    public State getState(float dt) {
        if (!entering && !dead) {
            previousState = currentState;
            if (hitted) return State.HITTED;
            if (attacking) return State.ATTACKING;
            if (b2body.getLinearVelocity().y > 0) return State.JUMPING;
            if (b2body.getLinearVelocity().y < 0) return State.FALLING;

            if (b2body.getLinearVelocity().x != 0) return State.RUNNING;
            return State.STANDING;
        } else if (entering){
            if (((Door) interactingWithNow).getCurrentAnimation().isAnimationFinished(((Door) interactingWithNow).getStateTimer())) {
                return State.ENTER_THE_DOOR;
            } else return State.STANDING;
        }else {
            return State.DEAD;

        }
    }


    public Animation getCurrentAnimation() {
        return currentAnimation;
    }

    public float getStateTimer() {
        return stateTimer;
    }

    public int getLives(){
        return lives;
    }

    public void definePlayer() {
        dead = false;
        Box2DCreator creator = screen.getBoxCreator();

        b2body = creator.createDynamicBody(200 / Platformer.PPM, 100 / Platformer.PPM);

        Fixture fixture = creator.createSquareFixture(b2body, WIDTH / 2, HEIGHT / 2);
        fixture.setFilterData(creator.createFilter(fixture, Platformer.PLAYER_BIT,
                Platformer.DEFAULT_BIT, Platformer.ENEMY_BIT, Platformer.PLATFORM_BIT, Platformer.DOOR_BIT, Platformer.ITEM_BIT));

        fixture.setUserData(this);

        fixture = creator.createSquareFixture(b2body, 30 / Platformer.PPM, 0,
                15 / Platformer.PPM, 10 / Platformer.PPM, 0, true, 0,0,0);
        fixture.setUserData("AttackRight");

        fixture = creator.createSquareFixture(b2body, -30 / Platformer.PPM, 0,
                15 / Platformer.PPM, 10 / Platformer.PPM, 0, true,0,0,0);
        fixture.setUserData("AttackLeft");
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

        //ПОБЕДА ИЛИ СМЕРТЬ, Я ВЫБИРАЮ СМЕРТТ
        currRegion = screen.getAtlas().findRegion("Dead (78x58)");
        for (int i = 0; i < 4; i++)
            frames.add(new TextureRegion(currRegion, i * 78, 0, 78, 58));
        animationDead = new Animation(0.1f, frames);
        frames.clear();
        //ПОБЕДА ИЛИ СМЕРТЬ, Я ВЫБИРАЮ ПО ЛИЦУ
        currRegion = screen.getAtlas().findRegion("Hit (78x58)");
        for (int i = 0; i < 2; i++)
            frames.add(new TextureRegion(currRegion, i * 78, 0, 78, 58));
        animationHit = new Animation(0.1f, frames);
        frames.clear();
        //ПОБЕДА ИЛИ СМЕРТЬ, Я ВЫБИРАЮ ПРИЗЕМЛЕНИЕ
        currRegion = screen.getAtlas().findRegion("Ground (78x58)");
        for (int i = 0; i < 1; i++)
            frames.add(new TextureRegion(currRegion, i * 78, 0, 78, 58));
        animationGround = new Animation(0.1f, frames);
        frames.clear();

    }

    public boolean isRunningRight() {
        return runningRight;
    }
}
