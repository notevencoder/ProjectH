package Tools;

import Sprites.Door;
import Sprites.InteractiveObjects;
import Sprites.Enemy;
import Sprites.Platforms;
import Sprites.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Platformer;
import com.mygdx.game.Screens.PlatScreen;

public class WorldContactListener implements ContactListener {
    private PlatScreen screen;
    public WorldContactListener(PlatScreen screen){
        this.screen = screen;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Fixture object;
        Fixture player;

        if (fixtureA.getUserData() == "Head" || fixtureB.getUserData() == "Head")
            Gdx.app.log("contact", "Head begin");
        else if (fixtureA.getUserData() == "Legs" || fixtureB.getUserData() == "Legs")
            Gdx.app.log("contact", "Legs begin");
        else if (fixtureA.getUserData() == "Left" || fixtureB.getUserData() == "Left")
            Gdx.app.log("contact", "Left begin");
        else if (fixtureA.getUserData() == "Right" || fixtureB.getUserData() == "Right")
            Gdx.app.log("contact", "Right begin");
        if (fixtureA.getUserData().getClass() == Door.class || fixtureB.getUserData().getClass() == Door.class){

            if (fixtureA.getUserData().getClass() == Door.class){
                object = fixtureA;
                player = fixtureB;
            }
            else{
                object = fixtureB;
                player = fixtureA;
            }
            PlatScreen.getPlayer().setCanInteractWithNow((InteractiveObjects) object.getUserData());



        }


        int cDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        switch (cDef){
            case Platformer.ENEMY_BIT | Platformer.PLAYER_BIT: {
                Fixture player;
                Fixture enemy;
                if (fixtureA.getFilterData().categoryBits == Platformer.PLAYER_BIT){
                    player = fixtureA;
                    enemy = fixtureB;
                }else {
                    player = fixtureB;
                    enemy = fixtureA;
                }
                if (player.getUserData() == "AttackRight")
                    ((Enemy)enemy.getUserData()).destroyFromRight(true);
                else if (player.getUserData() == "AttackLeft")
                        ((Enemy)enemy.getUserData()).destroyFromLeft(true);

                break;
            }
        }

    }


    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Fixture object;
        Fixture player;

        if (fixtureA.getUserData().getClass() == Door.class || fixtureB.getUserData().getClass() == Door.class){

            if (fixtureA.getUserData().getClass() == Door.class){
                object = fixtureA;
                player = fixtureB;
            }
            else{
                object = fixtureB;
                player = fixtureA;
            }

            PlatScreen.getPlayer().setCanInteractWithNow(null);



        }


        int cDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        switch (cDef){
            case Platformer.ENEMY_BIT | Platformer.PLAYER_BIT: {
                Fixture player;
                Fixture enemy;
                if (fixtureA.getFilterData().categoryBits == Platformer.PLAYER_BIT){
                    player = fixtureA;
                    enemy = fixtureB;
                }else {
                    player = fixtureB;
                    enemy = fixtureA;
                }
                if (player.getUserData() == "AttackRight")
                    ((Enemy)enemy.getUserData()).destroyFromRight(false);
                else if (player.getUserData() == "AttackLeft")
                    ((Enemy)enemy.getUserData()).destroyFromLeft(false);
                break;
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
