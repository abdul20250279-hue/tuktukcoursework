package datamodels;

public class dealer {
    private String code;
    private String name;
    private String phone;
    private String location;


    public dealer(String code, String name, String phone, String location) {
        this.code = code;
        this.name = name;
        this.phone = (phone == null || phone.trim().isEmpty()) ? "N/A" : phone;
        this.location = location;
    }


    public String getCode() { return code; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getLocation() { return location; }


    @Override
    public String toString() {
        return code + " - " + name + " | " + phone + " | " + location;
    }
}
