package uvce.com.impetus;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Event implements Serializable, Comparable<Event> {
    private int id, rounds;
    private String name, venue;
    private String day1, day2;
    private String image;
    private boolean admin, registered;

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

    private int getStartIdx(String s) {
        int idx = 0;

        while (s.charAt(idx) != '-' && idx < s.length()) {
            ++idx;
        }

        return idx + 1;
    }

    private String getRound1StartTime() {
        StringBuilder time = new StringBuilder();
        if (day1.contains("round1")) {
            int startIdx = getStartIdx(day1);

            for (int i = startIdx; day1.charAt(i) != '-'; ++i) {
                time.append(day1.charAt(i));
            }
        } else if (day2.contains("round1")) {
            int startIdx = getStartIdx(day2);

            for (int i = startIdx; day2.charAt(i) != '-'; ++i) {
                time.append(day2.charAt(i));
            }
        }

        return time.toString();
    }

    private String getRound1EndTime() {
        StringBuilder time = new StringBuilder();
        if (day1.contains("round1")) {
            int startIdx = getStartIdx(day1);

            while (day1.charAt(startIdx) != '-') {
                ++startIdx;
            }

            ++startIdx;

            for (int i = startIdx; i < day1.length() && day1.charAt(i) != ','; ++i) {
                time.append(day1.charAt(i));
            }

        } else if (day2.contains("round1")) {
            int startIdx = getStartIdx(day2);

            while (day2.charAt(startIdx) != '-') {
                ++startIdx;
            }

            ++startIdx;

            for (int i = startIdx; i < day2.length() && day2.charAt(i) != ','; ++i) {
                time.append(day2.charAt(i));
            }
        }

        return time.toString();
    }

    private String getRound1Day() {
        if (day1.contains("round1")) {
            return "day1";
        } else if (day2.contains("round2")) {
            return "day2";
        }

        return null;
    }

    boolean allDay() {
        return day1.contains("allday");
    }

    boolean collidesWith(Event otherEvent) {
        if (!Objects.equals(getRound1Day(), otherEvent.getRound1Day())) {
            return false;
        }

        String e1StartTime = getRound1StartTime();
        String e1EndTime = getRound1EndTime();
        String e2StartTime = otherEvent.getRound1StartTime();
        String e2EndTime = otherEvent.getRound1EndTime();

        Integer h1Start = Integer.parseInt(e1StartTime.charAt(0) + "" + e1StartTime.charAt(1));
        Integer h1End = Integer.parseInt(e1EndTime.charAt(0) + "" + e1EndTime.charAt(1));
        Integer h2Start = Integer.parseInt(e2StartTime.charAt(0) + "" + e2StartTime.charAt(1));
        Integer h2End = Integer.parseInt(e2EndTime.charAt(0) + "" + e2EndTime.charAt(1));

        return (h1Start >= h2Start && h1Start < h2End) || (h2Start >= h1Start && h2Start < h1End);
    }

    boolean registrationPossible() {
        return day1.contains("round1") || day2.contains("round1") || day1.contains("allday");
    }

    public String getName() {
        return name;
    }

    boolean isRegistered() {
        return registered;
    }

    void setRegistered(boolean registered) {
        this.registered = registered;
    }

    String getVenue() {
        return venue;
    }

    int getRounds() {
        return rounds;
    }

    String getDay1() {
        return day1;
    }

    String getDay2() {
        return day2;
    }

    public String getImage() {
        return image;
    }

    boolean isAdmin() {
        return admin;
    }

    void setAdmin(boolean _admin) {
        admin = _admin;
    }

    public String getInfo() {
        return id + " " + name + " " + venue + " "
                + rounds + " " + day1 + " " + day2;
    }

    @Override
    public int compareTo(@NonNull Event e2) {
        if (day1.equals("-1") && e2.getDay1().equals("-1")) {
            if (!day2.equals("-1") && e2.getDay2().equals("-1")) {
                return -1;
            }

            if (day2.equals("-1") && !e2.getDay2().equals("-1")) {
                return 1;
            }

            if (day2.equals("allday")) {
                return -1;
            }

            if (e2.day2.equals("allday")) {
                return 1;
            }

            Log.d(LoginActivity.TAG, "E1 " + name + " E2: " + e2.getName());

            String e1StartTime = getDay2StartTime();
            String e1EndTime = getDay2EndTime();
            String e2StartTime = e2.getDay2StartTime();
            String e2EndTime = e2.getDay2EndTime();

            Log.d(LoginActivity.TAG, "E1: " + e1StartTime + "-" + e1EndTime);
            Log.d(LoginActivity.TAG, "E2: " + e2StartTime + "-" + e2EndTime);

            Integer h1Start = Integer.parseInt(e1StartTime.charAt(0) + "" + e1StartTime.charAt(1));
            Integer h1End = Integer.parseInt(e1EndTime.charAt(0) + "" + e1EndTime.charAt(1));
            Integer h2Start = Integer.parseInt(e2StartTime.charAt(0) + "" + e2StartTime.charAt(1));
            Integer h2End = Integer.parseInt(e2EndTime.charAt(0) + "" + e2EndTime.charAt(1));


            if (Objects.equals(h1Start, h2Start)) {
                return h1End - h2End;
            }

            return h1Start - h2Start;
        }

        if (!day1.equals("-1") && e2.getDay1().equals("-1")) {
            return -1;
        }

        if (day1.equals("-1") && !e2.getDay1().equals("-1")) {
            return 1;
        }

        if (day1.equals("allday")) {
            return -1;
        }

        if (e2.day1.equals("allday")) {
            return 1;
        }

        Log.d(LoginActivity.TAG, "E1 " + name + " E2: " + e2.getName());

        String e1StartTime = getDay1StartTime();
        String e1EndTime = getDay1EndTime();
        String e2StartTime = e2.getDay1StartTime();
        String e2EndTime = e2.getDay1EndTime();

        Log.d(LoginActivity.TAG, "E1: " + e1StartTime + "-" + e1EndTime);
        Log.d(LoginActivity.TAG, "E2: " + e2StartTime + "-" + e2EndTime);

        Integer h1Start = Integer.parseInt(e1StartTime.charAt(0) + "" + e1StartTime.charAt(1));
        Integer h1End = Integer.parseInt(e1EndTime.charAt(0) + "" + e1EndTime.charAt(1));
        Integer h2Start = Integer.parseInt(e2StartTime.charAt(0) + "" + e2StartTime.charAt(1));
        Integer h2End = Integer.parseInt(e2EndTime.charAt(0) + "" + e2EndTime.charAt(1));

        if (Objects.equals(h1Start, h2Start)) {
            return h1End - h2End;
        }

        return h1Start - h2Start;
    }

    private String getDay1StartTime() {
        StringBuilder res = new StringBuilder();
        int startIdx = 0;

        while (!Character.isDigit(day1.charAt(startIdx))) {
            ++startIdx;
        }
        startIdx += 2;

        while (startIdx < day1.length() && day1.charAt(startIdx) != '-') {
            res.append(day1.charAt(startIdx));
            ++startIdx;
        }

        return res.toString();
    }

    private String getDay1EndTime() {
        StringBuilder res = new StringBuilder();
        int startIdx = 0;

        while (!Character.isDigit(day1.charAt(startIdx))) {
            ++startIdx;
        }
        startIdx += 2;

        while (day1.charAt(startIdx) != '-') {
            ++startIdx;
        }

        ++startIdx;

        while (startIdx < day1.length() && day1.charAt(startIdx) != ',') {
            res.append(day1.charAt(startIdx));
            ++startIdx;
        }

        return res.toString();
    }

    private String getDay2EndTime() {
        StringBuilder res = new StringBuilder();
        int startIdx = 0;

        while (!Character.isDigit(day2.charAt(startIdx))) {
            ++startIdx;
        }
        startIdx += 2;

        while (startIdx < day2.length() && day2.charAt(startIdx) != '-') {
            res.append(day2.charAt(startIdx));
            ++startIdx;
        }

        return res.toString();
    }

    private String getDay2StartTime() {
        StringBuilder res = new StringBuilder();
        int startIdx = 0;

        while (!Character.isDigit(day2.charAt(startIdx))) {
            ++startIdx;
        }
        startIdx += 2;

        while (day2.charAt(startIdx) != '-') {
            ++startIdx;
        }

        ++startIdx;

        while (startIdx < day2.length() && day2.charAt(startIdx) != ',') {
            res.append(day2.charAt(startIdx));
            ++startIdx;
        }

        return res.toString();
    }
}
