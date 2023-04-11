package jvaug.mycontactlist;

import java.io.Serializable;

public class Contact implements Serializable {
    int db_id = -1;
    String name;
    String address;
    String city;
    String state;
    int zipcode;
    String home_phone;
    String cell_phone;
    String email;
    String birthday;

    public Contact() {
        name = "";
        address = "";
        city = "";
        state = "";
        zipcode = 0;
        home_phone = "";
        cell_phone = "";
        email = "";
        birthday = "";
    }
    public Contact(String name, String address, String city, String state,
                   int zipcode, String home_phone, String cell_phone, String email, String birthday) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.home_phone = home_phone;
        this.cell_phone = cell_phone;
        this.email = email;
        this.birthday = birthday;
    }

    public int getID() { return db_id; }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public int getZipCode() {
        return zipcode;
    }

    public String getHomePhone() {
        return home_phone;
    }

    public String getCellPhone() {
        return cell_phone;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setID(int id) { this.db_id = id; }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZipCode(int zipcode) {
        this.zipcode = zipcode;
    }

    public void setHomePhone(String home_phone) {
        this.home_phone = home_phone;
    }

    public void setCellPhone(String cell_phone) {
        this.cell_phone = cell_phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

}
