package com.example.myw;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText  email;
    EditText passwordEditText;




    ImageView ivToggle;




    Button logButton;

    TextView register;
    FirebaseAuth mAuth;

    private AlertDialog noInternetDialog;
    private Handler handler = new Handler();
    final boolean[] isPasswordVisible = {false};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        checkInternetLoop();

        mAuth = FirebaseAuth.getInstance();


        passwordEditText = findViewById(R.id.etPassword);
        ivToggle = findViewById(R.id.ivTogglePassword);

        ivToggle.setOnClickListener(v -> {
            if (isPasswordVisible[0]) {
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ivToggle.setImageResource(R.drawable.eye_closed);
            } else {
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ivToggle.setImageResource(R.drawable.eye_opened);
            }

            passwordEditText.setSelection(passwordEditText.length());
            isPasswordVisible[0] = !isPasswordVisible[0];
        });


        register = findViewById(R.id.log_register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(Login.this , Register.class);
                startActivity(i);
                finish();
            }

        });





        email = findViewById(R.id.log_email);


        logButton = findViewById(R.id.log_login);


        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = email.getText().toString().trim();
                String password = passwordEditText.getText().toString();


                 if (mail.isEmpty() || (!mail.endsWith("@gmail.com")) || mail.length()<=10) {


                    Toast.makeText(Login.this, "Please enter your email", Toast.LENGTH_SHORT).show();

                }

                else if (password.isEmpty()) {
                    Toast.makeText(Login.this, "Please enter your password", Toast.LENGTH_SHORT).show();

                }

                else if (password.length() < 8) {
                    Toast.makeText(Login.this, "Password must be atleast 8 characters", Toast.LENGTH_SHORT).show();

                }
                else{


                    mAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent i  = new Intent(Login.this , Home.class);
                                startActivity(i);
                                finish();


                            } else {
                                Toast.makeText(Login.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });












                }


            }

        });

    }



    public boolean isConnectedToInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
    private void checkInternetLoop() {
        if (!isConnectedToInternet(this)) {
            showNoInternetDialog();
            handler.postDelayed(this::checkInternetLoop, 3000); // retry every 3 seconds
        } else {
            if (noInternetDialog != null && noInternetDialog.isShowing()) {
                noInternetDialog.dismiss();
            }

            // Now allow user interaction (e.g., enable UI or show content)
            Toast.makeText(this, "Internet is connected!", Toast.LENGTH_SHORT).show();
        }
    }
    private void showNoInternetDialog() {
        if (noInternetDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Internet")
                    .setMessage("Please connect to the internet to continue.")
                    .setCancelable(false); // block interaction

            noInternetDialog = builder.create();
        }

        if (!noInternetDialog.isShowing()) {
            noInternetDialog.show();
        }
    }





}