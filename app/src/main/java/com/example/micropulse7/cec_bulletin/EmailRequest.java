package com.example.micropulse7.cec_bulletin;



import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jason on 11/02/2017.
 */

public class EmailRequest extends StringRequest {




    private static final String MAIL_REQUEST_URL = "http://jasonkid.esy.es/fetch_email.php";
    private Map<String, String> params;
    public EmailRequest (String sendEmail, Response.Listener<String> listener){

        super(Method.POST,MAIL_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("sendEmail", sendEmail);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }


}
