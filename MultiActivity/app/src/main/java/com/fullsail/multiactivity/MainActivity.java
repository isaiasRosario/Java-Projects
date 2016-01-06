package com.fullsail.multiactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

   public final static String EXTRA_MESSAGE = "com.fullsail.multiactivity.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
              Intent intent = new Intent(MainActivity.this, AddScreen.class);
              String message = "TEST";
              intent.putExtra(EXTRA_MESSAGE, message);
              startActivity(intent);

              Snackbar.make(view, "colllllllll", Snackbar.LENGTH_LONG)
                 .setAction("Action", null).show();
           }
        });

        ArrayList<UserData> users = new ArrayList<>();
        users.add(new UserData("Isaias", "Rosario", "31"));
        users.add(new UserData("Angel", "Rosario", "13"));
        users.add(new UserData("Zariah", "Rosario", "5"));

       ListView lv = (ListView)findViewById(R.id.user_list);

       ArrayAdapter<UserData>  adapter = new ArrayAdapter<UserData>(this,
          android.R.layout.simple_list_item_1, users);

       lv.setAdapter(adapter);

    }

}
