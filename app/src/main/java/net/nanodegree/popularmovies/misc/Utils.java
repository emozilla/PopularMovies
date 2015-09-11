package net.nanodegree.popularmovies.misc;

import android.content.Context;

import net.nanodegree.popularmovies.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by antonio on 10/09/15.
 */
public class Utils {

    public static String getKeyFromResource(Context context) {

        String strKey = "";

        try {
            InputStream in = context.getResources().openRawResource(R.raw.themoviedbkey);
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();

            while ((strKey = r.readLine()) != null) {
                total.append(strKey);
            }

            strKey = total.toString();
            strKey = strKey.trim();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return strKey;
     }


    public static void log(String message) {

        System.out.println("************************************************************************");
        System.out.println("*                                                                      *");
        System.out.println(message);
        System.out.println("*                                                                      *");
        System.out.println("************************************************************************");
    }
}
