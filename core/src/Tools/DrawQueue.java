package Tools;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class DrawQueue {
    private Array<Array<Drawable>> arr = new Array<Array<Drawable>>();
    private Array<Array<Drawable>> Arr = new Array<Array<Drawable>>();

    public void draw(SpriteBatch batch){
        for (int j = arr.size - 1; j >=0; j--)
            for (Drawable i : arr.get(j))
                i.drawMe(batch);
        for (int j = Arr.size - 1; j >=0; j--)
            for (Drawable i : Arr.get(j))
                i.drawMe(batch);
            Arr.clear();
    }

    public void add(Drawable obj, int layer){
        while (arr.size <= layer + 1)
            arr.add(new Array<Drawable>());
        arr.get(layer).add(obj);
    }
    public void addOnce(Drawable obj, int layer){
        while (Arr.size <= layer + 1)
            Arr.add(new Array<Drawable>());
        Arr.get(layer).add(obj);
    }
    public void remove(Drawable obj){
        for (Array<Drawable> i : arr)
            i.removeValue(obj, true);
    }

}
