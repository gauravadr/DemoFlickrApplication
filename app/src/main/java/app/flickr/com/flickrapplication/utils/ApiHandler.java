package app.flickr.com.flickrapplication.utils;

import android.content.Context;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import app.flickr.com.flickrapplication.R;

/**
 * Created by Guest1 on 4/20/2016.
 */
public class ApiHandler {

    public static String getApiUrl(Context context,String tag,String numberOfData){
        try{
            tag=URLEncoder.encode(tag,"utf-8");
            return String.format(context.getResources().getString(R.string.app_url),tag,numberOfData);
        }catch(UnsupportedEncodingException e){
            Log.e("UnsupportedException", e.getLocalizedMessage());
            return String.format(context.getResources().getString(R.string.app_url),"",numberOfData);
        }
    }
}
