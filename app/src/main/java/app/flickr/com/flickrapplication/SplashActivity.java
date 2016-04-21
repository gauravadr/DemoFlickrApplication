package app.flickr.com.flickrapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Guest1 on 4/20/2016.
 */
public class SplashActivity extends Activity {

    Timer timer;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity_layout);

        handler=new Handler();

        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
              handler.post(new Runnable() {
                  @Override
                  public void run() {
                      startActivity(new Intent(SplashActivity.this,MainActivity.class));
                      finish();
                  }
              }) ;
            }
        },1000);
    }
}
