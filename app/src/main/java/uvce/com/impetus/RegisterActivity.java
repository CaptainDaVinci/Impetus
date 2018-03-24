package uvce.com.impetus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.TAG;
    private TextView nameField, venueField, timeField, fees, member1;
    private EditText member2, member3, member4;
    private User user;
    private Event event;
    private String confirmKey;
    DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Log.d(TAG, "Register activity started");

        rootRef = FirebaseDatabase.getInstance().getReference();

        SharedPreferences preferences = getSharedPreferences(HomeActivity.USERSESSION, MODE_PRIVATE);
        int userId = preferences.getInt("userId", -1);
        event = (Event) getIntent().getSerializableExtra("event");

        if (userId == -1 || event == null) {
            Toast.makeText(getApplicationContext(), "Some error occured", Toast.LENGTH_LONG).show();
            return ;
        }

        getUserInfo(userId, event.getId());
    }

    public void getUserInfo(int userId, final int eventId) {
        DatabaseReference usersRef = rootRef.child("users");
        Query userRef = usersRef.orderByChild("id").equalTo(userId);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    user = snapshot.getValue(User.class);
                }

                getEventInfo(eventId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        userRef.addListenerForSingleValueEvent(listener);
        userRef.removeEventListener(listener);
    }

    public void getEventInfo(int eventId) {
        nameField = findViewById(R.id.eventNameField);
        venueField = findViewById(R.id.venueField);
        timeField = findViewById(R.id.timeField);
        fees = findViewById(R.id.feeLabel);

        member1 = findViewById(R.id.teamember1);
        member2 = findViewById(R.id.teamember2);
        member3 = findViewById(R.id.teamember3);
        member4 = findViewById(R.id.teamember4);

        nameField.setText(event.getName());
        venueField.setText(event.getVenue());

        if (event.getDay1().equals("allday") || event.getDay2().equals("allday")) {
            timeField.setText("All day");
        } else if (event.getDay1().equals("-1")) {
            timeField.setText(event.getDay2());
        } else if (event.getDay2().equals("-1")) {
            timeField.setText(event.getDay1());
        } else {
            timeField.setText(event.getDay1() + event.getDay2());
        }

        member1.setText(user.getName());

        DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference().child("Events");
        Query eventRef = eventsRef.orderByChild("id").equalTo(eventId);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Integer teamSize = snapshot.child("teamSize").getValue(Integer.class);
                    String totalFee = "Fees: " + " \u20B9 " + 30 * teamSize
                                        + " (30 x "  + teamSize + ")";
                    fees.setText(totalFee);

                    if (teamSize == 2) {
                        member2.setVisibility(View.VISIBLE);
                    } else if (teamSize == 3) {
                        member2.setVisibility(View.VISIBLE);
                        member3.setVisibility(View.VISIBLE);
                    } else if (teamSize == 4) {
                        member2.setVisibility(View.VISIBLE);
                        member3.setVisibility(View.VISIBLE);
                        member4.setVisibility(View.VISIBLE);
                    }
                }

                Button pay = findViewById(R.id.paytmButton);
                final Button cancel = findViewById(R.id.cancelButton);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, "Payment cancelled");
                        finish();
                    }
                });

                pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (member2.getVisibility() == View.GONE) {
                            processPayment();
                            cancel.setVisibility(View.GONE);
                        } else if (member2.getText().toString().isEmpty()) {
                            throwError();
                        } else if (member3.getVisibility() == View.GONE) {
                            processPayment();
                            cancel.setVisibility(View.GONE);
                        } else if (member3.getText().toString().isEmpty()) {
                            throwError();
                        } else if (member4.getVisibility() == View.GONE) {
                            processPayment();
                            cancel.setVisibility(View.GONE);
                        } else if (member4.getText().toString().isEmpty()) {
                            throwError();
                        } else {
                            processPayment();
                            cancel.setVisibility(View.GONE);
                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        eventRef.addListenerForSingleValueEvent(listener);
        eventRef.removeEventListener(listener);
    }

    private void throwError() {
        TextView errorField = findViewById(R.id.errorField);
        errorField.setVisibility(View.VISIBLE);
        errorField.setText("All team member names should be entered");
    }

    private void processPayment() {
        Log.d(TAG, "Processing payment...");

        confirmKey = generateConfirmationKey();
        Log.d(TAG, "Confirmation key: " + confirmKey);

        addUserToEvent();
    }

    private String generateConfirmationKey() {
        String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

        StringBuilder key = new StringBuilder();
        Random rnd = new Random();

        while (key.length() < 8) {
            int index = (int) (rnd.nextFloat() * CHARS.length());
            key.append(CHARS.charAt(index));
        }

        return key.toString();

    }

    private void addUserToEvent() {
        Log.d(TAG, "Adding user to event " + event.getId());
        DatabaseReference eventsRef = rootRef.child("Events");
        final Query eventRef = eventsRef.orderByChild("id").equalTo(event.getId());

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DataSnapshot snap = snapshot.child("registrations").child("count");
                    DatabaseReference ref = snapshot.child("registrations").getRef();

                    int count = Integer.parseInt(snap.getValue().toString());
                    count += 1;

                    ref.child("count").setValue(count);
                    ref.child("usersRegistered").child(String.valueOf(count)).child("userId").setValue(user.getId());
                    ref.child("usersRegistered").child(String.valueOf(count)).child("attended").setValue(false);
                    ref.child("usersRegistered").child(String.valueOf(count)).child("confirmKey").setValue(confirmKey);

                    Log.d(TAG, "USER ADDED: " + user.getId() + " registered for " + event.getId()
                            + " successfully");
                }

                addEventToUser();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        eventRef.addListenerForSingleValueEvent(listener);
        eventRef.removeEventListener(listener);
    }

    private void addEventToUser() {
        Log.d(TAG, "Adding event to user " + user.getId());
        final DatabaseReference usersRef = rootRef.child("users");
        final Query userRef = usersRef.orderByChild("id").equalTo(user.getId());
        int count = 0;

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DataSnapshot snap = snapshot.child("eventsRegistered").child("count");

                    DatabaseReference ref = snapshot.child("eventsRegistered").getRef();
                    DatabaseReference userRootRef = snapshot.getRef();

                    long eve = user.getEvents();
                    eve = (eve | (1 << event.getId()));
                    userRootRef.child("events").setValue(eve);

                    int count = Integer.parseInt(snap.getValue().toString());
                    count += 1;

                    ref.child("count").setValue(count);
                    ref.child("event").child(String.valueOf(count)).child("eventId").setValue(event.getId());
                    ref.child("event").child(String.valueOf(count)).child("confirmKey").setValue(confirmKey);

                    Log.d(TAG, "EVENT ADDED: " + event.getId() + " added to " + user.getId()
                            + " successfully");
                }

                Intent intent = new Intent(RegisterActivity.this, ConfirmKeyActivity.class);
                intent.putExtra("eventId", event.getId());

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        userRef.addListenerForSingleValueEvent(listener);
        userRef.removeEventListener(listener);
    }
}
