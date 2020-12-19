package id.ac.id.telkomuniversity.tass.blogapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import id.ac.id.telkomuniversity.tass.blogapp.R;

public class InsertNumberActivity extends AppCompatActivity {

    EditText phoneNumber;
    String phone;
    Button btn_send_code;

    private FirebaseAuth auth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_number);

        backToLogin();

        auth = FirebaseAuth.getInstance();

        phoneNumber = findViewById(R.id.otp_code);
        btn_send_code = findViewById(R.id.send_code);


        btn_send_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phone = phoneNumber.getText().toString();
                String phone_number = "+62"+""+phone;

                if(!phone.isEmpty()){
                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber(phone_number)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(InsertNumberActivity.this)
                            .setCallbacks(mCallBacks)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                }else{

                    Toast.makeText(InsertNumberActivity.this, "Please Phone Number", Toast.LENGTH_SHORT).show();

                }

            }
        });


        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                signIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                Toast.makeText(InsertNumberActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(@NonNull final String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                Toast.makeText(InsertNumberActivity.this, "OTP has been sent", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent otpIntent = new Intent(InsertNumberActivity.this, LoginPhoneActivity.class);
                        otpIntent.putExtra("auth", s);
                        startActivity(otpIntent);


                    }
                }, 10000);




            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = auth.getCurrentUser();
        if(user!=null){
            sendToMain();
        }
    }

    private void sendToMain() {

        Intent mainIntent = new Intent(InsertNumberActivity.this , Home.class);
        startActivity(mainIntent);
        finish();
    }

    private void signIn(PhoneAuthCredential credential){
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    sendToMain();
                }else{
                    Toast.makeText(InsertNumberActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void backToLogin(){

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(InsertNumberActivity.this, LoginActivity.class);
                startActivity(i);

            }
        });

    }
}