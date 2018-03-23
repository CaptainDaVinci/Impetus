package uvce.com.impetus;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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


    // show the dialog box, containing user information.
    private static void showUserInformation(final ViewUser user, Context context) {
        final AlertDialog.Builder userInfoDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.user_dialog, null);

        TextView emailTextView = view.findViewById(R.id.dialog_email_field),
                branchTextView = view.findViewById(R.id.dialog_branch_field),
                collegeField = view.findViewById(R.id.dialog_college_field),
                yearTextView = view.findViewById(R.id.dialog_year_field),
                keyTextView = view.findViewById(R.id.dialog_key_field);

        String email = "\u2022  " + user.getEmail(),
                branch = "\u2022  " + user.getBranch(),
                college = "\u2022  " + user.getCollege(),
                year = "\u2022  " + user.getYear(),
                key = "\u2022  Key : " + user.getKey();

        emailTextView.setText(email);
        branchTextView.setText(branch);
        collegeField.setText(college);
        yearTextView.setText(year);

        if (user.getKey() == null) {
            keyTextView.setVisibility(View.GONE);
        } else {
            keyTextView.setText(key);
        }

        try {
            userInfoDialog.setView(view)
                    .setTitle(user.getName())
                    .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            justClicked = false;
                            dialogInterface.dismiss();
                        }
                    })
                    .setIcon(R.drawable.ic_account_circle_black_24dp)
                    .setCancelable(false)
                    .show();
        } catch (Exception e) {
            Toast.makeText(context, "Oops! Something went wrong",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
