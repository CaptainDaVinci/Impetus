package uvce.com.impetus;

public class Coordinator {
    private String name, email, phone;
    private String alternatePhone;

    public Coordinator(String _name, String _email, String _phone,
                        String _alternatePhone) {
        name = _name;
        email = _email;
        phone = _phone;
        alternatePhone = _alternatePhone;
    }

    public Coordinator(Coordinator _coordinator) {
        name = _coordinator.name;
        email = _coordinator.email;
        phone = _coordinator.phone;
        alternatePhone = _coordinator.alternatePhone;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAlternatePhone() {
        return alternatePhone;
    }
}
