package com.example.midexam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Button button2;
    EditText email;
    EditText password;
    Button button;
    DatabaseHelper db;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intialize();
        intializeLogic();
    }

    public  void  intialize(){
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
    }

    public void intializeLogic(){
        db = new DatabaseHelper(this);
        //for sharepreference
        sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);

        button.setOnClickListener(View -> {
            long userId = db.loginUser(email.getText().toString(),password.getText().toString());
            if(userId == -1){
                showMessage("Email or Password is Wrong!");
            }else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("login",String.valueOf(userId));
                editor.commit();

                showMessage(String.valueOf(userId));
                showMessage("login successful!");

                Intent i = new Intent(this,HomeActivity.class);
                startActivity(i);
                finish();
            }
        });

        button2.setOnClickListener(View -> {
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
        });
    }

    public void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}