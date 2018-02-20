package com.example.micropulse7.cec_bulletin;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jason on 16/03/2017.
 */

public class Update_request  extends StringRequest {

    private static final String UPDATE_REQUEST_URL = "http://jasonkid.esy.es/Promote.php";
    private Map<String, String> params;
    public Update_request ( String Std_id, String promote, Response.Listener<String> listener){

        super(Method.POST,UPDATE_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("Std_id", Std_id);
        params.put("promote", promote);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }


}
