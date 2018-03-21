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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        final int eventId = getIntent().getIntExtra("eventId", -1);
        event = (Event) getIntent().getSerializableExtra("event");

        if (eventId == -1 || event == null) {
            Toast.makeText(getApplicationContext(), "Some error occured!", Toast.LENGTH_LONG).show();
            return ;
        }


        Log.d(TAG, "Getting event info for " + eventId);

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

        if (event.isAdmin()) {
            registerButton.setText("Admin");
            registerButton.setBackgroundColor(Color.BLUE);
        } else if (event.isRegistered()) {
            registerButton.setText("Registered");
            registerButton.setBackgroundColor(Color.RED);
        } else {
            registerButton.setText("Register");
            registerButton.setBackgroundColor(Color.parseColor("#0ed218"));
        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventInfoActivity.this, RegisterActivity.class);
                intent.putExtra("eventId", eventId);
                startActivity(intent);
            }
        });

        getEventInfo(eventId);
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
                    String day1 = snapshot.child("Day1").getValue(String.class);
                    String day2 = snapshot.child("Day2").getValue(String.class);
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
