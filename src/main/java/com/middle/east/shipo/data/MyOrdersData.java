package com.middle.east.shipo.data;

public class MyOrdersData {
    String id , date , time , price , fee , details ,status ,name , phone ,fromCity , fromArea , fromAddress ,tCity , tArea , tAddress  , reason ,user_id;

    public MyOrdersData(String id, String date, String time, String price, String fee, String details, String status, String name, String phone, String fromCity, String fromArea, String fromAddress, String tCity, String tArea, String tAddress, String reason, String user_id) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.price = price;
        this.fee = fee;
        this.details = details;
        this.status = status;
        this.name = name;
        this.phone = phone;
        this.fromCity = fromCity;
        this.fromArea = fromArea;
        this.fromAddress = fromAddress;
        this.tCity = tCity;
        this.tArea = tArea;
        this.tAddress = tAddress;
        this.reason = reason;
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public String getFromArea() {
        return fromArea;
    }

    public void setFromArea(String fromArea) {
        this.fromArea = fromArea;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String gettCity() {
        return tCity;
    }

    public void settCity(String tCity) {
        this.tCity = tCity;
    }

    public String gettArea() {
        return tArea;
    }

    public void settArea(String tArea) {
        this.tArea = tArea;
    }

    public String gettAddress() {
        return tAddress;
    }

    public void settAddress(String tAddress) {
        this.tAddress = tAddress;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
