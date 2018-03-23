package uvce.com.impetus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ConfirmKeyActivity extends AppCompatActivity {
    private final static String TAG = LoginActivity.TAG;
    private User user = null;
    private Button homeButton;
    private String key = "";
    private TextView confirmField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_key);

        Log.d(TAG, "Confirm key activity started");

        confirmField = findViewById(R.id.confirmKeyField);
        homeButton = findViewById(R.id.homeButton);


        SharedPreferences preferences = getSharedPreferences(HomeActivity.USERSESSION, MODE_PRIVATE);
        int userId = preferences.getInt("userId", -1);
        int eventId = getIntent().getIntExtra("eventId", -1);
        if (userId == -1 || eventId == -1) {
            Log.d(TAG, "User id: " + userId + " even Id: " +eventId);
            Toast.makeText(getApplicationContext(), "Some thing went wrong!", Toast.LENGTH_SHORT).show();
            return ;
        }

        getUserData(userId, eventId);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (user != null && !key.equals("-1")) {
            startHomePageActivity(user);
        }
    }

    public void getUserData(int userId, final int eventId) {
        Log.d(TAG, "Fetching user data for " + userId);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");
        Query userQuery = userRef.orderByChild("id").equalTo(userId);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    user = snapshot.getValue(User.class);

                    DataSnapshot eventsSnapShot = snapshot.child("eventsRegistered").child("event");
                    for (DataSnapshot eventSnapshot : eventsSnapShot.getChildren()) {
                        if (eventSnapshot.child("eventId").getValue(Integer.class) == eventId) {
                            key = eventSnapshot.child("confirmKey").getValue(String.class);
                            break;
                        }
                    }
                }

                confirmField.setText(key);
                Log.d(TAG, "Got user: " + user.showInfo() + " and event key for: "
                        + eventId + " " + key);


                homeButton.setVisibility(View.VISIBLE);
                homeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startHomePageActivity(user);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        userQuery.addListenerForSingleValueEvent(listener);
        userQuery.removeEventListener(listener);
    }

    private void startHomePageActivity(User user) {
        if (user == null) {
            Log.d(TAG, "No user found");
            Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
        }

        Intent homePageIntent = new Intent(ConfirmKeyActivity.this, HomeActivity.class);
        homePageIntent.putExtra("User", user);
        homePageIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(homePageIntent);
        finish();
    }
}
