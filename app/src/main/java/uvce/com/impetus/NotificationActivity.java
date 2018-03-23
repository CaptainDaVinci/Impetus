package uvce.com.impetus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationActivity extends AppCompatActivity {
    private EditText bodyField;
    private Spinner topicField;
    private Button notifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        bodyField = findViewById(R.id.bodyField);
        topicField = findViewById(R.id.topicSpinner);
        notifyButton = findViewById(R.id.notifyButton);

        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Sending. . .", Toast.LENGTH_LONG).show();
                publish(bodyField.getText().toString(), topicField.getSelectedItem().toString());
            }
        });
    }

    private void publish(final String body, final String topic) {
        if (body.isEmpty() || topic.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Fields can't be empty", Toast.LENGTH_SHORT).show();
            return ;
        }

        final DatabaseReference newsRef = FirebaseDatabase.getInstance().getReference().child("News");
        final DatabaseReference newsCountRef = newsRef.child("count");

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = dataSnapshot.getValue(Integer.class);
                count += 1;

                String msg = topic + body;
                newsRef.child(String.valueOf(count)).child("body").setValue(msg);
                newsCountRef.setValue(count);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        newsCountRef.addListenerForSingleValueEvent(listener);
        newsCountRef.removeEventListener(listener);
    }
}
