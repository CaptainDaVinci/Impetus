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

public class ViewAllActivity extends AppCompatActivity {
    private final static String TAG = LoginActivity.TAG;
    private ArrayList<ViewUser> userList;
    private ViewUserAdapter userAdapter;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        Log.d(TAG, "Started view all activity");

        rootRef = FirebaseDatabase.getInstance().getReference();

        userList = new ArrayList<>();
        userAdapter = new ViewUserAdapter(userList);

        Log.d(TAG, "Populating user list");
        populateUserList();

        Log.d(TAG, "Populating recycler view");
        populateRecyclerView();
    }

    private void populateRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);

        int spacing = getResources().getDimensionPixelSize(R.dimen.spacing);
        mRecyclerView.addItemDecoration(new ViewAllActivity.SpacesItemDecoration(spacing));
        mRecyclerView.setAdapter(userAdapter);
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

    private void populateUserList() {
        DatabaseReference eventRef = rootRef.child("users");

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (!snapshot.child("events").exists()) {
                        continue;
                    }

                    int eventCount = snapshot.child("events").getValue(Integer.class);
                    if (eventCount != 0) {
                        User user = snapshot.getValue(User.class);
                        ViewUser viewUser = new ViewUser(user, null);
                        userList.add(viewUser);
                    }
                }

                Log.d(TAG, userList.size() + " users found participating");
                if (userList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "No users found, participating!", Toast.LENGTH_SHORT).show();
                }

                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        eventRef.addListenerForSingleValueEvent(listener);
        eventRef.removeEventListener(listener);
    }
}
