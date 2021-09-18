package com.example.androidproject;


public class members_bean {

    public static final String TABLE_NAME = "members";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_DOB = "dob";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_VISITS = "visits";
    public static final String COLUMN_PHOTO = "photo";
    public static final String COLUMN_STATUS = "status";

    private int id;
    private String name;
    private String address;
    private String city;
    private String dob;
    private String code;
    private int visits;
    private String photo;
    private int status;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_ADDRESS + " TEXT,"
                    + COLUMN_CITY + " TEXT,"
                    + COLUMN_DOB + " TEXT,"
                    + COLUMN_CODE + " TEXT,"
                    + COLUMN_VISITS + " TEXT,"
                    + COLUMN_PHOTO + " TEXT,"
                    + COLUMN_STATUS + " INT DEFAULT 1"
                    + ")";

    public members_bean() {
    }

    public members_bean(int id, String name, String address, String city, String dob, String code, int visits, String photo, int status) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.dob = dob;
        this.code = code;
        this.visits = visits;
        this.photo = photo;
        this.status = status;
    }

    public members_bean(String name, String address, String city, String dob, String code, int visits, String photo, int status) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.dob = dob;
        this.code = code;
        this.visits = visits;
        this.photo = photo;
        this.status = status;
    }

    public members_bean(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "members_bean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", dob='" + dob + '\'' +
                ", code='" + code + '\'' +
                ", visits='" + visits + '\'' +
                ", photo=" + photo +
                ", status=" + status +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}