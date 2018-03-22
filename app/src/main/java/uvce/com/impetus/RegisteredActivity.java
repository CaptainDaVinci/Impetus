package uvce.com.impetus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class RegisteredActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);

        Log.d(TAG, "Register activity started");
    }
}
