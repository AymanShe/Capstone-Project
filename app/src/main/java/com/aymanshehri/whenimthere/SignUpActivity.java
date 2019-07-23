package com.aymanshehri.whenimthere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.ed_email)
    EditText emailEditText;
    @BindView(R.id.ed_password)
    EditText passwordEditText;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    String signUpText;
    String loginText;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        signUpText = getString(R.string.sign_up_text);
        loginText = getString(R.string.loginText);
        button.setText(signUpText);
        textView.setText(loginText);

        mAuth = FirebaseAuth.getInstance();

        textView.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView:
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.button:
                RegisterUser();
                break;
        }
    }

    private void RegisterUser() {
        if(!Validator.validPassword(passwordEditText) | !Validator.validEmail(emailEditText)){
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            finish();
                            Toast.makeText(SignUpActivity.this, "User Registered Successfully.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(SignUpActivity.this, "This Email is Already Registered.", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(SignUpActivity.this, "Some Error Occurred.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
