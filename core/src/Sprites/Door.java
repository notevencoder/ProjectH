package Sprites;

import Tools.Updatable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Platformer;
import com.mygdx.game.Screens.PlatScreen;

public class Door extends InteractiveObjects implements Updatable {

    public enum State {OPENNING, IDLE, CLOSING}

    ;

    private Animation openning;
    private Animation closing;
    private Animation idle;
    private float stateTimer;
    private TextureAtlas atlas;


    private State curState;
    private State prevState;

    public Door(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);


        atlas = new TextureAtlas("Door/Door.pack");
        fixture.setUserData(this);
        defineAnimations();
        SetCategoryFilter(Platformer.DOOR_BIT);
        fixture.setSensor(true);
        PlatScreen.updateQueue.addForever(this);

        PlatScreen.updateQueue.addToDrawable(this);
        PlatScreen.updateQueue.addForever(this);
        setRegion((TextureRegion) openning.getKeyFrame(0, true));
        setBounds(bounds.getX() / Platformer.PPM, bounds.getY() / Platformer.PPM , 46/ Platformer.PPM, 56 / Platformer.PPM);

    }

    private void defineAnimations() {
        curState = State.OPENNING;
        prevState = State.OPENNING;
        stateTimer = 0;


        TextureRegion curRegion;
        Array<TextureRegion> frames = new Array<TextureRegion>();

        //Анимация закрытия
        curRegion = atlas.findRegion("Closing (46x56)");
        for (int i = 0; i < 3; i++)
            frames.add(new TextureRegion(curRegion, i * 46, 0, 46, 56));
        closing = new Animation(0.5f, frames);
        frames.clear();
        //Анимация открытия
        curRegion = atlas.findRegion("Opening (46x56)");
        for (int i = 0; i < 5; i++)
            frames.add(new TextureRegion(curRegion, i * 46, 0, 46, 56));
        openning = new Animation(0.5f, frames);
        frames.clear();
        //Анимация покоя
        curRegion = atlas.findRegion("Idle");
        frames.add(new TextureRegion(curRegion, 0, 0, 46, 56));
        idle = new Animation(0.1f, frames);
        frames.clear();

    }

    private State getState() {
        return State.CLOSING;
    }


    private TextureRegion getFrame(float dt) {
        TextureRegion region;
        State current = getState();

        switch (current) {
            case OPENNING:
                region = (TextureRegion) openning.getKeyFrame(stateTimer, true);
                break;
            case CLOSING:
                region = (TextureRegion) closing.getKeyFrame(stateTimer, true);
                break;
            case IDLE:
            default:
                region = (TextureRegion) idle.getKeyFrame(stateTimer);
                break;


        }
        stateTimer = curState == prevState ? stateTimer + dt : 0;
        prevState = curState;
        return region;
    }

    @Override
    public void update(float dt) {
        //setPosition(bounds.getX()/ Platformer.PPM, bounds.getY() / Platformer.PPM);
        setRegion(getFrame(dt));

        Gdx.app.log("Door", "Updated");

    }
}
