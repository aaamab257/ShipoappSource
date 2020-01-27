package com.middle.east.shipo.data;

public class DelegatesInfo {
    String id , name , offerId , phone ,rate ;

    public DelegatesInfo(String id, String name, String offerId, String phone, String rate) {
        this.id = id;
        this.name = name;
        this.offerId = offerId;
        this.phone = phone;
        this.rate = rate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
