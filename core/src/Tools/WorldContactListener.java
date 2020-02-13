package Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA.getUserData() == "Head" || fixtureB.getUserData() == "Head")
            Gdx.app.log("contact", "Head begin");
        if (fixtureA.getUserData() == "Legs" || fixtureB.getUserData() == "Legs")
            Gdx.app.log("contact", "Legs begin");
        if (fixtureA.getUserData() == "Left" || fixtureB.getUserData() == "Left")
            Gdx.app.log("contact", "Left begin");
        if (fixtureA.getUserData() == "Right" || fixtureB.getUserData() == "Right")
            Gdx.app.log("contact", "Right begin");
    }


    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
