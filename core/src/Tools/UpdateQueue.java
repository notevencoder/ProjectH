package Tools;

import Sprites.Door;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class UpdateQueue {

    private Array<Updatable> arr = new Array<Updatable>();
    private Array<Updatable> arrForever = new Array<Updatable>();
    private Array<Sprite> drawable = new Array<Sprite>();

    public void add(Updatable obj){
        arr.add(obj);
    }
    public void addForever(Updatable obj){
        arrForever.add(obj);
    }
    public void addToDrawable(Sprite door){
        drawable.add(door);
    }

    public void Draw(SpriteBatch batch){
        for (Sprite i : drawable)
            i.draw(batch);
    }

    public void update(float dt){
        for (Updatable i : arr)
            i.update(dt);
        arr.clear();
        for (Updatable i : arrForever)
            i.update(dt);
    }

}
