package com.example.a2dmobilegame.Transform;

public class Position extends Point {
    private boolean faceRight;
    public Position(float x, float y) {
        super(x, y);
        faceRight = true;
    }

    public boolean getIsFacingRight(){
        return  faceRight;
    }

    public  void  setIsFacingRight(boolean faceRight){
        this.faceRight = faceRight;
    }
}
