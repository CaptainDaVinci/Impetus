package uvce.com.impetus;

import android.content.SharedPreferences;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyAccountActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.TAG;
    private TextView nameField, collegeField, yearField,
                        branchField, emailField;
    private EventAdapterSmall adapterSmall;
    private ArrayList<String> participating;

    private String[] eventList = {
            "Steal Jobs", "IT WIZ 2.0", "Tech Charades", "Vaaksamara",
            "Tech Mark", "Automania", "Electrophilia", "Spektrom",
            "So you think it's over?", "Appathon", "Sumo Wars",
            "Gaming", "Cyborg", "Neutral Oxide", "Summit",
            "Code Storm", "Enigma", "Marvel Mania", "Robo Soccer",
            "Mech or Break", "Laser Tag"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        SharedPreferences preferences = getSharedPreferences(HomeActivity.USERSESSION, MODE_PRIVATE);
        int userId = preferences.getInt("userId", -1);
        int intentUserId = getIntent().getIntExtra("userId", -1);

        if (userId == -1) {
            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            return ;
        }

        if (intentUserId != -1) {
            Log.d(TAG, "Got user from an intent!");
            userId = intentUserId;
        } else {
            Log.d(TAG, "Got user from shared preference");
        }

        Log.d(TAG, "My account for " + userId);


        nameField = findViewById(R.id.nameField);
        collegeField = findViewById(R.id.college_field);
        yearField = findViewById(R.id.year_field);
        branchField = findViewById(R.id.branch_field);
        emailField = findViewById(R.id.email_field);

        participating = new ArrayList<>();
        adapterSmall = new EventAdapterSmall(participating);

        populateRecyclerView();
        getUserInfo(userId);
    }

    private void populateRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);

        int spacing = getResources().getDimensionPixelSize(R.dimen.spacing);
        mRecyclerView.addItemDecoration(new MyAccountActivity.SpacesItemDecoration(spacing));
        mRecyclerView.setAdapter(adapterSmall);
    }

    private class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        SpacesItemDecoration(int spacing) {
            this.space = spacing;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.bottom = space;

            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.top = space;
            } else {
                outRect.top = 0;
            }
        }
    }

    public void getUserInfo(int userId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");
        Query userQuery = userRef.orderByChild("id").equalTo(userId);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = null;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    user = snapshot.getValue(User.class);
                }

                if (user != null) {
                    for (int i = 0; i < eventList.length; ++i) {
                        if (user.isParticipatingInEvent(i)) {
                            participating.add(eventList[i]);
                        }
                    }

                    Log.d(TAG, "user participating in " + participating.size() + " events");
                    setInfo(user);
                    adapterSmall.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        userQuery.addListenerForSingleValueEvent(listener);
        userQuery.removeEventListener(listener);
    }

    public void setInfo(User user) {
        nameField.setText(user.getName());
        collegeField.setText(user.getCollege());
        branchField.setText(user.getBranch());
        yearField.setText(user.getYear());
        emailField.setText(user.getEmail());
    }
}
