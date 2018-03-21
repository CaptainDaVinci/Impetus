package uvce.com.impetus;

import java.util.HashSet;
import java.util.Set;

public class User {
    private String name, college, branch, email;
    private int year;
    private Set<Integer> events;

    public User(String _name, String _email, String _college,
                String _branch, int _year) {

        name = _name;
        email = _email;
        college = _college;
        branch = _branch;
        year = _year;

        events = new HashSet<Integer>();
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

    public Set<Integer> getEvents() {
        return events;
    }
}
