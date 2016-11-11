package org.finalappproject.findapetsitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.finalappproject.findapetsitter.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Login activity
 */
public class LoginActivity extends AppCompatActivity {

    private static final String LOG_TAG = "LoginActivity";

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.btLogin)
    Button btLogin;

    @BindView(R.id.tvSignUp)
    TextView ivSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        ButterKnife.bind(this);

        if (ParseUser.getCurrentUser() != null) {
            startHomeActivity();
            // Causes the activity to close if the user returns to it
            finish();
        } else {
            setupLoginButton();
            setupSignUpButton();
        }
    }

    private void setupLoginButton() {
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();
            }
        });
    }

    private void setupSignUpButton() {
        ivSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignUpActivity();
            }
        });
    }

    /**
     * Logs user into the application using parse
     */
    private void logIn() {
        // Retrieve information from views
        final String userName = etEmail.getText().toString();
        final String password = etPassword.getText().toString();

        // Execute parse authentication
        ParseUser.logInInBackground(userName, password, new LogInCallback() {

            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    startHomeActivity();
                } else {
                    Log.e(LOG_TAG, "Authentication failure", e);
                    Toast.makeText(LoginActivity.this, getString(R.string.error_authentication), Toast.LENGTH_LONG).show();
                    // TODO Improve connection error handle connectivity problems
                    // TODO Signup failed. Look at the ParseException to see what happened.
                }
            }
        });
    }

    /**
     * Starts home activity
     */
    private void startHomeActivity() {
        Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(homeIntent);
    }

    /**
     * Starts sign-up activity
     */
    private void startSignUpActivity() {
        Intent signUpActivity = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(signUpActivity);
    }
}
