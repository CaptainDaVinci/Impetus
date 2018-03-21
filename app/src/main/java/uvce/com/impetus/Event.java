package uvce.com.impetus;

public class Event {
    private int id;
    private String name, description;
    Coordinator coordinator;
    private int rounds;
    private String startTime, endTime;

    public Event(int _id, String _name, String _description,
                Coordinator _coordinator,
                String _start, String _end,
                int _rounds) {

        id = _id;
        name = _name;
        description = _description;
        coordinator = new Coordinator(_coordinator);
        rounds = _rounds;
        startTime = _start;
        endTime = _end;
    }

    public int getId() {
        return id;
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
