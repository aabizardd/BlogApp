package id.ac.id.telkomuniversity.tass.blogapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import id.ac.id.telkomuniversity.tass.blogapp.R;

public class LoginPhoneActivity extends AppCompatActivity {

    EditText otp_code;
    Button verify_btn;
    String verficationId;
    String phone, OTP;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone);

        String phoneNo = getIntent().getStringExtra("number");
        mAuth = FirebaseAuth.getInstance();
        otp_code = findViewById(R.id.otp_code);
        verify_btn = findViewById(R.id.send_code);

        OTP = getIntent().getStringExtra("auth");

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String verification_code = otp_code.getText().toString();
                if(!verification_code.isEmpty()){
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OTP, verification_code);
                    signIn(credential);
                }else{
                    Toast.makeText(LoginPhoneActivity.this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void signIn(PhoneAuthCredential credential){

        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    sendToMain();
                }
                else{
                    Toast.makeText(LoginPhoneActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currUser = mAuth.getCurrentUser();
        if(currUser != null){
            sendToMain();
        }
    }

    private void sendToMain(){
        startActivity(new Intent(LoginPhoneActivity.this , Home.class));
        finish();
    }
}