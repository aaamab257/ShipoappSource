package com.middle.east.shipo.data;

public class TraderHistory {
    String id , flat , flng , tlat , tlng ,time , history , price ,fee ,city ,details ,status ,createdat ;

    public TraderHistory(String id, String flat, String flng, String tlat, String tlng, String time, String history, String price, String fee, String city, String details, String status, String createdat) {
        this.id = id;
        this.flat = flat;
        this.flng = flng;
        this.tlat = tlat;
        this.tlng = tlng;
        this.time = time;
        this.history = history;
        this.price = price;
        this.fee = fee;
        this.city = city;
        this.details = details;
        this.status = status;
        this.createdat = createdat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getFlng() {
        return flng;
    }

    public void setFlng(String flng) {
        this.flng = flng;
    }

    public String getTlat() {
        return tlat;
    }

    public void setTlat(String tlat) {
        this.tlat = tlat;
    }

    public String getTlng() {
        return tlng;
    }

    public void setTlng(String tlng) {
        this.tlng = tlng;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedat() {
        return createdat;
    }

    public void setCreatedat(String createdat) {
        this.createdat = createdat;
    }
}
