package com.example.a2dmobilegame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.example.a2dmobilegame.Transform.Animation;
import com.example.a2dmobilegame.Transform.BoxCollider;
import com.example.a2dmobilegame.Transform.Position;

public class Enemy extends Position implements DrawAble{
    private Bitmap currentFrame;
    private Animation walk_anim;
    private BoxCollider collider;

    private Position target;
    private float moveSpeed = 15;
    public Enemy(Resources res, float x, float y) {
        super(x, y);
        currentFrame = BitmapFactory.decodeResource(res , R.drawable.enemy_move0);
        walk_anim = new Animation(0.5f);
        walk_anim.addFrame( BitmapFactory.decodeResource(res , R.drawable.enemy_move0));
        walk_anim.addFrame( BitmapFactory.decodeResource(res , R.drawable.enemy_move1));

        collider = new BoxCollider(getPoint(), currentFrame.getWidth(), currentFrame.getHeight());
    }

    public void Update(){
        if(target != null){
            float speed = 0;
            if(distance(target) > currentFrame.getWidth()/2) {
                Log.d("[Enemy]", "Update: distance - " + distance(target));
                speed = moveSpeed / MainGameThread.getDeltaTime();
            }
            currentFrame = walk_anim.getFrame();
            Log.d("[Enemy]", "Update: pos: x-> " + getX()+" , y-> "+getY());
            if(target.getX() > this.getX()){
                setX(getX()+speed);
            }else {
                setX(getX()-speed);
            }

            if(target.getY() > this.getY()){
                setY(getY()+speed);
            }else {
                setY(getY()-speed);
            }
        }
    }

    public void setTarget(Position target){
            this.target = target;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(currentFrame, getX(), getY(), null);
    }
}
