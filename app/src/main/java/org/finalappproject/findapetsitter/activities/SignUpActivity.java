package org.finalappproject.findapetsitter.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.parse.interceptors.ParseLogInterceptor;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.application.AppConstants;
import org.finalappproject.findapetsitter.model.UserProfile;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.password;

public class SignUpActivity extends AppCompatActivity {

    private static final String LOG_TAG = "SignUpActivity";

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.etRepeatPassword)
    EditText etRepeatPassword;

    @BindView(R.id.etFullName)
    EditText etFullName;

    @BindView(R.id.btSave)
    Button btSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // TODO setup toolbar or remove from layout/theme

        // Initialize views
        ButterKnife.bind(this);
        // Setup save button
        setupSaveButton();
    }

    private void setupSaveButton() {
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();
                final String repeatPassword = etRepeatPassword.getText().toString();
                final String fullName = etFullName.getText().toString();

                if (!isValidProfile(email, password, repeatPassword, fullName)) {
                    Toast.makeText(SignUpActivity.this, getString(R.string.error_validation), Toast.LENGTH_LONG).show();
                    return;
                }

                // Note that we've decided to user the email as the username
                final ParseUser user = new ParseUser();
                user.setUsername(email);
                user.setPassword(password);
                user.setEmail(email);

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            UserProfile userProfile = createUserProfile(user, fullName);

                            // Launch home activity
                            Intent homeIntent = new Intent(SignUpActivity.this, HomeActivity.class);

                            homeIntent.putExtra(AppConstants.EXTRA_USER_PROFILE, Parcels.wrap(userProfile));

                            startActivity(homeIntent);
                        } else {
                            Log.e(LOG_TAG, "Failed to signup", e);
                            Toast.makeText(SignUpActivity.this, getString(R.string.error_sign_up), Toast.LENGTH_LONG).show();
                            // TODO  Sign up didn't succeed. Look at the ParseException to figure out what went wrong
                        }
                    }
                });

                // TODO how to handle other profile information ???

            }
        });
    }

    private boolean isValidProfile(String email, String password, String repeatPassword, String fullName) {
        return (!email.isEmpty()
                && !password.isEmpty()
                && !repeatPassword.isEmpty()
                && password.equals(repeatPassword)
                && !fullName.isEmpty());
    }

    private UserProfile createUserProfile(ParseUser user, String fullName) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(user.getObjectId());
        userProfile.setFullName(fullName);

        userProfile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {

                } else {
                    Log.e(LOG_TAG, "Failed to save user profile", e);
                    Toast.makeText(SignUpActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return userProfile;
    }
}
