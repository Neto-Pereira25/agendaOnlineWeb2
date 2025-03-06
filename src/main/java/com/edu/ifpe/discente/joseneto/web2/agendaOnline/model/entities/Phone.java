package com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.entities;

public class Phone {

    private int id;
    private int contactId;
    private String phoneNumber;
    private String phoneType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    @Override
    public String toString() {
        return "Phone [id=" + id + ", contactId=" + contactId + ", phoneNumber=" + phoneNumber + ", phoneType="
                + phoneType + "]";
    }

}
