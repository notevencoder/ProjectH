package com.mygdx.game.Scenes;

import Tools.Updatable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
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
        private TextureRegion hudTexture, heartTexture, heartDamagedTexture;
        private float padX, padY;
        private float stateTimer;
        private int lives;
        private boolean damaged = false;
        private Rectangle[] hearts;


        private final float scale = 2;

        Animation animationIdle, animationHit;


        public LiveBar() {
            PlatScreen.updateQueue.addForever(this);


            setSize(scale * 66, scale * 34);
            setPad(15, 15);
            setPosition(0, Platformer.V_HEIGHT - getHeight());

            defineAnimations();
            heartTexture = (TextureRegion) animationIdle.getKeyFrame(0);

            hearts = new Rectangle[3];
            for (int i = 0; i < hearts.length; i++) {
                hearts[i] = new Rectangle();
                hearts[i].x = getX() + padX + scale * (11 + i * (heartTexture.getRegionWidth() - 7));
                hearts[i].y = getY() - padY + 11 * scale;
                hearts[i].width = heartTexture.getRegionWidth() * scale;
                hearts[i].height = heartTexture.getRegionHeight() * scale;
            }
        }

        public void setPad(float x, float y) {
            padX = x;
            padY = y;
        }


        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(hudTexture, getX() + padX, getY() - padY, getWidth(), getHeight());
            for (Rectangle i : hearts)
                batch.draw(heartTexture, i.x, i.y, i.width, i.height);

            if (damaged)
                batch.draw(heartDamagedTexture, hearts[lives].x, hearts[lives].y, hearts[lives].width, hearts[lives].height);


        }


        @Override
        public void update(float dt) {
            heartTexture = (TextureRegion) animationIdle.getKeyFrame(stateTimer, true);
            stateTimer += dt;
            if (lives > screen.getPlayer().getLives()) {
                damaged = true;
                stateTimer = 0;
            }
            lives = screen.getPlayer().getLives();

            if (damaged && animationHit.isAnimationFinished(stateTimer)) {
                damaged = false;
            }
            if (damaged)
                heartDamagedTexture = (TextureRegion) animationHit.getKeyFrame(stateTimer);
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
