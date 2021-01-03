package com.example.a2dmobilegame.gameObject;

import android.content.res.Resources;

public class Position extends Point {
    private boolean faceRight;
    public Position(float x, float y) {
        super(x, y);
        faceRight = true;
    }

    public void pushAside(int units){
        if (getX()+units > 0 && getX()+units < Resources.getSystem().getDisplayMetrics().widthPixels){
            setX(getX()+units);
        }
    }

    public boolean getIsFacingRight(){
        return  faceRight;
    }

    public  void  setIsFacingRight(boolean faceRight){
        this.faceRight = faceRight;
    }
    public Position getPosition(){
        return this;
    }
}
