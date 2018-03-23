package uvce.com.impetus;

import java.io.Serializable;

public class User implements Serializable {

    private String name, college, branch, email, password;
    private boolean superAdmin;
    private int id;
    private String year;
    private long events;
    private long eventAdmin;

    public  User() {
        // for firebase.
    }

    User(String _name, String _email, String _college,
         String _branch, String _password, String _year) {

        name = _name;
        email = _email;
        college = _college;
        branch = _branch;
        password = _password;
        year = _year;

        superAdmin = false;
        events = eventAdmin = 0;
    }

    public String getName() {
        return name;
    }

    String getCollege() {
        return college;
    }

    String getBranch() {
        return branch;
    }

    String getEmail() {
        return email;
    }

    String getYear() {
        return year;
    }

    String getPassword() {
        return password;
    }

    public boolean isSuperAdmin() {
        return superAdmin;
    }

    public long getEvents() {
        return events;
    }

    public long getEventAdmin() {
        return eventAdmin;
    }

    public boolean isEventAdmin(int eventId) {
        return (eventAdmin & (1 << eventId)) != 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int _id) {
        id = _id;
    }

    public boolean isParticipatingInEvent(int eventId) {
        return (events & (1 << eventId)) != 0;
    }

    public String showInfo() {
        return name + " " + college + " " + branch + " " + " " + year + " "
                + email + " " + password +  " " + superAdmin + " " + eventAdmin + " " + events;
    }
}
