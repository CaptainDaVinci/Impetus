package uvce.com.impetus;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventHolder> {
    private static String TAG = LoginActivity.TAG;
    private ArrayList<Event> eventList;

    EventAdapter(ArrayList<Event> _eventList) {
        eventList = _eventList;
    }

    @Override
    public EventAdapter.EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);

        return new EventHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(EventHolder holder, int position) {
        Event event = eventList.get(position);

        holder.bindEventInfo(event);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class EventHolder extends RecyclerView.ViewHolder {
        private TextView nameField, venueField,
                roundsField, startTimeField, endTimeField;
        private Button infoButton, registerButton, viewRegistrations;
        private View view;
        private Event event;

        void bindEventInfo(Event _event) {
            event = _event;

            nameField.setText(event.getName());
            venueField.setText(event.getVenue());
            roundsField.setText(String.valueOf(event.getRounds()));

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

            startTimeField.setText(day1);
            endTimeField.setText(day2);

            if (event.isAdmin()) {
                viewRegistrations.setVisibility(View.VISIBLE);
                registerButton.setText("Admin");
                registerButton.setBackgroundColor(Color.GRAY);
            } else if (event.isRegistered()) {
                registerButton.setText("Key");
                registerButton.setBackgroundColor(Color.BLUE);
            } else {
                registerButton.setText("Register");
                registerButton.setBackgroundColor(Color.parseColor("#0ed218"));
                viewRegistrations.setVisibility(View.GONE);
            }
        }

        EventHolder(View itemView) {
            super(itemView);
            view = itemView;

            nameField = itemView.findViewById(R.id.nameField);
            venueField = itemView.findViewById(R.id.venueField);
            roundsField = itemView.findViewById(R.id.roundField);
            startTimeField = itemView.findViewById(R.id.startTimeField);
            endTimeField = itemView.findViewById(R.id.endTimeField);

            infoButton = itemView.findViewById(R.id.infoButton);
            registerButton = itemView.findViewById(R.id.registerButton);
            viewRegistrations = itemView.findViewById(R.id.viewRegisteredButton);

            infoButton.setOnClickListener(infoListener);
            registerButton.setOnClickListener(registerListener);
            viewRegistrations.setOnClickListener(viewRegistrationsListener);
        }

        private View.OnClickListener infoListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(view.getContext(), EventInfoActivity.class);
                intent.putExtra("event", event);

                view.getContext().startActivity(intent);
            }
        };

        private View.OnClickListener registerListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (event.isAdmin()) {
                    Toast.makeText(view.getContext(), "Admins can't register!",
                            Toast.LENGTH_SHORT).show();
                    return ;
                }


                Intent intent;

                if (event.isRegistered()) {
                    intent = new Intent(view.getContext(), ConfirmKeyActivity.class);
                    intent.putExtra("eventId", event.getId());
                } else {
                    intent = new Intent(view.getContext(), RegisterActivity.class);
                    intent.putExtra("event", event);
                }

                view.getContext().startActivity(intent);
            }
        };

        private View.OnClickListener viewRegistrationsListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), RegisteredActivity.class);
                intent.putExtra("eventId", event.getId());
                view.getContext().startActivity(intent);
            }
        };
    }

}
