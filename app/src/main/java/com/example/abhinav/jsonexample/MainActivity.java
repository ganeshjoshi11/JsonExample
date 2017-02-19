package com.example.abhinav.jsonexample;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    /**
     * Async task to get json by making HTTP call
     */
    public class GetContacts extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please Wait....");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            //Making a request to url and getting response
            String jsonStr =sh.makeServiceCall(url);
            Log.e(TAG,"Response from url: "+ jsonStr);

            if (jsonStr != null){
                try{
                    JSONArray jsonAry = new JSONArray(jsonStr);
                    //Getting JSON Array node
//                   JSONObject jsonObj = jsonAry.getJSONObject(jsonStr);

                    //looping through All Contacts
                    for (int i = 0; i<jsonAry.length(); i++){
                        JSONObject c = jsonAry.getJSONObject(i);

                        String userId = c.getString("userId");
                        String id = c.getString("id");
                        String title = c.getString("title");
                        String body = c.getString("body");

                        //tmp hash map for single contacrt
                        HashMap<String, String > contact = new HashMap<>();

                        //adding each child node to HashMap key => value
                        contact.put("userId",userId);
                        contact.put("id",id);
                        contact.put("title",title);
                        contact.put("body",body);

                        //adding contact to contact list
                        contactList.add(contact);

                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: "+ e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Json parsing error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Couldn't get json from server. check logcat", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }
        @Override
        protected void onPostExecute (Void result){
            super.onPostExecute(result);

            //Dismiss the progress Dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into LIStView
             */
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, contactList,
                    R.layout.list_item, new String[]{"userId","id", "title", "body"}, new int[]{
                    R.id.userId, R.id.id, R.id.title, R.id.body});
            lv.setAdapter(adapter);

        }
    }
}