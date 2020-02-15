package Sprites;

import Tools.Drawable;
import Tools.Updatable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Platformer;
import com.mygdx.game.Screens.PlatScreen;
import com.sun.tools.javac.comp.Enter;

public class Door extends InteractiveObjects implements Updatable, Drawable {

    public enum State {OPEN, IDLE, CLOSE}
    private boolean Entering;
    private Animation opening;
    private Animation closing;
    private Animation idle;
    private Animation currentAnimation;
    private TextureRegion currentRegion;


    private float stateTimer;
    private TextureAtlas atlas;

    private Player interector;
    private State curState;
    private State prevState;

    public Door(PlatScreen screen, Rectangle bounds) {
        super(screen, bounds);
        Entering = false;

        atlas = new TextureAtlas("Door/Door.pack");
        fixture.setUserData(this);
        defineAnimations();
        SetCategoryFilter(Platformer.DOOR_BIT);
        fixture.setSensor(true);
        PlatScreen.updateQueue.addForever(this);

        PlatScreen.updateQueue.addForever(this);
        PlatScreen.drawQueue.add(this);
        //setRegion((TextureRegion) openning.getKeyFrame(0, true));
        setRegion((TextureRegion) opening.getKeyFrame(0, true));
        setBounds(bounds.getX() / Platformer.PPM, bounds.getY() / Platformer.PPM , 46/ Platformer.PPM, 56 / Platformer.PPM);

    }
    @Override
    public void drawMe(SpriteBatch batch){
        draw(batch);
    }

    @Override
    public void Interact(Player player) {
        Entering = true;
        curState = State.OPEN;
        interector = player;
        player.Interact(this);
    }

    private void defineAnimations() {
        curState = State.IDLE;
        prevState = State.IDLE;
        currentAnimation = idle;
        stateTimer = 0;


        TextureRegion curRegion;
        Array<TextureRegion> frames = new Array<TextureRegion>();

        //Анимация закрытия
        curRegion = atlas.findRegion("Closing (46x56)");
        for (int i = 0; i < 3; i++)
            frames.add(new TextureRegion(curRegion, i * 46, 0, 46, 56));
        closing = new Animation(0.8f, frames);
        frames.clear();

        //Анимация открытия
        curRegion = atlas.findRegion("Opening (46x56)");
        for (int i = 0; i < 5; i++)
            frames.add(new TextureRegion(curRegion, i * 46, 0, 46, 56));
        opening = new Animation(0.5f, frames);
        frames.clear();
        //Анимация покоя
        curRegion = atlas.findRegion("Idle");
        frames.add(new TextureRegion(curRegion, 0, 0, 46, 56));
        idle = new Animation(0.1f, frames);
        frames.clear();
        //Анимация закрытия


    }

    private State getState(float dt) {
        //Gdx.app.log("getState" , "Enter");
        if (Entering){
            //Gdx.app.log("getState" , "Entering");
            if (currentAnimation.isAnimationFinished(getStateTimer())){

                //Gdx.app.log("DoorState", "AnimationFinished");
                if(currentAnimation == opening && interector.getState(dt) != Player.State.STANDING &&interector.getCurrentAnimation().isAnimationFinished(interector.getStateTimer())){
                    Gdx.app.log("DoorState", "Close");
                        return State.CLOSE;
                }

            } else return curState;
        }

        return curState;


        /**/
    }

    public static Rectangle getBounds(){
        return bounds;

    }


    private TextureRegion getFrame(float dt) {
        TextureRegion region;
        curState = getState(dt);
        //Gdx.app.log("getFrame" , "Entered");

        switch (curState) {
            case OPEN:
                region = (TextureRegion) opening.getKeyFrame(stateTimer);
                currentAnimation = opening;
                //Gdx.app.log("getFrame" , "Open");
                break;
            case CLOSE:
                region = (TextureRegion) closing.getKeyFrame(stateTimer);
                currentAnimation = closing;
                Gdx.app.log("getFrame" , "Close");
                break;
            case IDLE:
            default:
                region = (TextureRegion) idle.getKeyFrame(stateTimer);
                currentAnimation = idle;
                break;


        }
        stateTimer = curState == prevState ? stateTimer + dt : 0;
        prevState = curState;
        currentRegion = region;
        return region;
    }

    public Animation getCurrentAnimation(){
        return currentAnimation;

    }

    public float getStateTimer(){
        return stateTimer;

    }

    @Override
    public void update(float dt) {
        setRegion(getFrame(dt));
    }
}
