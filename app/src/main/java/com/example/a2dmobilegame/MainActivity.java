package com.example.a2dmobilegame;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class MainActivity extends Activity implements View.OnClickListener {
    private FirebaseAuth mAuth;

    private Button loggin_btn;
    private Button create_btn;

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        progress = new ProgressDialog(this);

        setContentView(R.layout.activity_main);

        loggin_btn = findViewById(R.id.login_button);
        create_btn = findViewById(R.id.create_button);

        loggin_btn.setOnClickListener(this);
        create_btn.setOnClickListener(this);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setContentView(new GameView(this));
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        Log.d("[current user]", "updateUI: "+currentUser);
        if(currentUser != null){
            //login();
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
/*
            Map<String, Object> highScoreBoard = new HashMap<>();
            highScoreBoard.put(currentUser.getEmail().split("@")[0], 105);

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            progress.show();
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
            progress.dismiss();
*/
            setContentView(new GameView(this, mAuth, currentUser));
            Log.d("[loggin]", "updateUI: alredy loggidin: "+currentUser.getEmail());
        }else {

        }
    }

    private void creatNewAcount(){
        TextView email = findViewById(R.id.email_input);
        TextView password = findViewById(R.id.password_imput);
        //TODO: check for valid email and 6 words password.
        progress.show();

        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("[New Account]", "createUserWithEmail:success");
                            progress.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {

                            // If sign in fails, display a message to the user.
                            progress.dismiss();
                            Log.w("[New Account]", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        // ...
                    }
                });
    }

    private void login(){
        TextView email = findViewById(R.id.email_input);
        TextView password = findViewById(R.id.password_imput);

        progress.show();

        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("[login]", "signInWithEmail:success");
                            progress.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            progress.dismiss();
                            Log.w("[login]", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onClick(View view) {
        Log.d("[button listener]", "onClick: try clicking");
        if(view == loggin_btn){
            Log.d("[button listener]", "onClick: click login btn!");
            login();
        }else if(view == create_btn){
            Log.d("[button listener]", "onClick: click create btn!");

            creatNewAcount();
        }

    }
}