package com.ta.emilia.kpsp.util;

import android.os.StrictMode;

/**
 * Created by taufik on 12/04/18.
 */

public class Config {
    //link server
    public static final String HOST = "http://103.111.86.246/app/kpsp-emel/api/";

    public void izinNetworkPolicy(){
        //dapatkan  izin untuk melakukan thread policy (proses Background AsycnTask)
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}