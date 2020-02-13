package Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Platformer;

public class Platforms extends InteractiveObjects {
    public Platforms(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        fixture.setUserData(this);
        SetCategoryFilter(Platformer.PLATFORM_BIT);
    }
}
