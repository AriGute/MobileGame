package com.example.a2dmobilegame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.example.a2dmobilegame.Transform.Position;


public class Character extends Position implements DrawAble {
    private Bitmap image;

    public Character(Resources res ,float x, float y){
        super(x,y);
        image = BitmapFactory.decodeResource(res , R.drawable.red_test);
    }

    public void update(){

    }

    /**
     * Move the character to a new position.
     * @param x direction parameter.
     * @param y direction parameter.
     */
    public void move(float x,float y){
        float scale = (float) 0.05;
        int maxValue = 10;
        float speedX = Math.abs(Math.abs((x*scale))>maxValue? maxValue : (x*scale));
        float speedY = Math.abs(Math.abs((y*scale))>maxValue? maxValue : (y*scale))/2;


        Log.d("[Move]", "move: x:"+x+", y: "+y+", speedX: "+speedX+", speedY: "+speedY);

        if (x != 0) {
            setX(getX() - (x / Math.abs(x)) * speedX);
        }
        if (y != 0) {
            setY(getY() - (y / Math.abs(y)) * speedY);
        }

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, getX(), getY(), null);
    }
}
