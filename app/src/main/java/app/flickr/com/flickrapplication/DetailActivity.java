package app.flickr.com.flickrapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import org.w3c.dom.Text;

/**
 * Created by Guest1 on 4/21/2016.
 */
public class DetailActivity extends Activity {

    private TextView itemNameText;
    private ImageView itemImageView;
    private ProgressBar progressBar;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity_layout);

        itemNameText=(TextView)findViewById(R.id.item_detail_text);
        progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        itemImageView=(ImageView)findViewById(R.id.item_detail_image);

        String itemName=getIntent().getStringExtra(getString(R.string.str_item_name_extra));
        String itemUrl=getIntent().getStringExtra(getString(R.string.str_item_image_url_extra));

        itemNameText.setText(itemName);
        ImageRequest imageRequest = new ImageRequest(itemUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        itemImageView.setImageBitmap(bitmap);
                        progressBar.setVisibility(View.GONE);
                    }
                }, 0, 0, ImageView.ScaleType.FIT_XY,null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                    }
                });

        imageRequest.setTag(this);

        requestQueue=FlickrApplication.getMyApplication().getRequestQueue();
        requestQueue.add(imageRequest);
    }

    @Override
    public void onStop(){
        super.onStop();
        requestQueue.cancelAll(this);
    }
}


















