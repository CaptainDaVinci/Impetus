package uvce.com.impetus;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

class EventAdapterSmall extends RecyclerView.Adapter<EventAdapterSmall.EventHolder> {
    private static String TAG = LoginActivity.TAG;
    private ArrayList<String> eventList;

    EventAdapterSmall(ArrayList<String> _eventList) {
        eventList = _eventList;
    }

    @Override
    public EventAdapterSmall.EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item_small, parent, false);

        return new EventHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(EventHolder holder, int position) {
        String event = eventList.get(position);

        holder.bindEventInfo(event);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class EventHolder extends RecyclerView.ViewHolder {
        private TextView nameField;
        private View view;
        private String event;

        void bindEventInfo(String _event) {
            event = _event;
            nameField.setText(event);
        }

        EventHolder(View itemView) {
            super(itemView);
            view = itemView;

            nameField = itemView.findViewById(R.id.eventNameField);
        }
    }

}
