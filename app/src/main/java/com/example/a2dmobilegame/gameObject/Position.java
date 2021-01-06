package com.example.a2dmobilegame.gameObject;

import android.content.res.Resources;

/**
 * Handle the position of gameobject on the screent x,y and directen its face(left or rigth).
 */
public class Position extends Point {
    private boolean faceRight;
    public Position(float x, float y) {
        super(x, y);
        faceRight = true;
    }

    /**
     * Push this gameobject some units aside.
     * @param units number of unist to push(int)
     */
    public void pushAside(int units){
        if (getX()+units > 0 && getX()+units < Resources.getSystem().getDisplayMetrics().widthPixels){
            setX(getX()+units);
        }
    }

    /**
     * Get the directer the gameobject is facing.
     * @return
     */
    public boolean getIsFacingRight(){
        return  faceRight;
    }

    /**
     * Set the direction the gameobject is facing.
     * @param faceRight as boolean.
     */
    public  void  setIsFacingRight(boolean faceRight){
        this.faceRight = faceRight;
    }

    /**
     * Return this position object.
     * @return this object as Position.
     */
    public Position getPosition(){
        return this;
    }
}
