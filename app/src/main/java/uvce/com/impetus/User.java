package uvce.com.impetus;

import java.util.ArrayList;

public class User {
    private String name, college, branch, email, password;
    private int year;
    private ArrayList<Event> events;

    public  User() {
        // for firebase.
    }

    public User(String _name, String _email, String _college,
                String _branch, String _password, int _year) {

        name = _name;
        email = _email;
        college = _college;
        branch = _branch;
        password = _password;
        year = _year;

        events = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getCollege() {
        return college;
    }

    public String getBranch() {
        return branch;
    }

    public String getEmail() {
        return email;
    }

    public int getYear() {
        return year;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }
}
