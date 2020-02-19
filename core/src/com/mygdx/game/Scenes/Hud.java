package com.mygdx.game.Scenes;

import Tools.Drawable;
import Tools.Updatable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
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



    private class LiveBar extends Actor {

        private TextureAtlas atlas;
        private TextureRegion tx;
        private PlatScreen screen;
        private float corX, corY;
        private float height, width;
        private float padX, padY;
        private HudHeart Heart1,Heart2,Heart3;
        public Vector2 heart1pos, heart2pos, heart3pos;
        public static final float scale = 2;


        public LiveBar(PlatScreen screen){
            this.screen = screen;

            heart1pos = new Vector2(/*getPosition().x + 18 * scale, getPosition().y + 13 * scale*/ 100, 10);
            heart2pos = new Vector2(heart1pos.x + 3 * scale, heart1pos.y);
            heart3pos = new Vector2(heart2pos.x + 3 * scale, heart2pos.y);

            Heart1 = new HudHeart(screen, heart1pos.x, heart1pos.y);
            Heart2 = new HudHeart(screen, heart2pos.x, heart2pos.y);
            Heart3 = new HudHeart(screen, heart3pos.x, heart3pos.y);

            setBounds(64 * scale,32 * scale);
            setPad(15,15);
            setPosition(0, Platformer.V_HEIGHT - height);

            atlas  = new TextureAtlas("Hud/Hud.pack");
            tx = new TextureRegion(atlas.findRegion("Live Bar"));
        }

        public void setPosition(float x, float y){
            corX = x;
            corY = y;
        }

        public Vector2 getPosition(){
            return new Vector2(corX,corY);

        }

        public void setBounds(float w, float h){
            height = h;
            width = w;

        }
        public void setPad(float x, float y){
            padX = x;
            padY = y;

        }

        @Override
        public void draw(Batch batch, float parentAlpha) {

            batch.draw(tx, corX + padX,corY - padY, width,height);

        }
    }




    public Hud(PlatScreen screen){
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



        stage.addActor(new LiveBar(screen));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
