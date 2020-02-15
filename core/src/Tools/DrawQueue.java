package Tools;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class DrawQueue {
    private Array<Drawable> arr = new Array<Drawable>();

    public void draw(SpriteBatch batch){
        for (Drawable i : arr)
            i.drawMe(batch);
    }

    public void add(Drawable obj){
        arr.add(obj);
    }
    public void remove(Drawable obj){
        arr.removeValue(obj, true);
    }

}
