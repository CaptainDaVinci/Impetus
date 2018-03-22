package uvce.com.impetus;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Log.d(TAG, "Register activity started");
        SharedPreferences preferences = getSharedPreferences(HomeActivity.USERSESSION, MODE_PRIVATE);
        int userId = preferences.getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(getApplicationContext(), "Some error occured", Toast.LENGTH_LONG).show();
            return ;
        }

        getUserInfo(userId);
    }

    private void getUserInfo(int userId) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        Query userRef = usersRef.orderByChild("id").equalTo(userId);

    }
}
