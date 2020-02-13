package Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Platformer;
import com.mygdx.game.Screens.PlatScreen;

public class Player extends Sprite {
        public World world;
        public Body b2body;
        TextureRegion idle;
        // объявляем переменные для Анимации
        private enum State {STANDING, JUMPING, RUNNING, FALLING};
        private State currentState;
        private State previousState;
        private Animation playerStanding;
        private Animation playerRunning;
        private Animation playerFalling;
        private Animation playerJumping;
        private float stateTimer;
        private boolean runningRight;

        public Player(World world, PlatScreen screen){
            //super(screen.getAtlas().findRegion("Run (78x58)"));
            this.world = world;

            definePlayer();
            defineAnimations(screen);

            setBounds(0,0, 78 / Platformer.PPM, 58 / Platformer.PPM);

        }


        public TextureRegion getFrame(float dt){
                currentState = getState();
                TextureRegion region;

                switch(currentState){
                    case RUNNING:
                        region = (TextureRegion) playerRunning.getKeyFrame(stateTimer,true);
                        break;
                    case FALLING:
                        region = (TextureRegion) playerFalling.getKeyFrame(stateTimer, true);
                        break;
                    case JUMPING:
                        region = (TextureRegion) playerJumping.getKeyFrame(stateTimer,true);
                        break;
                    case STANDING:
                    default:
                        region = (TextureRegion) playerStanding.getKeyFrame(stateTimer,true);
                        break;
                }

                if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
                    region.flip(true,false);
                    runningRight = false;
                }
                else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
                    region.flip(true,false);
                    runningRight = true;
                }

                stateTimer = currentState == previousState ? stateTimer + dt : 0;
                previousState = currentState;
                return region;

        }

        public State getState(){
            if (b2body.getLinearVelocity().y > 0) return State.JUMPING;
            else if (b2body.getLinearVelocity().y < 0)return State.FALLING;
            else if (b2body.getLinearVelocity().x != 0) return State.RUNNING;
            else return State.STANDING;
        }
        public void update(float dt){
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(getFrame(dt));
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
        public void defineAnimations(PlatScreen screen){
            currentState = State.STANDING;
            previousState = State.STANDING;
            stateTimer = 0;
            runningRight = true;

            Array<TextureRegion> frames = new Array<TextureRegion>();

            // Анимация бега
            TextureRegion currRegion = screen.getAtlas().findRegion("Run (78x58)");
            for (int i = 0; i < 8; i++){
                frames.add(new TextureRegion(currRegion, i*78, 0, 78, 58));

            }

            playerRunning = new Animation(0.1f, frames);
            frames.clear();

            // анимация стаяния
            currRegion = screen.getAtlas().findRegion("Idle (78x58)");
            for (int i = 0; i < 11; i++){
                frames.add(new TextureRegion(currRegion, i*78, 0, 78, 58));

            }
            playerStanding = new Animation(0.1f, frames);
            frames.clear();

            // Анимация прыжка
            currRegion = screen.getAtlas().findRegion("Jump (78x58)");
            frames.add(new TextureRegion(currRegion, 0, 0, 78, 58));
            playerJumping = new Animation(0.1f, frames);
            frames.clear();


            currRegion = screen.getAtlas().findRegion("Fall (78x58)");
            frames.add(new TextureRegion(currRegion, 0, 0, 78, 58));
            playerFalling = new Animation(0.1f, frames);
            frames.clear();

        }


}
