package uvce.com.impetus;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "APPATHON";

    private EditText emailField;
    private EditText passwordField;
    private TextView errorField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d(TAG, "Login started");

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        errorField = findViewById(R.id.errorField);
        Button button = findViewById(R.id.signupButton);
        button.setPaintFlags(button.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    public void onClick(View view) {
        errorField.setVisibility(View.GONE);

        switch (view.getId()) {
            case R.id.loginButton:
                handleLogin(emailField.getText().toString(), passwordField.getText().toString());
                break;

            case R.id.signupButton:
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
        Log.d(TAG, "validating input data");

        if (email.isEmpty() || password.isEmpty()) {
            showError(1);
            return;
        }

        if (!validEmail(email)) {
            Log.d(TAG, "Email is invalid");
            showError(2);
            return;
        }

        Log.d(TAG, "Looking for " + email + " with " + password);
        validateCredentials(email, password);
    }

    private void validateCredentials(final String email, final String password) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");
        Query userQuery = userRef.orderByChild("email").equalTo(email);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean found = false;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    if (Objects.equals(user.getEmail(), email) &&
                            Objects.equals(user.getPassword(), password)) {
                        startHomePageActivity(user);
                        found = true;
                    } else {
                        Log.d(TAG, "Invalid credential " + password + " - " + user.getPassword());
                        showError(3);
                    }
                }

                if (!found) {
                    Log.d(TAG, "Not found");
                    showError(4);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        userQuery.addListenerForSingleValueEvent(listener);
        userQuery.removeEventListener(listener);
    }

    private void startHomePageActivity(User user) {
        Log.d(TAG, "Successful login " + user.showInfo());

        Intent homePageIntent = new Intent(LoginActivity.this, HomeActivity.class);
        homePageIntent.putExtra("User", user);
        startActivity(homePageIntent);
        finish();
    }

    private boolean validEmail(String email) {
        final Pattern VALID_EMAIL =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                        Pattern.CASE_INSENSITIVE);

        Matcher matcher = VALID_EMAIL.matcher(email);
        return matcher.find();
    }

    private void showError(int errorCode) {
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

            case 4:
                msg = "Email not present in database. sign up?";
                break;
        }

        errorField.setVisibility(View.VISIBLE);
        errorField.setText(msg);
    }
}
