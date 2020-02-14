package Tools;

import Sprites.Door;
import Sprites.Platforms;
import Sprites.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Screens.PlatScreen;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA.getUserData() == "Head" || fixtureB.getUserData() == "Head")
            Gdx.app.log("contact", "Head begin");
        else if (fixtureA.getUserData() == "Legs" || fixtureB.getUserData() == "Legs")
            Gdx.app.log("contact", "Legs begin");
        else if (fixtureA.getUserData() == "Left" || fixtureB.getUserData() == "Left")
            Gdx.app.log("contact", "Left begin");
        else if (fixtureA.getUserData() == "Right" || fixtureB.getUserData() == "Right")
            Gdx.app.log("contact", "Right begin");
        if (fixtureA.getUserData().getClass() == Door.class || fixtureB.getUserData().getClass() == Door.class)
            Gdx.app.log("contact", "DOOR");

    }


    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA.getUserData().getClass() == Door.class || fixtureB.getUserData().getClass() == Door.class)
            Gdx.app.log("End.contact", "DOOR");


    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
