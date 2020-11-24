package com.gorvodokanal.meters.net;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;

public final class RequestQueueSingleton {
   private static RequestQueue mQueue = null;

   public static RequestQueue getInstance(Context context) {
       if(mQueue == null) {
           final CookieManager manager = new CookieManager();
           CookieHandler.setDefault(manager);

           mQueue =  Volley.newRequestQueue(context);
       }
       return mQueue;
   }

}
