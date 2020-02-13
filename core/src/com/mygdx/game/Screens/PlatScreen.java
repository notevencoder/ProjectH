package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
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

    public PlatScreen(Platformer GAME){
        this.game = GAME;

        hud = new Hud(game.batch);

        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(Platformer.V_WIDTH, Platformer.V_HEIGHT,  gamecam);

        // инициализация карты
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("test3.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);

        //устанавливаем камеру
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
    }

    public void handleInput(float dt){
        if (Gdx.input.isTouched())
            gamecam.position.x += 100 * dt;

    }

    public void update (float dt){
        handleInput(dt);
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
