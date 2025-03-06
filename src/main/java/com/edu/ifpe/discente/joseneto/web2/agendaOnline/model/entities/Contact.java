package com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.entities;

import java.util.List;

public class Contact {

    private int id;
    private int userId;
    private String name;
    private String email;
    private String address;
    private List<Phone> phones;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    @Override
    public String toString() {
        return "Contact [id=" + id + ", userId=" + userId + ", name=" + name + ", email=" + email + ", address="
                + address + ", phones=" + phones + "]";
    }

}
