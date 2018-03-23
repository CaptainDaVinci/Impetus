package uvce.com.impetus;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class ShowScheduleActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.TAG;
    private ArrayList<Event> eventList;
    private ShowScheduleAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_schedule);

        Log.d(TAG,"Show schedule started!");

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("BUNDLE");
        eventList = new ArrayList<>((ArrayList<Event>) bundle.getSerializable("schedulelist"));
        Log.d(TAG, eventList.size() + " events scheduled!");

        eventAdapter = new ShowScheduleAdapter(eventList);
        populateRecyclerView();
    }

    private void populateRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);

        int spacing = getResources().getDimensionPixelSize(R.dimen.spacing);
        mRecyclerView.addItemDecoration(new ShowScheduleActivity.SpacesItemDecoration(spacing));
        mRecyclerView.setAdapter(eventAdapter);
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
}
