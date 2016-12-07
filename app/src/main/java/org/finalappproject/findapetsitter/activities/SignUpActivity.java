package org.finalappproject.findapetsitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SignUpCallback;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Sign-up activity
 */
public class SignUpActivity extends AppCompatActivity {

    private static final String LOG_TAG = "SignUpActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.etRepeatPassword)
    EditText etRepeatPassword;

    @BindView(R.id.etFullName)
    EditText etFullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Bind views
        ButterKnife.bind(this);

        // Set support toolbar
        toolbar.setTitle(R.string.toolbar_title_sign_up);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_signup, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.miSave:
                signUp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Validates the profile information entered by the user, signs up with the Parse backend
     * and starts up the Home activity in case of success
     */
    private void signUp() {
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();
        final String repeatPassword = etRepeatPassword.getText().toString();
        final String fullName = etFullName.getText().toString();

        if (!validateProfile(email, password, repeatPassword, fullName)) {
            Toast.makeText(SignUpActivity.this, getString(R.string.error_validation), Toast.LENGTH_LONG).show();
            return;
        }

        // NOTE: that we've decided to user the email as the username
        final User user = new User();
        user.setUsername(email);
        user.setPassword(password);
        user.setEmail(email);
        user.setFullName(fullName);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    startHomeActivity();
                } else {
                    Log.e(LOG_TAG, "Failed to signup", e);
                    Toast.makeText(SignUpActivity.this, getString(R.string.error_sign_up), Toast.LENGTH_LONG).show();
                    // TODO  Sign up didn't succeed. Look at the ParseException to figure out what went wrong
                }
            }
        });
    }

    /**
     * Vealidates that the profile attributes are not empty and that the password and repeat password are the same
     *
     * @param email
     * @param password
     * @param repeatPassword
     * @param fullName
     * @return isValidProfile
     */
    private boolean validateProfile(String email, String password, String repeatPassword, String fullName) {
        return (!email.isEmpty()
                && !password.isEmpty()
                && !repeatPassword.isEmpty()
                && password.equals(repeatPassword)
                && !fullName.isEmpty());
    }

    /**
     * Starts the home activity
     */
    private void startHomeActivity() {
        Intent homeIntent = new Intent(this, HomeActivity.class);
        startActivity(homeIntent);
    }
}
