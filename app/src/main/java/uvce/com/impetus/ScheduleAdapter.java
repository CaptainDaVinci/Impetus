package uvce.com.impetus;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.EventHolder> {
    private static String TAG = LoginActivity.TAG;
    private ArrayList<Event> eventList;
    private static boolean[] eventCheck;

    ScheduleAdapter(ArrayList<Event> _eventList) {
        eventList = _eventList;
        eventCheck = new boolean[30];
        for (int i = 0; i < 30; ++i) eventCheck[i] = false;
    }

    @Override
    public ScheduleAdapter.EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_row, parent, false);

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
        private TextView nameField;
        private Button addButton;
        private View view;
        private Event event;

        void bindEventInfo(Event _event) {
            event = _event;
            nameField.setText(event.getName());

            if (eventCheck[event.getId()]) {
                addButton.setBackgroundResource(R.drawable.ic_check_black_24dp);
            } else {
                addButton.setBackgroundResource(R.drawable.ic_add_black_24dp);
            }
        }

        EventHolder(View itemView) {
            super(itemView);
            view = itemView;


            nameField = itemView.findViewById(R.id.eventNameField);
            addButton = itemView.findViewById(R.id.addButton);

            addButton.setOnClickListener(null);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!eventCheck[event.getId()]) {
                        ScheduleActivity.addEvent(event);
                        addButton.setBackgroundResource(R.drawable.ic_check_black_24dp);
                    } else {
                        ScheduleActivity.deleteEvent(event);
                        addButton.setBackgroundResource(R.drawable.ic_add_black_24dp);
                    }

                    eventCheck[event.getId()] = !eventCheck[event.getId()];
                }
            });
        }
    }

}
