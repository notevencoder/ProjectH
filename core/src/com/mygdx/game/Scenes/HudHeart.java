package com.mygdx.game.Scenes;

import Tools.Drawable;
import Tools.Updatable;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.physics.bullet.linearmath.HullDesc;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Platformer;
import com.mygdx.game.Screens.PlatScreen;

class HudHeart extends Sprite implements Drawable, Updatable {

    private enum State{IDLE , HIT};
    private PlatScreen screen;
    private float stateTimer;
    private TextureAtlas atlas;
    private boolean exist, destroying, destroyed;
    private float scale = 2;

    private float curX, curY;

    private Animation animIdle, animHitOn;

    public HudHeart(PlatScreen screen, float X, float Y){

        this.screen = screen;

        defineHeart(X, Y);
        defineAnimation();

        setBounds(X, Y, 18 *  scale, 14 * scale );


    }

    private void defineAnimation() {
        stateTimer = 0;

        atlas = new TextureAtlas("Hud/Hud.pack");

        Array<TextureRegion> frames = new Array<TextureRegion>();
        TextureRegion curRegion = atlas.findRegion("Small Heart Idle (18x14)");

        for (int i = 0; i < 8; i++){
            frames.add(new TextureRegion(curRegion, i*18,0,18,14));
        }
        animIdle = new Animation(0.1f, frames);
        frames.clear();

        curRegion = atlas.findRegion("Small Heart Hit (18x14)");
        for (int i = 0 ; i < 2; i++)
            frames.add(new TextureRegion(curRegion,i*18,0,18,14));
        animHitOn = new Animation(0.1f, frames);

        frames.clear();
    }

    private void defineHeart(float X, float Y) {
        exist = true;
        destroyed = false;
        destroying = false;



        curX = X;
        curY = Y;


    }

    private TextureRegion getFrame(float dt){
        TextureRegion region = (TextureRegion) animIdle.getKeyFrame(stateTimer, true);
        stateTimer += dt;
        return  region;
    }

    @Override
    public void drawMe(SpriteBatch batch) {
        draw(batch);
    }

    @Override
    public void update(float dt) {
        setRegion(getFrame(dt));
        screen.updateQueue.add(this);




    }
}