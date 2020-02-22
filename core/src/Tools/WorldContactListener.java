package Tools;

import Sprites.InteractiveObjects;
import Sprites.Enemies.EnemyPig;
import Sprites.Items.Item;
import Sprites.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Platformer;
import com.mygdx.game.Screens.PlatScreen;

public class WorldContactListener implements ContactListener {
    private PlatScreen screen;

    public WorldContactListener(PlatScreen screen) {
        this.screen = screen;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Fixture object;
        Fixture player;


        int cDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        switch (cDef) {
            case Platformer.ENEMY_BIT | Platformer.PLAYER_BIT:

                Fixture enemy;
                if (fixtureA.getFilterData().categoryBits == Platformer.PLAYER_BIT) {
                    player = fixtureA;
                    enemy = fixtureB;
                } else {
                    player = fixtureB;
                    enemy = fixtureA;
                }
                if (player.getUserData() == "AttackRight")
                    ((EnemyPig) enemy.getUserData()).destroyFromRight(true);
                else if (player.getUserData() == "AttackLeft")
                    ((EnemyPig) enemy.getUserData()).destroyFromLeft(true);

                break;
            case Platformer.PLAYER_BIT | Platformer.ITEM_BIT:
                if (fixtureA.getUserData().getClass() == Player.class) {
                    player = fixtureA;
                    object = fixtureB;
                } else if (fixtureB.getUserData().getClass() == Player.class) {
                    player = fixtureB;
                    object = fixtureA;
                } else
                    break;

                ((Item) object.getUserData()).onTake();
                break;
            case Platformer.PLAYER_BIT | Platformer.DOOR_BIT: {
                if (fixtureB.getUserData().getClass() == Player.class) {
                    object = fixtureA;
                    player = fixtureB;
                } else if (fixtureA.getUserData().getClass() == Player.class) {
                    object = fixtureB;
                    player = fixtureA;
                } else
                    break;

                ((Player) player.getUserData()).setCanInteractWithNow((InteractiveObjects) object.getUserData());

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

        int cDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        switch (cDef) {
            case Platformer.ENEMY_BIT | Platformer.PLAYER_BIT: {

                Fixture enemy;
                if (fixtureA.getFilterData().categoryBits == Platformer.PLAYER_BIT) {
                    player = fixtureA;
                    enemy = fixtureB;
                } else {
                    player = fixtureB;
                    enemy = fixtureA;
                }
                if (player.getUserData() == "AttackRight")
                    ((EnemyPig) enemy.getUserData()).destroyFromRight(false);
                else if (player.getUserData() == "AttackLeft")
                    ((EnemyPig) enemy.getUserData()).destroyFromLeft(false);
                break;

            }
            case Platformer.PLAYER_BIT | Platformer.DOOR_BIT: {
                if (fixtureB.getUserData().getClass() == Player.class) {
                    object = fixtureA;
                    player = fixtureB;
                } else if (fixtureA.getUserData().getClass() == Player.class) {
                    object = fixtureB;
                    player = fixtureA;
                } else
                    break;

                ((Player) player.getUserData()).setCanInteractWithNow(null);

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
