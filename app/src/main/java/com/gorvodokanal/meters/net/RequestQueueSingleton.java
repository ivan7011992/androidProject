package com.gorvodokanal.meters.net;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public final class RequestQueueSingleton {
   private static RequestQueue mQueue = null;

   public static RequestQueue getInstance(Context context) {
       if(mQueue == null) {
           mQueue =  Volley.newRequestQueue(context);
       }
       return mQueue;
   }

}
