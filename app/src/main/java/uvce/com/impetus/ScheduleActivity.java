package uvce.com.impetus;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ScheduleActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.TAG;
    private static ArrayList<Event> scheduleEventList = new ArrayList<>();
    private ArrayList<Event> eventList;
    private User user;
    private int clashCount = 0;
    private ScheduleAdapter scheduleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Log.d(TAG, "Schedule Activity started");

        user = (User) getIntent().getSerializableExtra("User");

        eventList = new ArrayList<>();
        scheduleEventList.clear();
        scheduleAdapter = new ScheduleAdapter(eventList);

        populateEventList();
        populateRecyclerView();
    }

    private void populateEventList() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference eventRef = rootRef.child("Events");

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Integer id = snapshot.child("id").getValue(Integer.class);
                    Integer rounds = snapshot.child("rounds").getValue(Integer.class);
                    String name = snapshot.child("name").getValue(String.class);
                    String venue = snapshot.child("venue").getValue(String.class);
                    String image =  snapshot.child("image").getValue(String.class);
                    String day1 = snapshot.child("Day1").getValue(String.class);
                    String day2 = snapshot.child("Day2").getValue(String.class);

                    Event event = new Event(
                            id, rounds, name, venue,
                            day1, day2,
                            image
                    );

                    event.setRegistered(user.isParticipatingInEvent(id));
                    event.setAdmin(user.isSuperAdmin() || user.isEventAdmin(id));
                    eventList.add(event);
                }


                scheduleAdapter.notifyDataSetChanged();
                Log.d(TAG, "Event list populated with " + eventList.size() + " events");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        eventRef.addListenerForSingleValueEvent(listener);
        eventRef.removeEventListener(listener);
    }

    private void populateRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);

        int spacing = getResources().getDimensionPixelSize(R.dimen.spacing);
        mRecyclerView.addItemDecoration(new ScheduleActivity.SpacesItemDecoration(spacing));
        mRecyclerView.setAdapter(scheduleAdapter);
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

    public static void addEvent(Event event) {
        Log.d(TAG, "Adding event: " + event.getId());

        scheduleEventList.add(event);
    }

    public static void deleteEvent(Event event) {
        Log.d(TAG, "Deleting event: " + event.getId());

        for (int i = 0; i < scheduleEventList.size(); ++i) {
            if (scheduleEventList.get(i).getId() == event.getId()) {
                scheduleEventList.remove(i);
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.schedule) {
            Log.d(TAG, "Events scheduled: " + scheduleEventList.size());
            clashCount = 0;
            detectClashes();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void detectClashes() {
        boolean collides = false;
        for (int i = 0; i < scheduleEventList.size() && !collides; ++i) {
            if (scheduleEventList.get(i).allDay()) {
                continue;
            }

            for (int j = i + 1; j < scheduleEventList.size() && !collides; ++j) {
                if (scheduleEventList.get(j).allDay()) {
                    continue;
                }

                if (scheduleEventList.get(i).collidesWith(scheduleEventList.get(j))) {
                    Log.d(TAG, scheduleEventList.get(i).getName() + " collides with " + scheduleEventList.get(j).getName());

                    ScheduleAdapter.eventCheck[scheduleEventList.get(i).getId()] = false;
                    ScheduleAdapter.eventCheck[scheduleEventList.get(j).getId()] = false;

                    scheduleAdapter.notifyDataSetChanged();

                    scheduleEventList.remove(j);
                    scheduleEventList.remove(i);
                    collides = true;

                    if (clashCount == 0) {
                        Toast.makeText(getApplicationContext(), "Clashes detected resolving . . .", Toast.LENGTH_SHORT).show();
                    }

                    ++clashCount;
                }
            }
        }

        if (collides) {
            detectClashes();
        } else if (clashCount != 0) {
            Toast.makeText(getApplicationContext(),"resolved, please schedule now", Toast.LENGTH_SHORT).show();
        } else if (scheduleEventList.isEmpty()) {
            Toast.makeText(getApplicationContext(), "No events scheduled", Toast.LENGTH_SHORT).show();
        } else {
                Intent intent = new Intent(ScheduleActivity.this, ShowScheduleActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("schedulelist", scheduleEventList);
                intent.putExtra("BUNDLE", bundle);

                startActivity(intent);
        }
    }
}
