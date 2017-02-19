package com.example.abhinav.jsonexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String TAG =MainActivity.class.getSimpleName();

    private ListView lv;

    //URL to get contacts JSON
    private static String url = "https://jsonplaceholder.typicode.com/posts/";

    ArrayList<HashMap<String ,String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        new GetContacts().execute();
    }
}
