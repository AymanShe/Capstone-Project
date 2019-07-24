package com.aymanshehri.whenimthere.services;

import android.util.Patterns;
import android.widget.EditText;

public class Validator {

    public static boolean validEmail(EditText emailEditText){
        String email = emailEditText.getText().toString().trim();
        if (email.isEmpty()) {
            emailEditText.setError("This field ir required");
            emailEditText.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {//todo match firbase rules
            emailEditText.setError("Please enter a valid email");
            emailEditText.requestFocus();
            return false;
        }
        return true;
    }
    public static boolean validPassword(EditText passwordEditText){
        String password = passwordEditText.getText().toString().trim();
        if (password.isEmpty()) {
            passwordEditText.setError("This field ir required");
            passwordEditText.requestFocus();
            return false;
        }
        if (password.length() < 6) {
            passwordEditText.setError("Password has to be more than 6 characters");
            passwordEditText.requestFocus();
            return false;
        }
        return true;
    }

}
