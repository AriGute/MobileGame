package com.example.a2dmobilegame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

/**
 * This class add attributes to gameObjects such as health, damage,  etc..
 */
public class Attributes implements DrawAble {
    private int currentHP;
    private int maxHP;
    private int damage;

    private int posX;
    private int posY;
    private int fixWidth;

    private boolean isAlive;

    public Attributes(int maxHP, int damage){
        this.maxHP = maxHP;
        this.currentHP = maxHP;

        this.damage = damage;
        this.isAlive = true;
    }

    /**
     * Set the position of the health bar(above the gameobject on the canvas).
     * @param x x position.
     * @param y y position
     * @param width width of the bitmap of the gameObject that oun the attributes.
     */
    public void setPos(int x, int y, int width){
        this.posX = x;
        this.posY = y;
        this.fixWidth = width;
    }

    /**
     *
     * @return the current hp as int.
     */
    public int getCurrentHP() {
        return currentHP;
    }

    /**
     * Method to reduce the current health point(currentHP).
     * @param damage damage(int) to reduce from currentHP.
     */
    public void DeliverDamage(int damage){
        if(currentHP-damage > 0) {
            currentHP -= damage;
        }else {
            isAlive = false;
        }
        Log.d("[attr]", "DeliverDamage: current hp: "+currentHP);
    }

    /**
     *
     * @return if agent is alive as boolean.
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     *
     * @return damage as int.
     */
    public int getDamage() {
        return damage;
    }

    @Override
    public void draw(Canvas canvas) {
        Rect br = new Rect(posX-10,posY-50,posX+(int)(fixWidth*1.1),posY-15);
        Paint bp = new Paint(0);
        bp.setColor(Color.rgb(0,0,0));
        canvas.drawRect(br,bp);

        Rect r = new Rect(posX,posY-40,posX+(fixWidth/90)*(getCurrentHP()),posY-25);
        Paint p = new Paint(0);
        p.setColor(Color.rgb(255,0,0));
        canvas.drawRect(r,p);
    }
}
