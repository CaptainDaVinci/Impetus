package uvce.com.impetus;

import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class RegisteredActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.TAG;
    private ArrayList<ViewUser> viewUsers;
    private ViewUserAdapter viewUserAdapter;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);

        Log.d(TAG, "Register activity started");
        int eventId = getIntent().getIntExtra("eventId", -1);

        if (eventId == -1) {
            Toast.makeText(getApplicationContext(), "Event not found!", Toast.LENGTH_SHORT).show();
            return ;
        }

        rootRef = FirebaseDatabase.getInstance().getReference();
        viewUsers = new ArrayList<>();
        viewUserAdapter = new ViewUserAdapter(viewUsers);

        Log.d(TAG, "Populating registered users");
        populateUsers(eventId);

        Log.d(TAG, "Populating recycler view");
        populateRecyclerView();
    }

    private void populateRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);

        int spacing = getResources().getDimensionPixelSize(R.dimen.spacing);
        mRecyclerView.addItemDecoration(new RegisteredActivity.SpacesItemDecoration(spacing));
        mRecyclerView.setAdapter(viewUserAdapter);
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

    private void populateUsers(final int eventId) {
        Query eventRef = rootRef.child("Events").orderByChild("id").equalTo(eventId);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DataSnapshot regSnapshot = snapshot.child("registrations").child("usersRegistered");
                    for (DataSnapshot userRegSnapShot : regSnapshot.getChildren()) {
                        Log.d(TAG, userRegSnapShot.toString());
                        Integer userId = userRegSnapShot.child("userId").getValue(Integer.class);
                        addUser(userId, eventId);
                    }
                }

                viewUserAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        eventRef.addListenerForSingleValueEvent(listener);
        eventRef.removeEventListener(listener);
    }

    private void addUser(Integer userId, final Integer eventId) {
        Log.d(TAG, "Adding user: " + userId);

        DatabaseReference userRef = rootRef.child("users");
        Query userQuery = userRef.orderByChild("id").equalTo(userId);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = null;
                String key = null;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    user = snapshot.getValue(User.class);

                    DataSnapshot eventsSnapShot = snapshot.child("eventsRegistered").child("event");
                    for (DataSnapshot eventSnapshot : eventsSnapShot.getChildren()) {
                        if (Objects.equals(eventSnapshot.child("eventId").getValue(Integer.class), eventId)) {
                            key = eventSnapshot.child("confirmKey").getValue(String.class);
                            break;
                        }
                    }
                }

                ViewUser viewUser = new ViewUser(user, key);
                viewUsers.add(viewUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        userQuery.addListenerForSingleValueEvent(listener);
        userQuery.removeEventListener(listener);
    }
}
