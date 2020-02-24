package com.mygdx.game.Screens;

import Sprites.Player;
import Tools.B2DWorldCreator;
import Tools.DrawQueue;
import Tools.UpdateQueue;
import Tools.WorldContactListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    private AssetManager manager;

    // объявляем world Box2d
    private World world;
    private Box2DDebugRenderer b2dr;
    private Player player;
    //EnemyPig enemy;

    private TextureAtlas atlas;
    public static UpdateQueue updateQueue;
    public static DrawQueue drawQueue;


    private MessageManager messageManager;

    public PlatScreen(Platformer GAME, AssetManager manager){

        updateQueue = new UpdateQueue();
        drawQueue = new DrawQueue();

        atlas = new TextureAtlas("KingAtlas/King.atlas");
        this.game = GAME;
        this.manager = manager;

        hud = new Hud(this);

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
        world.setContactListener(new WorldContactListener(this));
        b2dr = new Box2DDebugRenderer();

        new B2DWorldCreator( this);

        player = new Player(this);
        drawQueue.add(player, 0);

        defineMsgDispatcher();
    }

    public void update (float dt){
        messageManager.update();

        handleInput(dt);


        world.step(1/60f,6,2);
        updateQueue.update(dt);
       // enemy.update(dt);
        player.update(dt);

        gamecam.position.x = player.b2body.getPosition().x;

        gamecam.update();
        renderer.setView(gamecam);


    }

    public void handleInput(float dt){
        boolean fl = false;

        if (Gdx.input.isKeyJustPressed(Input.Keys.F) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            messageManager.dispatchMessage(Platformer.MSG_F);
            fl = true;
        }

        if ((Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W))
            //&& (getState() != Player.State.FALLING && getState() != State.JUMPING)
        ) {
            messageManager.dispatchMessage(Platformer.MSG_UP);
            fl = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            messageManager.dispatchMessage(Platformer.MSG_RIGHT);
            fl = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            messageManager.dispatchMessage(Platformer.MSG_LEFT);
            fl = true;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            fl = true;
            messageManager.dispatchMessage(Platformer.MSG_E);
        }

        if (!fl) messageManager.dispatchMessage(Platformer.MSG_DEFAULT);


        //player.handleInput(dt);
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
        drawQueue.draw(game.batch);
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    public AssetManager getManager(){
        return manager;
    }
    public Hud getHud(){
        return hud;

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        b2dr.dispose();
        world.dispose();

    }

    public Player getPlayer(){
        return player;
    }

    public World getWorld() {
        return world;
    }

    public TiledMap getMap() {
        return map;
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    public SpriteBatch getBatch(){
        return game.batch;
    }

    public Viewport getPort(){
        return gamePort;
    }

    public void defineMsgDispatcher(){
        messageManager = MessageManager.getInstance();

        messageManager.addListeners(player,
                Platformer.MSG_DOWN, Platformer.MSG_UP,Platformer.MSG_LEFT,Platformer.MSG_RIGHT,
                Platformer.MSG_E, Platformer.MSG_F, Platformer.MSG_DEFAULT);
    }

}
