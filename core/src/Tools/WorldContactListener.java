package Tools;

import Sprites.Enemies.Enemy;
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
                if (player.getUserData().getClass() == Player.class){
                    Body body = ((Player)player.getUserData()).b2body;
                    float direction = 1;
                    if (((Player) player.getUserData()).isRunningRight()) direction = -1;

                    float width = ( (Player) player.getUserData()).getWidth(),height = ( (Player) player.getUserData()).getHeight();
                    body.setLinearVelocity(0,0);
                    body.applyLinearImpulse(  2 * direction,1,body.getPosition().x + width / 2 ,body.getPosition().y  + height / 2,true);
                    ((Enemy)enemy.getUserData()).attack((Player) player.getUserData());
                    Gdx.app.log("Player", "has been booped");
                }

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
