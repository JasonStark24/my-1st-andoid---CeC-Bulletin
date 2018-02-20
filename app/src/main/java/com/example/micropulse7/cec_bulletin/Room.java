package com.example.micropulse7.cec_bulletin;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Room extends AppCompatActivity {

    String myJSON;

    private static final String TAG_RESULTS="result";
    private static final String TAG_ID = "Room_num";
    private static final String TAG_NAME = "Room_name";
    private static final String TAG_ADD ="Std_id"; //1
    private static final String TAG_SNAME ="Std_name";
    private static final String TAG_INFO ="Room_info";
    JSONArray peoples = null;

    ArrayList<HashMap<String, String>> personList;

    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        list = (ListView) findViewById(R.id.listView);
        personList = new ArrayList<HashMap<String,String>>();
        getData();
        
        final EditText Room = (EditText) findViewById(R.id.Room);
        final EditText RoomDescription = (EditText) findViewById(R.id.RoomDescription);
        final EditText RoomPassword = (EditText) findViewById(R.id.RoomPassword);
        final Button CRoom = (Button) findViewById(R.id.Croom);





        CRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog progressDialog = new ProgressDialog(Room.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Authenticating...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                final String Room_name = Room.getText().toString();
                final String Room_password = RoomPassword.getText().toString();
                final String Room_description = RoomDescription.getText().toString();


                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success){
                                new android.os.Handler().postDelayed(
                                        new Runnable() {
                                            public void run() {

                                                progressDialog.dismiss();
                                            }
                                        }, 3000);





                            }else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Room.this);
                                builder.setMessage("Room created Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();

                                new android.os.Handler().postDelayed(
                                        new Runnable() {
                                            public void run() {

                                                progressDialog.dismiss();
                                            }
                                        }, 3000);


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };


                RoomRequest roomRequest = new RoomRequest(Room_name, Room_password, Room_description, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Room.this);
                queue.add(roomRequest);
            }
        });

        /*------------------------------------------------------------------------------------------------------------------*/


    }

    public void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(Room.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setProgressStyle(ProgressDialog.BUTTON_NEUTRAL);
        progressDialog.show();

        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://jasonkid.esy.es/fetch_room.php");

                // Depends on your web service
                httppost.setHeader("Content-type", "application/json");

                InputStream inputStream = null;
                String result = null;
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    // Oops
                }
                finally {
                    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                myJSON=result;
                showList();
            }

            protected void showList() {


                try {
                    JSONObject jsonObj = new JSONObject(myJSON);
                    peoples = jsonObj.getJSONArray(TAG_RESULTS);

                    for(int i=0;i<peoples.length();i++){

                        JSONObject c = peoples.getJSONObject(i);
                        String Room_num = c.getString(TAG_ID);
                        final String name = c.getString(TAG_NAME);
                        String address = c.getString(TAG_ADD); //2
                        final String Std_name = c.getString(TAG_SNAME);
                        String Room_info = c.getString(TAG_INFO);

                        final HashMap<String,String> persons = new HashMap<String,String>();

                        persons.put(TAG_ID,Room_num);
                        persons.put(TAG_NAME,name);
                        persons.put(TAG_ADD,address); //3
                        persons.put(TAG_SNAME,Std_name);
                        persons.put(TAG_INFO,Room_info);

                        personList.add(persons);


                        /*


final String get_name_try = "";
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                                Toast.makeText(Room.this,get_name_try, Toast.LENGTH_LONG).show();

                            }
                        });*/

                    }




                    progressDialog.dismiss();

                    ListAdapter adapter = new SimpleAdapter(

                            Room.this, personList, R.layout.list_item,
                            new String[]{TAG_ID,TAG_NAME,TAG_ADD,TAG_SNAME,TAG_INFO},
                            new int[]{R.id.Room_num, R.id.Room_name2, R.id.Std_id, R.id.Std_name, R.id.Room_info}//4

                    );
/*-------------------------------------------------------------------------------------------------------------*/

                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                            Toast.makeText(Room.this,"", Toast.LENGTH_LONG).show();

                        }
                    });














                    /*
                    ListAdapter adapter = new SimpleAdapter(

                            Room.this, personList, R.layout.list_item,
                            new String[]{TAG_ID,TAG_NAME,TAG_ADD,TAG_SNAME,TAG_INFO},
                            new int[]{R.id.Room_num, R.id.Room_name2, R.id.Std_id, R.id.Std_name, R.id.Room_info}//4

                    );

                    */



/*
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                            Toast.makeText(Room.this,"", Toast.LENGTH_LONG).show();

                        }
                    });

*/
                    /*
                    final TextView Room_name =(TextView) findViewById(R.id.Room_name);

Room_name.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Toast.makeText(Room.this,"test", Toast.LENGTH_LONG).show();

    }
});*/

                    list.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();


    }
}
