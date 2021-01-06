package com.example.a2dmobilegame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.example.a2dmobilegame.gameObject.Animation;
import com.example.a2dmobilegame.gameObject.BoxCollider;
import com.example.a2dmobilegame.gameObject.Position;

public class Enemy extends Position implements DrawAble{
    private Bitmap currentFrame;
    private Animation walk_anim;
    private Animation hit_anim;

    private BoxCollider collider;
    private Attributes attr;

    private Position target;
    private float maxSpeed = 15;
    private float speedBuffer = 1;

    private float hitTimer = 0;
    private float hitRate = 2;

    public Enemy(Resources res, float x, float y, Position target) {
        super(x, y);
        currentFrame = BitmapFactory.decodeResource(res , R.drawable.enemy_move0);
        walk_anim = new Animation(0.5f);
        walk_anim.addFrame(BitmapFactory.decodeResource(res , R.drawable.enemy_move0));
        walk_anim.addFrame(BitmapFactory.decodeResource(res , R.drawable.enemy_move1));

        hit_anim = new Animation(1);
        hit_anim.addFrame(BitmapFactory.decodeResource(res, R.drawable.enemy_hit0));
        hit_anim.addFrame(BitmapFactory.decodeResource(res, R.drawable.enemy_hit1));



        collider = new BoxCollider(getPoint(), currentFrame.getWidth(), currentFrame.getHeight());
        attr = new Attributes(100, 10);
        setTarget(target);
    }

    public void Update(){
        if(target != null){
            float speed = 0;
            if(distance(target) > currentFrame.getWidth()/2) {
                speed = speedBuffer / MainGameThread.getDeltaTime();

                hitTimer = 0;

                if(speedBuffer < maxSpeed){
                    speedBuffer+= 2/MainGameThread.getDeltaTime();
                }
                //Log.d("[speedBuffer]", "Update: buffer: "+speedBuffer+", speed: "+speed);
            }else {
                hit();
            }
            if(hitTimer <=0 ){
                currentFrame = walk_anim.getFrame();
                followTarget(speed);
            }
        }
        attr.setPos((int) getX(), (int)getY(), (int) collider.getWidth());
    }

    /**
     * move to the possition of the target.
     * @param speed movments speed as float
     */
    private void followTarget(float speed){
        //Log.d("[Enemy]", "Update: pos: x-> " + getX()+" , y-> "+getY());
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

    /**
     * hit and reduce health points to the target.
     */
    private void hit(){
        if(hitTimer > 0){
            hitTimer -= 1/MainGameThread.getDeltaTime();
            currentFrame = hit_anim.getFrame();
            //Log.d("[enemy hit]", "hit: is this null? :" + hit_anim.getFrame());
        }else {
            hit_anim.resetAnim();
            hitTimer = hitRate;
            GameView.player.getAttr().DeliverDamage(attr.getDamage());
        }
    }

    /**
     * reset speed buffer.
     */
    public void resetSpeed(){
        speedBuffer = 1;
    }

    /**
     * set the target that this unit is need follow.
     * @param target as Position.
     */
    public void setTarget(Position target){
            this.target = target;
    }

    /**
     * return attributes(used to use attr methords such as deliverDamage etc..).
     * @return Attributes.
     */
    public Attributes getAttr(){
        return attr;
    }

    @Override
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(currentFrame, getX(), getY(), null);
        attr.draw(canvas);
    }
}
