package uvce.com.impetus;

import java.util.ArrayList;

public class Event {
    private int id, rounds;
    private String name, description, venue;
    private String coordinatorName, contact;
    private String day1, day2;
    private String image;
    private boolean admin;
    public ArrayList<Integer> registeredUsers;

    public int getId() {
        return id;
    }

    public Event(int _id, int _rounds,
                 String _name, String _venue,
                 String _day1, String _day2,
                String _image) {

        id = _id;
        rounds = _rounds;
        name = _name;
        venue = _venue;
        day1 = _day1;
        day2 = _day2;
        image = _image;

        admin = false;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getVenue() {
        return venue;
    }

    public String getCoordinatorName() {
        return coordinatorName;
    }

    public String getContact() {
        return contact;
    }

    public int getRounds() {
        return rounds;
    }

    public String getDay1() {
        return day1;
    }

    public String getDay2() {
        return day2;
    }

    public String getImage() {
        return image;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean _admin) {
        admin = _admin;
    }

    public String getInfo() {
        return id + " " + name + " " + venue + " "
                + rounds + " " + day1 + " " + day2;
    }
}
