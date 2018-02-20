package com.example.micropulse7.cec_bulletin;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jason on 11/02/2017.
 */

public class ConfirmRegister  extends StringRequest {







    private static final String UPDATE_REQUEST_URL = "http://jasonkid.esy.es/confirm_register.php";
    private Map<String, String> params;
    public ConfirmRegister (String id_number22, String getHEmail1, Response.Listener<String> listener){

        super(Method.POST,UPDATE_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id_number22", id_number22);
        params.put("getHEmail1", getHEmail1);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }


}
