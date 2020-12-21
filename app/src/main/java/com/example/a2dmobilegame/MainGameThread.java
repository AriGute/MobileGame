package com.example.a2dmobilegame;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainGameThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private GameView gameView;
    private boolean running;
    public static Canvas canvas;

    public MainGameThread(SurfaceHolder holder, GameView gameView) {
        super();
        this.gameView = gameView;
        this.surfaceHolder = holder;
    }

    public void setRunning(boolean running) {
        this.running = running;
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
