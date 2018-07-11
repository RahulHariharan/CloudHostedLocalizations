package com.demo.dynamiclocalizationsdemo.views;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.demo.dynamiclocalizationsdemo.BaseActivity;
import com.demo.dynamiclocalizationsdemo.R;
import com.demo.dynamiclocalizationsdemo.localization.LocalizationAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends BaseActivity implements LocalizationAPI.LanguagePackReadyListener {

    private FirebaseAuth mAuth;
    private EditText userNameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        userNameEditText = findViewById(R.id.user_name);
        passwordEditText = findViewById(R.id.password);
        LocalizationAPI.sharedInstance().loadDynamicLanguagePack("https://firebasestorage.googleapis.com/v0/b/dynamiclocalizationsdemo.appspot.com/o/document.json?alt=media&token=6321f768-26e6-4523-bf18-5e32bcea0b9a",this);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void signIn(View view) {
        if (TextUtils.isEmpty(userNameEditText.getText().toString())) {
            Toast.makeText(LoginActivity.this, "Enter username.",
                    Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(passwordEditText.getText().toString())) {
            Toast.makeText(LoginActivity.this, "Enter passwword.",
                    Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(userNameEditText.getText().toString(),
                    passwordEditText.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("event_", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                navigateToNextPage();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("event_", "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void navigateToNextPage() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLanguagePackAvailable() {
        Log.v("event_", "onLanguagePackAvailable");
        Log.v("event_", LocalizationAPI.sharedInstance().getString("MOST RECENT TOPIC"));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
