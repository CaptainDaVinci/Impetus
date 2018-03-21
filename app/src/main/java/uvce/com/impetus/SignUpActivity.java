package uvce.com.impetus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private final static String TAG = LoginActivity.TAG;

    EditText nameField, collegeField, branchField, yearField,
            emailField, passwordField;

    TextView errorField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Log.d(TAG, "Signup started!");

        nameField = findViewById(R.id.nameField);
        collegeField = findViewById(R.id.collegeField);
        branchField = findViewById(R.id.branchField);
        yearField = findViewById(R.id.yearField);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        errorField = findViewById(R.id.errorField);

        Button signUp = findViewById(R.id.signupButton);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorField.setVisibility(View.GONE);
                validateInput();
            }
        });
    }

    private void validateInput() {
        Log.d(TAG, "Validating input");

        String name = nameField.getText().toString();
        String college = collegeField.getText().toString();
        String branch = branchField.getText().toString();
        String year = yearField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        if (name.isEmpty() || college.isEmpty() || branch.isEmpty() ||
                year.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showError(1);
            return ;
        }

        if (!validEmail(email)) {
            showError(2);
            return ;
        }

        int yearInt = Integer.parseInt(year);
        if (yearInt < 1 || yearInt > 5) {
            showError(3);
            return ;
        }

        if (password.length() < 8) {
            showError(4);
            return ;
        }

        Log.d(TAG, "Valid input");

        User user = new User(name, email, college, branch, password, yearInt);
        createNewUser(user, password);
    }

    private void createNewUser(User user, String password) {
        Log.d(TAG, "Creating new user: " + user.getName() + " " +
                user.getCollege() + " "
                + user.getBranch() + " "
                + user.getYear() + " "
                + user.getEmail());
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
                msg = "All fields should be filled";
                break;

            case 2:
                msg = "Invalid email";
                break;

            case 3:
                msg = "Year should be between 1 - 5";
                break;

            case 4:
                msg = "Password should be atleast 8 characters long";
                break;
        }

        errorField.setVisibility(View.VISIBLE);
        errorField.setText(msg);
    }
}
