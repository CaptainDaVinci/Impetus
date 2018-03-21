package uvce.com.impetus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.util.regex.*;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "APPATHON";

    private DatabaseReference mDatabase;
    private User user;

    private EditText emailField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d(TAG, "Login started");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                Log.d(TAG, "Login button clicked");
                handleLogin(emailField.getText().toString(), passwordField.getText().toString());
                break;

            case R.id.signupButton:
                Log.d(TAG, "Signup button clicked");
                handleSignup();
                break;
        }
    }

    private void handleSignup() {
        Intent signupIntent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(signupIntent);
        finish();
    }

    private void handleLogin(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Log.d(TAG, "Email or password is empty");
            showError(1);
            return;
        }

        if (!validEmail(email)) {
            Log.d(TAG, "Email is invalid");
            showError(2);
            return;
        }

        if (invalidCredential(email, password)) {
            Log.d(TAG, "Invalid credential");
            showError(3);
            return ;
        }
    }

    private boolean invalidCredential(String email, String password) {
        return false;
    }

    private boolean validEmail(String email) {
        final Pattern VALID_EMAIL =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                        Pattern.CASE_INSENSITIVE);

        Matcher matcher = VALID_EMAIL.matcher(email);
        return matcher.find();
    }

    private void showError(int errorCode) {
        TextView errorField = findViewById(R.id.errorField);
        String msg = "";

        switch (errorCode) {
            case 1:
                msg = "Password or email can't be empty!";
                break;

            case 2:
                msg = "Invalid email";
                break;

            case 3:
                msg = "Invalid credential";
                break;
        }

        errorField.setText(msg);
    }
}
