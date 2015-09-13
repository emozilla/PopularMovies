package net.nanodegree.popularmovies.misc;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import net.nanodegree.popularmovies.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.util.TypedValue.*;

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

    // from http://stackoverflow.com/questions/13974289/how-to-set-height-of-gridview-programmatically-android
    public static int convertDpToPixels(float dp, Context context){
        Resources resources = context.getResources();
        return (int) applyDimension(
                COMPLEX_UNIT_DIP,
                dp,
                resources.getDisplayMetrics()
        );
    }
}
