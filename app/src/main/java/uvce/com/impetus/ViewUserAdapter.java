package uvce.com.impetus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

class ViewUserAdapter extends RecyclerView.Adapter<ViewUserAdapter.UserInfoHolder> {
    private static String TAG = LoginActivity.TAG;
    private ArrayList<ViewUser> usersList;
    private static boolean justClicked;

    ViewUserAdapter(ArrayList<ViewUser> _userList) {
        usersList = _userList;
        justClicked = false;
    }

    @Override
    public ViewUserAdapter.UserInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_user_row, parent, false);

        return new UserInfoHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(UserInfoHolder holder, int position) {
        ViewUser user = usersList.get(position);

        holder.bindEventInfo(user);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    static class UserInfoHolder extends RecyclerView.ViewHolder
                    implements View.OnClickListener {
        private TextView nameField, keyField;
        private View view;
        private ViewUser user;

        void bindEventInfo(ViewUser _user) {
            user = _user;

            nameField.setText(user.getName());

            if (user.getKey() == null) {
                keyField.setVisibility(View.GONE);
            } else {
                keyField.setText(user.getKey());
            }
        }

        UserInfoHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            view = itemView;

            nameField = itemView.findViewById(R.id.nameField);
            keyField = itemView.findViewById(R.id.confirmKeyField);
        }

        @Override
        public void onClick(View v) {
            if (justClicked) {
                return ;
            }

            justClicked = true;
            showUserInformation(user, v.getContext());
        }
    }


    private static void showUserInformation(final ViewUser user, Context context) {
        Intent intent = new Intent(context, MyAccountActivity.class);
        intent.putExtra("userId", user.getId());
        context.startActivity(intent);

        justClicked = false;
    }
}
