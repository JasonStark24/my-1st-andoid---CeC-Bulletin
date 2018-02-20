package com.example.micropulse7.cec_bulletin;

import android.app.Application;
import com.firebase.client.Firebase;

/**
 * Created by Micropulse7 on 1/16/2017.
 */

/*Firebase Connection*/
public class FirebaseConn extends Application{

    @Override
    public void onCreate() {
    super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
