package com.example.a2dmobilegame;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class MainGameThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private GameView gameView;
    private boolean running;
    public static Canvas canvas;

    private static long last_time = System.nanoTime();


    public MainGameThread(SurfaceHolder holder, GameView gameView) {
        super();
        last_time = System.nanoTime();
        this.gameView = gameView;
        this.surfaceHolder = holder;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    static public float getDeltaTime(){
        long time = System.nanoTime();
        int delta_time = (int) ((time - last_time) / 1000000);
        last_time = time;
        //Log.d("[timeDeltaTime]", "getDeltaTime: last time:"+last_time+" , deltatime: "+delta_time);
        return delta_time;
    }

    @Override
    public  void  run(){
        while (running){
            canvas = null;
            try{
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    this.gameView.update();
                    this.gameView.draw(canvas);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            finally {
                if (canvas !=null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
