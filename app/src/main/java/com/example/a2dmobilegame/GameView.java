package com.example.a2dmobilegame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback{
    //TODO: HP class and damage get/give.
    //TODO: player attack/get hit and enemy attack/get hit.
    //TODO: enemy spawner.

    private MainGameThread mainThread;

    private boolean isTouch = false;
    private Bitmap backGround;
    private Bitmap resizedBackGround;

    private Character player;
    private Enemy enemy;

    boolean attackButtonIsDown = false;
    float startX = 0;
    float startY = 0;
    float currentX = 0;
    float currentY = 0;
    float distance = 0;

    int screenWidth = 0;
    int screenHeight = 0;



    /**
     * GameView constructor.
     * @param context current state of the application(Context).
     */
    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);

        mainThread = new MainGameThread(getHolder(), this);
        setFocusable(true);
    }



    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    /**
     * Create and handle the surface thread wich(handle the canvas).
     * @param surfaceHolder
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
         screenWidth=Resources.getSystem().getDisplayMetrics().widthPixels;
         screenHeight=Resources.getSystem().getDisplayMetrics().heightPixels;

        backGround = BitmapFactory.decodeResource(getResources() , R.drawable.forest_back_ground);
        resizedBackGround = Bitmap.createScaledBitmap(
                backGround, screenWidth, screenHeight, false);

        player = new Character(getResources(), screenWidth/8, screenHeight/2);
        enemy = new Enemy(getResources(), screenWidth/2, screenHeight/2);
        enemy.setTarget(player.getPosition());

        mainThread.setRunning(true);
        mainThread.start();
    }

    /**
     * Handle the destruction of the surface thread.
     * @param surfaceHolder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        while (retry){
            try {
                mainThread.setRunning(false);
                mainThread.join();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            retry = false;
        }
    }

    /**
     * This method running once per frame and should handle every.
     */
    public void update(){
        player.update();
        enemy.Update();
        if(isTouch){
            player.move(startX-currentX, startY-currentY);
        }
    }


    /**
     * Handle all the tuck inputs.
     * @param event current tuch.
     * @return boolean.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();
        if(event.getX() < screenWidth/2){
            //Left side of the screen is the joystick for character movment.
            switch (eventaction) {
                case MotionEvent.ACTION_DOWN:
                    //Log.d("[Tuch]", "onTouchEvent: "+ "ACTION_DOWN AT COORDS "+"X: "+X+", Y: "+Y);
                    startX = event.getX();
                    startY = event.getY();
                    currentX = event.getX();
                    currentY = event.getY();
                    distance = 1;
                    isTouch = true;
                    player.isWalking(true);
                    break;

                case MotionEvent.ACTION_MOVE:
                    //Log.d("[Tuch]", "onTouchEvent: "+ "ACTION_DOWN AT COORDS "+"X: "+X+", Y: "+Y+", distance "+distance);
                    distance = (float) Math.sqrt(Math.pow(startX - startX, 2) + Math.pow(startY - startY, 2));
                    currentX = event.getX();
                    currentY = event.getY();
                    break;

                case MotionEvent.ACTION_UP:
                    //Log.d("[Tuch]", "onTouchEvent: "+ "ACTION_DOWN AT COORDS "+"X: "+X+", Y: "+Y);
                    startX = 0;
                    startY = 0;
                    isTouch = false;
                    distance = 1;
                    player.isWalking(false);
                    break;
            }
        }

        if(event.getX() > screenWidth/2){
            //Rigth side of the screen is the attack button.
            switch (eventaction) {
                case MotionEvent.ACTION_DOWN:
                    if(attackButtonIsDown == false) {
                        Log.d("[test]", "onTouchEvent: attack!");
                        attackButtonIsDown = true;
                        player.hit();
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    attackButtonIsDown = false;
                    break;

            }
        }

        return true;
    }

    /**
     * Every time this method is called the canvas is repainted.
     * @param canvas
     */
    @Override
    public  void draw(Canvas canvas){
        super.draw(canvas);
        if(canvas != null) {
            canvas.drawColor(Color.BLACK);
            canvas.drawBitmap(resizedBackGround, 0, 0, null);
            enemy.draw(canvas);
            player.draw(canvas);
        }
    }
}
