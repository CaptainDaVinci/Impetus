package uvce.com.impetus;

/**
 * Created by captaindavinci on 23/3/18.
 */

public class ViewUser {
    private String name, college, branch,
                    year, email, key;
    private int id;

    public ViewUser(String name, String college, String branch, String year, String email, String key) {
        this.name = name;
        this.college = college;
        this.branch = branch;
        this.year = year;
        this.email = email;
        this.key = key;
    }

    public ViewUser(User user, String _key) {
        key = _key;
        name = user.getName();
        college = user.getCollege();
        year = user.getYear();
        email = user.getEmail();
        branch = user.getBranch();
        id = user.getId();
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

    public String getYear() {
        return year;
    }

    public String getEmail() {
        return email;
    }

    public String getKey() {
        return key;
    }

    public int getId() {
        return id;
    }
}
