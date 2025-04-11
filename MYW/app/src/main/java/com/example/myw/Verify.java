package com.example.myw;

import static com.google.firebase.auth.PhoneAuthProvider.*;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;

import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;


public class Verify extends AppCompatActivity {

    String verificationCodeBySystem;
    EditText otp;
    Button verify_btn;
    ProgressBar progressBar;


    String name;
    String email;
    String password ;
    String mobile_no ;
    String country ;
    String state;
    String district ;
    boolean what;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        otp = (EditText) findViewById(R.id.otp);
        verify_btn = (Button) findViewById(R.id.verify);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);


        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            name =  bundle.getString("name");
            email =  bundle.getString("email" );
            password =  bundle.getString("password");
            mobile_no =  bundle.getString("mobile_no");
            country =  bundle.getString("country");
            state =  bundle.getString("state");
            district =  bundle.getString("district");
            what  = bundle.getBoolean("what");


          sendVerificationCodeToUser(mobile_no);





        }
        else{
            Toast.makeText(this, "Process Failed", Toast.LENGTH_SHORT).show();
            Intent i  = new Intent(Verify.this , Login.class);

            startActivity(i);
            finish();

        }



        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = otp.getText().toString();

                if(code.isEmpty() || code.length() < 6){
                    otp.setError("Wrong OTP...");
                    otp.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        });
    }

    private void sendVerificationCodeToUser(String phoneNo) {

        getInstance().verifyPhoneNumber(
                "+91" + phoneNo,        // Phone number to verify
                60L,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                Verify.this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }

    private OnVerificationStateChangedCallbacks mCallbacks = new OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationCodeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(Verify.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    };

    private void verifyCode(String codeByUser) {
        PhoneAuthCredential credential = getCredential(verificationCodeBySystem, codeByUser);
        signInTheUserByCredentials(credential);
    }

    private void signInTheUserByCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(Verify.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(getApplicationContext(),Home.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);


                            if(what){

                                HashMap<String, Object> userDataMap =  new HashMap<>();
                                userDataMap.put("name", name);
                                userDataMap.put("email", email);
                                userDataMap.put("password", password);
                                userDataMap.put("mobile_no", mobile_no);
                                userDataMap.put("country", country);
                                userDataMap.put("state", state);
                                userDataMap.put("district", district);
//instantiate database connection
                                FirebaseDatabase database =  FirebaseDatabase.getInstance();
                                DatabaseReference quotesRef =  database.getReference( "Users");
                                String key =  quotesRef.push().getKey();
                                userDataMap.put("key", key);

                                Toast.makeText(Verify.this, "Data Processing", Toast.LENGTH_SHORT).show();


                                assert key != null;
                                quotesRef.child(key).setValue(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isComplete()){
                                            Toast.makeText(Verify.this, "Data Added", Toast.LENGTH_SHORT).show();
                                        }
                                        else{

                                            Toast.makeText(Verify.this, "Data Failed", Toast.LENGTH_SHORT).show();
                                        }




                                    }
                                });


                            }







                            startActivity(intent);
                        }else {
                            Toast.makeText(Verify.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}




