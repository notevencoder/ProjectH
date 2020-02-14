package Tools;

import com.badlogic.gdx.utils.Array;

public class UpdateQueue {

    Array<Updatable> arr = new Array<Updatable>();

    public void add(Updatable obj){
        arr.add(obj);
    }
    public void update(float dt){
        for (Updatable i : arr)
            i.update(dt);
        arr.clear();
    }

}
