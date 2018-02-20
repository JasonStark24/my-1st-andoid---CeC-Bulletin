package com.example.micropulse7.cec_bulletin;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jason on 13/03/2017.
 */

public class Log_in_room_request extends StringRequest {





    private static final String LOGIN_REQUEST_URL = "http://jasonkid.esy.es/Login_room.php";
    private Map<String, String> params;

    public Log_in_room_request( String Room_name, String Room_Security, Response.Listener<String> listener){

        super(Request.Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("Room_name", Room_name);
        params.put("Room_Security", Room_Security);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
