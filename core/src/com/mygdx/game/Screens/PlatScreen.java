package com.mygdx.game.Screens;

import Sprites.Player;
import Tools.B2DWorldCreator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Platformer;
import com.mygdx.game.Scenes.Hud;

public class PlatScreen implements Screen {

    private Platformer game;

    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;

    // Обьявляем загрузчик tmx карты, саму карту и рендерера
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // объявляем world Box2d
    private World world;
    private Box2DDebugRenderer b2dr;
    private Player player;

    private TextureAtlas atlas;

    public PlatScreen(Platformer GAME){
        atlas = new TextureAtlas("KingAtlas/King.atlas");
        this.game = GAME;
        //new Player(world);
        hud = new Hud(game.batch);

        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(Platformer.V_WIDTH  / Platformer.PPM, Platformer.V_HEIGHT  / Platformer.PPM,  gamecam);

        // инициализация карты
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("test3.tmx");
        renderer = new OrthogonalTiledMapRenderer(map,1 / Platformer.PPM);


        // устанавливаем камеру
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        // инициализация world
        world = new World(new Vector2(0,-10 ),true);
        b2dr = new Box2DDebugRenderer();

        new B2DWorldCreator(world, map);

        player = new Player(world, this);
    }
    public TextureAtlas getAtlas(){
        return atlas;
    }

    public void handleInput(float dt){
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
            player.b2body.applyLinearImpulse(new Vector2(0,4 ),player.b2body.getWorldCenter(),true);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            player.b2body.setLinearVelocity(2,player.b2body.getLinearVelocity().y);
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            player.b2body.setLinearVelocity(-2,player.b2body.getLinearVelocity().y);
        else
            player.b2body.setLinearVelocity(0,player.b2body.getLinearVelocity().y);

    }

    public Viewport getPort(){
        return gamePort;
    }

    public void update (float dt){
        handleInput(dt);

        world.step(1/60f,6,2);

        player.update(dt);

        gamecam.position.x = player.b2body.getPosition().x;

        gamecam.update();
        renderer.setView(gamecam);


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);
        renderer.render();
        b2dr.render(world, gamecam.combined);
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
