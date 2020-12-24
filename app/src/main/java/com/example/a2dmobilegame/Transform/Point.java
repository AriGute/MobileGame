package com.example.a2dmobilegame.Transform;

public class Point {
    private float x;
    private float y;

    public Point(float x, float y){
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @param x set parameter x (float)
     */
    public void setX(float x){
        this.x = x;
    }

    /**
     *
     * @return current x value(float)
     */
    public float getX(){
        return this.x;
    }

    /**
     *
     * @param y set parameter y (float)
     */
    public void setY(float y){
        this.y = y;
    }

    /**
     *
     * @return current x value(float)
     */
    public float getY(){
        return this.y;
    }

    /**
     *
     * @param other other Point.
     * @return distance between this point and other point as float.
     */
    public  float distance(Point other){
        return (float) Math.sqrt(Math.pow(other.getX(),2) + Math.pow(other.getY(),2));
    }

    protected Point getThisPoint(){
        return this;
    }
}
