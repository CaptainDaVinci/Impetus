package uvce.com.impetus;

public class Event {
    private int id, rounds;
    private String name, description, venue;
    private Coordinator coordinator;
    private String startTime, endTime;

    public int getId() {
        return id;
    }

    public Event(int _id, int _rounds,
                 String _name, String _description, String _venue,
                 Coordinator _coordinator,
                 String _start, String _end) {

        id = _id;
        rounds = _rounds;
        name = _name;
        venue = _venue;
        description = _description;
        coordinator = new Coordinator(_coordinator);
        startTime = _start;
        endTime = _end;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Coordinator getCoordinator() {
        return coordinator;
    }

    public int getRounds() {
        return rounds;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
