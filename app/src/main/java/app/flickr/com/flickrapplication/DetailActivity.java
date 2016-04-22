package app.flickr.com.flickrapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import org.w3c.dom.Text;

import app.flickr.com.flickrapplication.utils.FlickrUtilities;

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

        if(!FlickrUtilities.isConnected(this)){
            progressBar.setVisibility(View.GONE);
            showDialog(getString(R.string.str_no_connection));
            return;
        }else{
            requestQueue.add(imageRequest);
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        requestQueue.cancelAll(this);
    }

    private void showDialog(String message){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(true);

        alertDialog.setTitle(getString(R.string.app_name));

        if(message!=null&&message.trim().length()>0)
            alertDialog.setMessage(message);

        alertDialog.setNegativeButton(getString(R.string.str_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.show();
    }
}


















