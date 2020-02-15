package Sprites;

import Tools.Updatable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.StreamUtils;
import com.mygdx.game.Platformer;
import com.mygdx.game.Screens.PlatScreen;

public class Platforms extends InteractiveObjects implements Updatable {
    public boolean stepped = false;

    public Platforms(PlatScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setSensor(true);
        fixture.setUserData(this);
        SetCategoryFilter(Platformer.PLATFORM_BIT);
        PlatScreen.updateQueue.addForever(this);

    }

    @Override
    public void Interact(Player player) {

    }

    @Override
    public void update(float dt){
        if (screen.getPlayer().b2body.getPosition().y - screen.getPlayer().HEIGHT / 2 < this.body.getPosition().y || Gdx.input.isKeyPressed(Input.Keys.DOWN))
            fixture.setSensor(true);
        else
            fixture.setSensor(false);
    }
}
