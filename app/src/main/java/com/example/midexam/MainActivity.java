package com.example.midexam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText name ;
    EditText email;
    EditText password;

    Button button;
    Button button2;

    DatabaseHelper db;

    TextView textView;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intialize();
        intializeLogic();
    }

    public  void  intialize(){
        name = findViewById(R.id.editTextText);
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        textView = findViewById(R.id.textView);

        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
    }

    public void intializeLogic(){
        db = new DatabaseHelper(this);
        //for sharepreference
        sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(sharedPreferences.contains("login")){
            Intent i = new Intent(this,HomeActivity.class);
            startActivity(i);
            finish();
        }

        textView.setText("Total "+String.valueOf(db.totoalUsers()) + " User");

        button.setOnClickListener(View -> {
            long userId = db.registerUser(name.getText().toString(),email.getText().toString(),password.getText().toString());
            if( userId == -1){
                showMessage("Email is already Exists!");
            }else{
                editor.putString("login",String.valueOf(userId));
                editor.commit();
                showMessage(String.valueOf(userId));

                showMessage("Account created successfully!");
                Intent i = new Intent(this,HomeActivity.class);
                startActivity(i);
                finish();
            }
        });

        button2.setOnClickListener(View -> {
            Intent i = new Intent(this,LoginActivity.class);
            startActivity(i);
            finish();
        });
    }

    public void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}