package com.mygdx.game.Scenes;

import Tools.Updatable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Platformer;
import com.mygdx.game.Screens.PlatScreen;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private Integer worldTimer;
    private float timeCount;
    private Integer score;
    private PlatScreen screen;

    private LiveBar liveBar;

    public Hud(PlatScreen screen) {
        this.screen = screen;
        SpriteBatch sb = screen.getBatch();
        worldTimer = 300;
        timeCount = 0;
        score = 0;

        viewport = new FitViewport(Platformer.V_WIDTH, Platformer.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);
        liveBar = new LiveBar();
        stage.addActor(liveBar);


    }


    @Override
    public void dispose() {
        stage.dispose();
    }


    private class LiveBar extends Actor implements Updatable {

        private TextureAtlas atlas;
        private TextureRegion hudTexture, heartTexture;
        private float padX, padY;
        private float stateTimer;
        private float lives;


        public static final float scale = 2;

        Animation animationIdle, animationHit;



        public LiveBar() {
//            heart1pos = new Vector2(/*getPosition().x + 18 * scale, getPosition().y + 13 * scale*/ 100, 10);
//            heart2pos = new Vector2(heart1pos.x + 3 * scale, heart1pos.y);
//            heart3pos = new Vector2(heart2pos.x + 3 * scale, heart2pos.y);
            PlatScreen.updateQueue.addForever(this);
            Actor[] actors = new Actor[3];
            for (Actor i : actors) {
                i = new Actor();
            }


            setSize(scale * 66, scale * 34);
            setPad(15, 15);
            setPosition(0, Platformer.V_HEIGHT - getHeight());

            defineAnimations();
            heartTexture = (TextureRegion) animationIdle.getKeyFrame(0);


        }




        public void setPad(float x, float y) {
            padX = x;
            padY = y;
        }


        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(hudTexture, getX() + padX, getY() - padY, getWidth(), getHeight());
            for (int i = 0; i < lives; i++){
                batch.draw(heartTexture,
                         getX() + padX + scale*(11 + i * (heartTexture.getRegionWidth() - 7)),getY() - padY + 11 * scale,
                        heartTexture.getRegionWidth() * scale, heartTexture.getRegionHeight() * scale);
            }
        }


        @Override
        public void update(float dt) {
            lives = screen.getPlayer().getLives();
            heartTexture = (TextureRegion)animationIdle.getKeyFrame(stateTimer, true);
            stateTimer += dt;
        }

        private void defineAnimations() {
            atlas = new TextureAtlas("Hud/Hud.pack");

            hudTexture = new TextureRegion(atlas.findRegion("Live Bar"));

            Array<TextureRegion> frames = new Array<TextureRegion>();
            TextureRegion curRegion = atlas.findRegion("Small Heart Idle (18x14)");

            for (int i = 0; i < 8; i++) {
                frames.add(new TextureRegion(curRegion, i * 18, 0, 18, 14));
            }
            animationIdle = new Animation(0.1f, frames);
            frames.clear();

            curRegion = atlas.findRegion("Small Heart Hit (18x14)");
            for (int i = 0; i < 2; i++)
                frames.add(new TextureRegion(curRegion, i * 18, 0, 18, 14));
            animationHit = new Animation(0.1f, frames);

            frames.clear();
        }
    }
}
