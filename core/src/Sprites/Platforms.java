package Sprites;

import Tools.Updatable;
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
    private PlatScreen screen;
    public Platforms(World world, TiledMap map, Rectangle bounds, PlatScreen screen) {
        super(world, map, bounds);
        fixture.setSensor(true);
        fixture.setUserData(this);
        SetCategoryFilter(Platformer.PLATFORM_BIT);
        PlatScreen.updateQueue.addForever(this);
        this.screen = screen;
    }

    @Override
    public void update(float dt){
        if (screen.getPlayer().b2body.getPosition().y - screen.getPlayer().HEIGHT / 2 < this.body.getPosition().y)
            fixture.setSensor(true);
        else
            fixture.setSensor(false);
    }
}
