package app.flickr.com.flickrapplication.requests;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Request.Priority;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import app.flickr.com.flickrapplication.R;
import app.flickr.com.flickrapplication.model.ImageContainer;
import app.flickr.com.flickrapplication.utils.ApiHandler;


/**
 * Created by Guest1 on 4/20/2016.
 */
public class FlickrImageRequests<T> extends Request<T> {

    private Priority priority = Priority.HIGH;
    private Listener<T> listener;
    private final Gson mGson;

    public FlickrImageRequests(Context context, String tag, String dataQuantity,Listener<T> listener, ErrorListener errorListener) {
        super(Request.Method.GET, ApiHandler.getApiUrl(context,tag,dataQuantity), errorListener);
        this.listener = listener;
        this.mGson = new Gson();
        setRetryPolicy(new DefaultRetryPolicy(context.getResources().getInteger(R.integer.api_timeout), 2, 1));
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return super.getHeaders();
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Request.Priority priority) {
        this.priority = priority;
    }

    @Override
    protected void deliverResponse(T arg0) {
        final T result = (T) arg0;
        if (listener != null)
            listener.onResponse((T) result);
    }


    @SuppressWarnings("unchecked")
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String data = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            String json=data.replace("jsonFlickrApi(", "").replace(")", "");
            Log.e("Json","Data "+json);
            return (Response<T>) Response.success(mGson.fromJson(json, ImageContainer.class),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

}
