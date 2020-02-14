package Sprites;

import Tools.Updatable;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.StreamUtils;
import com.mygdx.game.Platformer;

public class Platforms extends InteractiveObjects implements Updatable {
    public boolean stepped = false;
    public Platforms(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        fixture.setSensor(true);
        fixture.setUserData(this);
        SetCategoryFilter(Platformer.PLATFORM_BIT);
    }
    @Override
    public void update(float dt){
        //Доделать
        if (stepped)
            fixture.setSensor(false);
        else
            fixture.setSensor(true);
    }
}
