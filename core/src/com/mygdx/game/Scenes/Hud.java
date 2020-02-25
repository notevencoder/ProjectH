package com.mygdx.game.Scenes;

import com.mygdx.game.Tools.Updatable;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
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
    protected Integer score;
    private PlatScreen screen;

    private LiveBar liveBar;
    private DiamondBar diamondBar;

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
        diamondBar = new DiamondBar(liveBar);
        stage.addActor(liveBar);
        stage.addActor(diamondBar);
        stage.setDebugAll(true);



    }


    @Override
    public void dispose() {
        stage.dispose();
    }


    protected class LiveBar extends Actor implements Updatable {

        private TextureAtlas atlas;
        private TextureRegion hudTexture, heartTexture, heartDamagedTexture;
        private float padX, padY;
        private float stateTimer;
        private int lives;
        private boolean damaged = false;
        private Rectangle[] hearts;


        public static final float scale = 1.5f ;

        Animation animationIdle, animationHit;


        public LiveBar() {
            PlatScreen.updateQueue.addForever(this);


            setSize(scale * 66, scale * 34);
            setPad(10, 10);
            setPosition(0 + padX, Platformer.V_HEIGHT - getHeight()- padY);

            defineAnimations();
            heartTexture = (TextureRegion) animationIdle.getKeyFrame(0);

            hearts = new Rectangle[3];
            for (int i = 0; i < hearts.length; i++) {
                hearts[i] = new Rectangle();
                hearts[i].x = getX()  + scale * (i+1)*(11 + lives * (heartTexture.getRegionWidth() - 7));
                hearts[i].y = getY()  + 11 * scale;
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
            batch.draw(hudTexture, getX() , getY() , getWidth(), getHeight());
            for (int i = 0; i < lives; i++) {
                batch.draw(heartTexture, hearts[i].x, hearts[i].y-1, hearts[i].width, hearts[i].height);
            }




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
    public void increaseScore(){
        score++;
    }


    protected class DiamondBar extends  Actor implements Updatable{

        private LiveBar bar;
        private TextureAtlas atlas;
        private float stateTimer;
        private Animation animDiamond;
        private float padY = 1 ;
        private TextureRegion IdleRegion;

        private Array<TextureRegion> numbers = new Array<TextureRegion>();


        public DiamondBar(LiveBar bar){

            this.bar = bar;
            screen.updateQueue.addForever(this);

            setSize(18 * bar.scale,14 * bar.scale);
            setPosition(bar.getX() + bar.getWidth() / 4, bar.getY() - getHeight());

            defineAnimations();
        }

        public void defineAnimations(){

            atlas = new TextureAtlas("Hud/Hud.pack");
            stateTimer = 0;

            TextureRegion region = new TextureRegion(atlas.findRegion("Small Diamond (18x14)"));
            Array<TextureRegion> frames = new Array<TextureRegion>();

            for (int i = 0; i < 8; i++)
                frames.add(new TextureRegion(region, i*18,0,18,14));

            animDiamond = new Animation(0.1f, frames);

            frames.clear();

            region = new TextureRegion(atlas.findRegion("Numbers (6x8)"));

            numbers.add(new TextureRegion(region,54,0,6,8));
            for (int i = 1; i < 10; i++)
                numbers.add(new TextureRegion(region,(i-1)*6,0,6,8));





        }






        public void drawScore(int score, Batch batch){
            int buff = score, length = 0;
            if (score != 0){
                while (buff > 0){
                    length++;
                    buff /= 10;
                }


                int[] mas = new int[length];
                int i = length -1;
                while(score > 0) {
                    mas[i] = score % 10;
                    score/=10;
                    i--;

                }

                for (i = 0 ; i < length; i++){
                    batch.draw(numbers.get(mas[i]),getX() + getWidth() + i * 6 * bar.scale,getY() + getHeight() / 4,6 * bar.scale ,8 * bar.scale);

                }
            }else {
                batch.draw(numbers.get(0),getX() + getWidth(),getY() + getHeight() / 4 ,6 * bar.scale ,8 * bar.scale);
            }

        }

        @Override
        public void update(float dt) {
            IdleRegion = (TextureRegion) animDiamond.getKeyFrame(stateTimer, true);

            stateTimer += dt;


        }

        @Override
        public void draw(Batch batch, float parentAlpha){
            batch.draw(IdleRegion, getX() , getY() , getWidth(),  getHeight());


            drawScore(score, batch);

        }
    }
}
