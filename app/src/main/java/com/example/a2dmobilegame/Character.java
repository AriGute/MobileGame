package com.example.a2dmobilegame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.example.a2dmobilegame.gameObject.Animation;
import com.example.a2dmobilegame.gameObject.BoxCollider;
import com.example.a2dmobilegame.gameObject.Position;


public class Character extends Position implements DrawAble {
    private final int BACK_GROUND_BOUND_FIX = 50;

    private Bitmap currentFrame;
    private Animation walk_anim;
    private Animation hit_anim;

    private Bitmap idle;

    private BoxCollider collider;
    private Attributes attr;
    private boolean walking = false;

    private  float hitRate = 2;
    private float hitTimer = 0;

    public Character(Resources res ,float x, float y){
        super(x,y);
        idle = BitmapFactory.decodeResource(res , R.drawable.character_idle);

        walk_anim = new Animation(0.3f);
        walk_anim.addFrame( BitmapFactory.decodeResource(res , R.drawable.character_step0));
        walk_anim.addFrame( BitmapFactory.decodeResource(res , R.drawable.character_step1));

        hit_anim = new Animation(0.8f);
        hit_anim.addFrame( BitmapFactory.decodeResource(res , R.drawable.character_hit));
        hit_anim.addFrame( BitmapFactory.decodeResource(res , R.drawable.character_hit0));

        currentFrame = idle;
        collider = new BoxCollider(getPoint(), idle.getWidth(), idle.getHeight());
        attr = new Attributes(100, 10);

    }

    /**
     * Update function is called each frame.
     */
    public void update(){
        if(hitTimer > 0){
            hitTimer -= 1/MainGameThread.getDeltaTime();
            currentFrame = hit_anim.getFrame();
        }else {
            currentFrame = idle;
            hit_anim.resetAnim();
            if(walking == true){
                currentFrame = walk_anim.getFrame();
            }else {
                walk_anim.resetAnim();
            }
        }

        if(getIsFacingRight()) {
            currentFrame = Bitmap.createScaledBitmap(currentFrame, currentFrame.getWidth(), currentFrame.getHeight(), false);
        }else {
            currentFrame = Bitmap.createScaledBitmap(currentFrame, -currentFrame.getWidth(), currentFrame.getHeight(), false);
        }
        attr.setPos((int) getX(), (int)getY(), (int) collider.getWidth());
    }

    /**
     * Move the character to a new position.
     * @param x direction parameter.
     * @param y direction parameter.
     */
    public void move(float x,float y){
        if(hitTimer <= 0f) {
            float scale = (float) 0.05;
            int maxValue = 10;
            float speedX = Math.abs(Math.abs((x * scale)) > maxValue ? maxValue : (x * scale));
            float speedY = Math.abs(Math.abs((y * scale)) > maxValue ? maxValue : (y * scale)) / 2;

            float nextX = 0;
            float nextY = 0;


            if (x != 0) {
                nextX = getX() - (x / Math.abs(x)) * speedX;
            }
            if (y != 0) {
                nextY = getY() - (y / Math.abs(y)) * speedY;
            }

            if (inBounds(nextX, nextY)) {
                if (speedX > 0) {
                    setIsFacingRight(true);
                } else {
                    setIsFacingRight(false);
                }

                //Log.d("[Move]", "move: x:" + x + ", y: " + y + ", speedX: " + speedX + ", speedY: " + speedY);
                if (x != 0) {
                    setX(nextX);
                }
                if (y != 0) {
                    setY(nextY);
                }
            }

            if(x < 0){
                setIsFacingRight(true);
            }else {
                setIsFacingRight(false);
            }
            //Log.d("[facing]", "move: is facing rigth: "+getIsFacingRight());
        }

    }

    public Attributes getAttr(){
        return attr;
    }

    public void hit(){
        if(hitTimer <= 0f) {
            hitTimer = hitRate;
            for (Enemy enemy : GameView.enemyList) {
                Log.d("[hit dis]", "hit: dis: "+distance(enemy.getPosition()));
                if(distance(enemy.getPosition()) < currentFrame.getWidth()){
                    if(getIsFacingRight()){
                        if(enemy.getX()>getX()){
                            enemy.getAttr().DeliverDamage(attr.getDamage());
                            enemy.pushAside(30);
                            enemy.resetSpeed();

                        }
                    }else {
                        if(enemy.getX()<getX()){
                            enemy.getAttr().DeliverDamage(attr.getDamage());
                            enemy.pushAside(-30);
                            enemy.resetSpeed();
                        }
                    }
                }
            }
        }
    }

    /**
     * Check if this character is in the game bounds.
     * @param x character next step on the x axis.
     * @param y character next step on the y axis.
     * @return true if the character is in the game boundaries.
     */
    private boolean inBounds(float x, float y){
        int width=Resources.getSystem().getDisplayMetrics().widthPixels;
        int height=Resources.getSystem().getDisplayMetrics().heightPixels;
        if(x >= width- idle.getWidth() || x <= 0){
            return false;
        }
        if(y >= height- idle.getHeight() || y + idle.getHeight()/2 <= height/3+BACK_GROUND_BOUND_FIX){
            return  false;
        }
        return true;
    }

    public void isWalking(boolean walk){
        this.walking = walk;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(currentFrame, getX(), getY(), null);

        //draw health bar
        attr.draw(canvas);
    }
}
