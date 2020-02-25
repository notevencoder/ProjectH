package com.mygdx.game.Tools;

public class WorldStateListener {
    private enum State {PLAYER_ENTERING_DOOR};
    private State curState;

    public State getState(){
        return curState;
    }


}
