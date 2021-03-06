package uvce.com.impetus;

import android.content.Intent;
import android.graphics.Color;
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

public class EventInfoActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.TAG;
    private TextView nameField, descriptionField, venueField, roundsField,
                    teamSizeField, startField, endField,
                    coordinatorNameField, contactField;

    private Event event;
    private boolean fromSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        event = (Event) getIntent().getSerializableExtra("event");
        fromSchedule = getIntent().getBooleanExtra("schedule", false);

        if (event == null) {
            Toast.makeText(getApplicationContext(), "Some error occured!", Toast.LENGTH_LONG).show();
            return ;
        }


        Log.d(TAG, "Getting event info for " + event.getId());

        nameField = findViewById(R.id.nameField);
        descriptionField = findViewById(R.id.descriptionField);
        venueField = findViewById(R.id.venueField);
        roundsField = findViewById(R.id.roundField);
        teamSizeField = findViewById(R.id.teamsSizeField);
        startField = findViewById(R.id.startTimeField);
        endField = findViewById(R.id.endTimeField);
        coordinatorNameField = findViewById(R.id.coordinatorNameField);
        contactField = findViewById(R.id.coordinatorContactField);

        Button registerButton = findViewById(R.id.registerButton);
        if (fromSchedule) {
            registerButton.setVisibility(View.GONE);
        }

        if (event.isAdmin()) {
            registerButton.setText("Admin");
            registerButton.setBackgroundColor(Color.GRAY);
        } else if (event.isRegistered()) {
            registerButton.setText("Key");
            registerButton.setBackgroundColor(Color.BLUE);
        } else {
            registerButton.setText("Register");
            registerButton.setBackgroundColor(Color.parseColor("#0ed218"));
        }


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (event.isAdmin()) {
                    Toast.makeText(getApplicationContext(), "Admins can't register!",
                            Toast.LENGTH_SHORT).show();
                    return ;
                }

                Intent intent;
                if (event.isRegistered()) {
                    intent = new Intent(view.getContext(), ConfirmKeyActivity.class);
                } else {
                    intent = new Intent(EventInfoActivity.this, RegisterActivity.class);
                    intent.putExtra("event", event);
                }

                intent.putExtra("eventId", event.getId());

                startActivity(intent);
                finish();
            }
        });

        getEventInfo(event.getId());
    }

    public void getEventInfo(int eventId) {
        DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference().child("Events");
        Query eventRef = eventsRef.orderByChild("id").equalTo(eventId);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Integer rounds = snapshot.child("rounds").getValue(Integer.class);
                    String name = snapshot.child("name").getValue(String.class);
                    String venue = snapshot.child("venue").getValue(String.class);
                    Integer teamSize = snapshot.child("teamSize").getValue(Integer.class);
                    String description = snapshot.child("description").getValue(String.class);
                    String coordinatorName = snapshot.child("eventCoordinator").getValue(String.class);
                    String contact = snapshot.child("contact").getValue(String.class);

                    nameField.setText(name);
                    descriptionField.setText(description);
                    venueField.setText(venue);
                    roundsField.setText(String.valueOf(rounds));
                    teamSizeField.setText(String.valueOf(teamSize));
                    coordinatorNameField.setText(coordinatorName);
                    contactField.setText(contact);

                    String day1 = "NA";
                    if (!event.getDay1().equals("-1")) {
                        day1 = event.getDay1();
                    }

                    if (event.getDay1().equals("allday")) {
                        day1 = "All day";
                    }

                    String day2 = "NA";
                    if (!event.getDay2().equals("-1")) {
                        day2 = event.getDay2();
                    }

                    if (event.getDay2().equals("allday")) {
                        day2 = "All day";
                    }

                    startField.setText(day1);
                    endField.setText(day2);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        eventRef.addListenerForSingleValueEvent(listener);
        eventRef.removeEventListener(listener);
    }
}
