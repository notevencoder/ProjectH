package Tools;

import com.badlogic.gdx.utils.Array;

public class UpdateQueue {

    Array<Updatable> arr = new Array<Updatable>();
    Array<Updatable> arrForever = new Array<Updatable>();

    public void add(Updatable obj){
        arr.add(obj);
    }
    public void addForever(Updatable obj){
        arrForever.add(obj);
    }
    public void update(float dt){
        for (Updatable i : arr)
            i.update(dt);
        arr.clear();
        for (Updatable i : arrForever)
            i.update(dt);
    }

}
