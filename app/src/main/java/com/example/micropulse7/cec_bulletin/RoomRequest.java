package com.example.micropulse7.cec_bulletin;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jason on 19/02/2017.
 */

public class RoomRequest  extends StringRequest {





    private static final String ROOM_REQUEST_URL = "http://jasonkid.esy.es/Room_Name.php";
    private Map<String, String> params;
    public RoomRequest (String Room_name, String Room_password, String Room_description, Response.Listener<String> listener){

        super(Method.POST,ROOM_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("Room_name", Room_name);
        params.put("Room_password", Room_password);
        params.put("Room_description", Room_description);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
