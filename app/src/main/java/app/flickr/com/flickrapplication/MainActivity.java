package app.flickr.com.flickrapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Arrays;

import app.flickr.com.flickrapplication.adapter.RecycleViewAdapter;
import app.flickr.com.flickrapplication.model.ImageContainer;
import app.flickr.com.flickrapplication.requests.FlickrImageRequests;
import app.flickr.com.flickrapplication.utils.FlickrUtilities;


public class MainActivity extends AppCompatActivity {

    private EditText searchEditText;
    private ImageView searchTextClear;
    private TextView defaultMessageTextView;
    private RecyclerView recyclerView;
    private RecycleViewAdapter adapter;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;

    ImageContainer container;

    private String currentDataQuantity;
    private int currentDataQuantityPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        defaultMessageTextView=(TextView)findViewById(R.id.search_on_flick_text);
        searchEditText=(EditText)findViewById(R.id.search_input);
        searchTextClear=(ImageView)findViewById(R.id.search_input_clear);
        recyclerView=(RecyclerView)findViewById(R.id.drawerList);

        currentDataQuantityPosition=0;
        currentDataQuantity="12";

        searchTextClear.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        requestQueue = FlickrApplication.getMyApplication().getRequestQueue();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().length()>0){
                    searchTextClear.setVisibility(View.VISIBLE);
                }else{
                    searchTextClear.setVisibility(View.GONE);
                }
            }
        });

        searchTextClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEditText.setText("");
            }
        });

        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        fetchData();
                    }
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showSingleChoiceListDialog();
            return true;
        }else if (id == R.id.action_refresh) {
            fetchData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchData() {
        hideKeypad(this, searchEditText);
        if(!FlickrUtilities.isConnected(this)){
            showDialog(getString(R.string.str_no_connection));
            return;
        }
        if(searchEditText.getText().toString().trim().length()>0){
            requestQueue.cancelAll(this);
            progressDialog=ProgressDialog.show(MainActivity.this,getString(R.string.str_please_wait),getString(R.string.str_loading_data),
                    false,false);
            FlickrImageRequests flickrImageRequests = new FlickrImageRequests(getApplicationContext(), searchEditText.getText().toString().trim(),currentDataQuantity,
                    new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            progressDialog.dismiss();
                            container = (ImageContainer) response;
                            if(container.getPhotos().getPhotoList().length>0){
                                adapter = new RecycleViewAdapter(MainActivity.this, Arrays.asList(container.getPhotos().getPhotoList()));
                                defaultMessageTextView.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
                            }else{
                                showDialog(getString(R.string.str_no_data_found));
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    defaultMessageTextView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            });

            flickrImageRequests.setTag(this);
            requestQueue.add(flickrImageRequests);
        }else{
            showDialog(getString(R.string.str_search_something));
        }
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

    private void showSingleChoiceListDialog(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(true);

        alertDialog.setTitle(getString(R.string.str_select_data_quantity));

        final CharSequence sequence[]=new CharSequence[]{"12","18","21"};

        alertDialog.setSingleChoiceItems(sequence,currentDataQuantityPosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentDataQuantityPosition=which;
                if(!currentDataQuantity.equals(sequence[which].toString())){
                    currentDataQuantity=sequence[which].toString();
                    if(searchEditText.getText().toString().trim().length()>0){
                        fetchData();
                    }
                }
                dialog.cancel();
            }
        }).create();

        alertDialog.show();
    }

    private void hideKeypad(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public void onStop(){
        super.onStop();
        requestQueue.cancelAll(this);
    }
}
