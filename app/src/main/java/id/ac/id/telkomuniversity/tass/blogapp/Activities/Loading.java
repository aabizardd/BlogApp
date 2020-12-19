package id.ac.id.telkomuniversity.tass.blogapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import id.ac.id.telkomuniversity.tass.blogapp.R;

public class Loading extends AppCompatActivity {

    Animation topAnim, bottomAnim;
    ImageView imageView;
    TextView textView;

    private static int SPLASH_SCREEN = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loading);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bot_animation);

        imageView = findViewById(R.id.logoLoading);
        textView = findViewById(R.id.titleLoading);

        imageView.setAnimation(topAnim);
        textView.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Loading.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_SCREEN);

    }
}