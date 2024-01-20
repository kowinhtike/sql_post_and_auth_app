package com.example.midexam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {

    TextView textView ;
    Button editBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        intialize();
        intializeLogic();
    }

    public void intialize(){
        textView = findViewById(R.id.postText);
        editBtn = findViewById(R.id.editBtn);
    }

    public void intializeLogic(){
        textView.setText(getIntent().getStringExtra("content"));
        editBtn.setOnClickListener(View -> {
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            databaseHelper.updatePost(Integer.valueOf(getIntent().getStringExtra("id")),textView.getText().toString(),Integer.valueOf(getIntent().getStringExtra("authId")));
            showMessage("Update Post Successful!");
            finish();
        });
    }

    public void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}