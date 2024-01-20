package com.example.midexam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    Button logout;
    Button post;

    EditText postText;

    SharedPreferences sharedPreferences;

    DatabaseHelper db;

    ListView listView;

    CustomAdapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        intialize();
        intializeLogic();
    }

    public void intialize(){
        logout = findViewById(R.id.logout);
        post = findViewById(R.id.editBtn);
        postText = findViewById(R.id.postText);
        listView = findViewById(R.id.listView);
    }

    public void intializeLogic(){
        sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //for database
        db = new DatabaseHelper(this);

        showMessage(String.valueOf(db.totoalPosts()));

        post.setOnClickListener(View -> {
            db.addPost(postText.getText().toString(),Long.valueOf(sharedPreferences.getString("login","")));
            setListView();
            postText.setText("");
        });

        logout.setOnClickListener(View -> {
            editor.remove("login");
            editor.apply();
            showMessage("Logout Successfully!");
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
        });
    }

    public void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setListView();
    }

    public void setListView(){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        List<PostModel> itemList = databaseHelper.getAllPost(Long.valueOf(sharedPreferences.getString("login","")));
        // Add more items as needed
        adapter = new CustomAdapter(this, itemList);
        listView.setAdapter(adapter);
    }

    public class CustomAdapter extends BaseAdapter {
        private Context context;
        private List<PostModel> itemList;
        public CustomAdapter(Context context, List<PostModel> itemList) {
            this.context = context;
            this.itemList = itemList;
        }

        @Override
        public CharSequence[] getAutofillOptions() {
            return super.getAutofillOptions();
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int position) {
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.custom_list_item, parent, false);
            }

            final PostModel currentItem = (PostModel) getItem(position);

            TextView textViewContent = convertView.findViewById(R.id.content);
            TextView textViewAuthId = convertView.findViewById(R.id.authId);
            Button deletBtn = convertView.findViewById(R.id.delete);
            Button updateBtn = convertView.findViewById(R.id.edit);
            TextView id = convertView.findViewById(R.id.id);

            textViewContent.setText(currentItem.getContent());
            textViewAuthId.setText(String.valueOf(currentItem.getAuthId()));
            id.setText(String.valueOf(position+1));

            convertView.setOnClickListener( View -> {
                        // Show ID in a Toast message
                        Toast.makeText(context, "Clicked item ID: " + position, Toast.LENGTH_SHORT).show();
                    }
            );

            updateBtn.setOnClickListener(View -> {
                Intent i = new Intent(context,UpdateActivity.class);
                i.putExtra("id",String.valueOf(currentItem.getId()));
                i.putExtra("content",currentItem.getContent());
                i.putExtra("authId",String.valueOf(currentItem.getAuthId()));
                startActivity(i);
            });

            deletBtn.setOnClickListener(View ->{
                DatabaseHelper dbHelper = new DatabaseHelper(context);
                dbHelper.deletePost(currentItem.getId());
                setListView();
            });

            return convertView;
        }

    }
}