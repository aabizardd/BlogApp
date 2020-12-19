package id.ac.id.telkomuniversity.tass.blogapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import id.ac.id.telkomuniversity.tass.blogapp.R;

public class HomeActivity extends AppCompatActivity {

    TextView name, mail;
    Button logout;

    private String username, email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logout = findViewById(R.id.logout_btn);
        name = findViewById(R.id.name);
        mail = findViewById(R.id.mail);


        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount != null){
            name.setText(signInAccount.getDisplayName());
            mail.setText(signInAccount.getEmail());
        }
        else{

            Intent intent = getIntent();
            username = intent.getStringExtra("name");
            email = intent.getStringExtra("email");

            name.setText(username);
            mail.setText(email);

        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getApplicationContext(), LogoutActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}