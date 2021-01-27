package com.example.a2dmobilegame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class GameView extends SurfaceView implements SurfaceHolder.Callback{
    private enum GameState {MainMenu, GamePlay, GameOver};
    private MainGameThread mainThread;

    //FireBase authentication and user details.
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    //Firebase cloude storage.
    private FirebaseFirestore db;

    private boolean isTouch = false;

    //game BackGround img
    private Bitmap backGround;
    private Bitmap resizedBackGround;

    //List of active enemys on the screen.
    public static List<Enemy> enemyList;

    //Player object
    public static Character player;

    private Bitmap playButton;

    //Game score of the current game.
    private int gameScore = 0;
    //Enemy spawn timer.
    private float spawnTime;
    //Enemey spawn rate.
    private final float spawnRate = 35;

    private String search;

    //Tuach for screen inputs.
    boolean attackButtonIsDown = false;
    float startX = 0;
    float startY = 0;
    float currentX = 0;
    float currentY = 0;
    float distance = 0;

    //currnet mobile sceen width and height.
    int screenWidth = 0;
    int screenHeight = 0;

    private GameState gameState;

    //Game high score form the data base(string for the score board).
    public static String highScore="High Score:\n";
    //Player name(from the email).
    private String name = "";

    //Dict that hold name and score for updating the scoreboard in the data base.
    Map<String, Object> highScoreBoard = new HashMap<>();




    /**
     * GameView constructor.
     * @param context current state of the application(Context).
     */
    public GameView(Context context,FirebaseAuth mAuth,FirebaseUser currentUser,String seach) {
        super(context);
        getHolder().addCallback(this);
        this.search = seach;
        this.auth = mAuth;
        this.currentUser = currentUser;
        name = currentUser.getEmail().split("@")[0];
        db = FirebaseFirestore.getInstance();

        mainThread = new MainGameThread(getHolder(), this);
        setFocusable(true);
    }

    private void getScoreBoard(){

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
        gameState = GameState.MainMenu;
        screenWidth=Resources.getSystem().getDisplayMetrics().widthPixels;
        screenHeight=Resources.getSystem().getDisplayMetrics().heightPixels;

        playButton = BitmapFactory.decodeResource(getResources(), R.drawable.play_button);


        getHighScore();
        Log.d("[highscore]", "surfaceCreated: score: " + this.highScore);

        backGround = BitmapFactory.decodeResource(getResources() , R.drawable.forest_back_ground2);
        resizedBackGround = Bitmap.createScaledBitmap(
                backGround, screenWidth, screenHeight, false);

        mainThread.setRunning(true);
        mainThread.start();
    }

    /**
     * Initiate the game mode.
     */
    private void startGame(){
        gameState = GameState.GamePlay;
        player = new Character(getResources(), screenWidth/8, screenHeight/2);
        enemyList = new ArrayList<Enemy>();
        spawnEnemy();
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
        switch(gameState) {
            case MainMenu:
                break;

            case GamePlay:
                player.update();
                if (spawnTime > 0) {
                    spawnTime -= 0.5 / MainGameThread.getDeltaTime();
                } else {
                    spawnTime = spawnRate;
                    spawnEnemy();
                }

                for (Enemy enemy : enemyList) {
                    enemy.Update();
                }
                if (isTouch) {
                    player.move(startX - currentX, startY - currentY);
                }
                break;

            case GameOver:
                break;

        }
    }

    private void spawnEnemy(){
        enemyList.add(new Enemy(getResources(), screenWidth/2, screenHeight/2, player.getPosition()));
    }

    /**
     * Handle all the tuch inputs.
     * @param event current tuch.
     * @return boolean.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();

        switch(gameState) {
            case MainMenu:
                switch (eventaction) {
                    case MotionEvent.ACTION_UP:
                        startX = event.getX();
                        startY = event.getY();

                        //If click on the play button(play button x and y on the screen).
                        if(startX > screenWidth/2-playButton.getWidth()/2 && startX < screenWidth/2+playButton.getWidth()/2){
                            if(startY > screenHeight/3-playButton.getHeight()/2 && startY < screenHeight/3+playButton.getHeight()/2) {
                                //Log.d("[mainMenu]", "onTouchEvent: click play!");
                                startGame();
                            }
                        }
                        break;
                }
                break;

            case GamePlay:
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
                break;

            case GameOver:

                break;

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
        //black top strip(some kind of black background).
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(resizedBackGround, 0, 100, null);

        //Score , player name and timer for the next enemy.
        Paint p = new Paint();
        p.setTextSize(50);
        p.setColor(Color.rgb(255,255,255));
        canvas.drawText("spawn time: "+(int)spawnTime, screenWidth/1.3f, 70, p);
        canvas.drawText("Player: "+name, screenWidth/2.2f, 70, p);
        canvas.drawText("score: "+ gameScore, 50, 70, p);

        //Display on the screen depend on the game state.
        if(canvas != null) {
            switch(gameState) {
                case MainMenu:
                    //Score board background(red)
                    Rect scoreBoardBG = new Rect(150,250,screenWidth/3+50,screenHeight-250);//BackGround color
                    Paint boardColorBG = new Paint(0);
                    boardColorBG.setColor(Color.rgb(255,0,0));
                    canvas.drawRect(scoreBoardBG,boardColorBG);

                    //Score board background(white)
                    Rect scoreBoard = new Rect(200,300,screenWidth/3,screenHeight-300);
                    Paint boardColor = new Paint(0);
                    boardColor.setColor(Color.rgb(255,255,255));
                    canvas.drawRect(scoreBoard,boardColor);

                    //Score board text attributes.
                    Paint higthScoreText = new Paint(0);
                    higthScoreText.setColor(Color.rgb(0,0,0));
                    higthScoreText.setTextSize(50);

                    //Score board text from dataBase.
                    String[] scors = highScore.split(",");
                    for (int i = 0; i < scors.length; i++) {
                        //show only top 10.
                        if(i == 10){
                            break;
                        }
                        canvas.drawText(scors[i], 250, 350 + 55*i, higthScoreText);
                    }

                    //Play button for main menu -> game mode.
                    canvas.drawBitmap(playButton, screenWidth/2-playButton.getWidth()/2, screenHeight/3-playButton.getHeight()/2, null);
                    break;

                case GamePlay:
                    //Draw and check if alive(Enemys).
                    for (int i=0 ; i < enemyList.size() ; i++){
                        Enemy enemy = enemyList.get(i);
                        if(enemy.getAttr().isAlive()) {
                            enemy.draw(canvas);
                        }else {
                            gameScore += 100+(int)spawnTime;
                            enemyList.remove(i);
                        }
                    }

                    //Draw and check if alive(Player).
                    if(player.getAttr().isAlive()) {
                        player.draw(canvas);
                    }else{
                        player = null;
                        enemyList = new ArrayList<Enemy>();
                        spawnTime = 0;

                        gameState  = GameState.GameOver;
                    }
                    break;

                case GameOver:
                    // Write a this score to the database
                    Log.d("[Score]", "draw: score: "+ gameScore);
                    highScoreBoard.put("name", currentUser.getEmail().split("@")[0]);
                    highScoreBoard.put("score", gameScore);
                    gameScore = 0;

                    postNewScore();
                    getHighScore();
                    gameState  = GameState.MainMenu;
                    break;

            }

        }
    }

    /**
     * Add new score to the data base.
      */
    private void postNewScore(){
        // Add a new document with a generated ID
        db.collection("highScoreBoard")
                .add(highScoreBoard)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("[postNewScore]", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("[postNewScore]", "Error adding document", e);
                    }
                });
    }

    /**
     * Retrive the high score collection forom the data base and convert it to string.
     */
    private void getHighScore(){
        Log.d("[Score]", "getHighScore: search: "+search);
        //Ask for the collection "highScoreBoard" and sort the scores from the highest to the lowest.
        db.collection("highScoreBoard").orderBy("score", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    //Once the data hase arrive:
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(search.compareTo("")==0) {
                                highScore = "High Score:\n";
                                //for each doc in the collecion split to names+scores and add to highscore.
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    highScore += "," + document.getData().get("name") + " : " + document.getData().get("score") + "\n";
                                    Log.d("[getHighScore]", document.getId() + " => " + document.getData());
                                }
                            }else{
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String name = document.getData().get("name").toString().replace(" ","");
                                    String s = search.replace(" ", "");
                                    
                                    highScore = "High Score:\n";
                                    Log.d("[score t]", "onComplete: "+name+ ", " + (name.compareTo(s)));
                                    if (name.compareTo(s)==0) {
                                        highScore += "," + document.getData().get("name") + " : " + document.getData().get("score") + "\n";
                                        Log.d("[getHighScore]", document.getId() + " => " + document.getData());
                                    }
                                }
                            }
                        } else {
                            Log.w("[getHighScore]", "Error getting documents.", task.getException());
                        }
                    }
                });

    }
}
