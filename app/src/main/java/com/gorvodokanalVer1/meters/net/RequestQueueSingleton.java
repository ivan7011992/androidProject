package com.gorvodokanalVer1.meters.net;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.gorvodokanalVer1.meters.model.UserModel;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;

public final class RequestQueueSingleton {
   private static RequestQueue mQueue = null;

   public static RequestQueue getInstance(Context context) {
       if(mQueue == null) {
           PersistentCookieStore persistentCookieStore = new PersistentCookieStore(context);
           final CookieManager manager = new CookieManager(persistentCookieStore,null);

           CookieHandler.setDefault(manager);


           mQueue =  Volley.newRequestQueue(context);
       }
       return mQueue;
   }

}
