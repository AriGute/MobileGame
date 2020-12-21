package com.example.a2dmobilegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback{
    private MainGameThread mainThread;
    //TODO: movment thread and buttons tread(for tuch inputs)
    // or get multiply tuchs with id for each and stuff.

    //private MovmentTread movmentTread;
    //private ButtonThread buttonThread;

    private boolean isTouch = false;

    Character player;
    boolean tuchDown = false;

    float startX = 0;
    float startY = 0;
    float currentX = 0;
    float currentY = 0;
    float distance = 0;

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
        player = new Character(getResources(), 200, 300);

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
        if(isTouch){
            player.move(startX-currentX, startY-currentY);
        }
    }

    /**
     * Every time this method is called the canvas is repainted.
     * @param canvas
     */
    @Override
    public  void draw(Canvas canvas){
        super.draw(canvas);
        if(canvas != null) {
            canvas.drawColor(Color.WHITE);
            //canvas.drawBitmap(joystick, 100, 100, null);
            player.draw(canvas);
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


        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:
                //Log.d("[Tuch]", "onTouchEvent: "+ "ACTION_DOWN AT COORDS "+"X: "+X+", Y: "+Y);
                startX = event.getX();
                startY = event.getY();
                currentX = event.getX();
                currentY = event.getY();
                distance = 1;
                isTouch = true;
                break;

            case MotionEvent.ACTION_MOVE:
                //Log.d("[Tuch]", "onTouchEvent: "+ "ACTION_DOWN AT COORDS "+"X: "+X+", Y: "+Y+", distance "+distance);
                distance = (float) Math.sqrt(Math.pow(startX-startX,2)+Math.pow(startY-startY,2));
                currentX = event.getX();
                currentY = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                //Log.d("[Tuch]", "onTouchEvent: "+ "ACTION_DOWN AT COORDS "+"X: "+X+", Y: "+Y);
                startX = 0;
                startY = 0;
                isTouch = false;
                distance = 1;
                break;
        }

        return true;
    }
}
