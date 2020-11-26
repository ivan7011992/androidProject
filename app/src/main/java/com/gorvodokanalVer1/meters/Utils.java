package com.gorvodokanalVer1.meters;

import android.view.View;
import android.view.ViewManager;

public class Utils {

    public  static void removeElement(View view){
        ((ViewManager) view.getParent()).removeView(view);
    }


}
