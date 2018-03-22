package uvce.com.impetus;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by captaindavinci on 27/10/17.
 */

class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventHolder> {
    private static String TAG = LoginActivity.TAG;
    private ArrayList<Event> eventList;
    private Context context;

    EventAdapter(ArrayList<Event> _eventList, Context c) {
        eventList = _eventList;
        context = c;
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

        private Event event;

        void bindEventInfo(Event _event) {
            event = _event;

            nameField.setText(event.getName());
            venueField.setText(event.getVenue());
            roundsField.setText(String.valueOf(event.getRounds()));
            startTimeField.setText(event.getStartTime());
            endTimeField.setText(event.getEndTime());

            if (event.isAdmin()) {
                viewRegistrations.setVisibility(View.VISIBLE);
            } else {
                viewRegistrations.setVisibility(View.GONE);
            }
        }

        EventHolder(View itemView) {
            super(itemView);


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
                Log.d(TAG, "Info clicked");
            }
        };

        private View.OnClickListener registerListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Register clicked");
            }
        };

        private View.OnClickListener viewRegistrationsListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "View registered clicked");
            }
        };
    }

}
