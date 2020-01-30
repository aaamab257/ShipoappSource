package com.middle.east.shipo.data;

public class OnWayTraderOrdersData {
    String OrderID ,MandobName , Mandobphone , orderDetails , orderPrice , deleid ,status , fcity , tcity , fee;

    public OnWayTraderOrdersData(String orderID, String mandobName, String mandobphone, String orderDetails, String orderPrice,String deleid,String status,String fcity,String tcity,String fee) {
        OrderID = orderID;
        MandobName = mandobName;
        Mandobphone = mandobphone;
        this.orderDetails = orderDetails;
        this.orderPrice = orderPrice;
        this.deleid = deleid;
        this.status = status;
        this.fcity = fcity;
        this.tcity = tcity;
        this.fee = fee;
    }

    public String getFcity() {
        return fcity;
    }

    public void setFcity(String fcity) {
        this.fcity = fcity;
    }

    public String getTcity() {
        return tcity;
    }

    public void setTcity(String tcity) {
        this.tcity = tcity;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeleid() {
        return deleid;
    }

    public void setDeleid(String deleid) {
        this.deleid = deleid;
    }

    public String getMandobName() {
        return MandobName;
    }

    public String getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(String orderDetails) {
        this.orderDetails = orderDetails;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public void setMandobName(String mandobName) {
        MandobName = mandobName;
    }

    public String getMandobphone() {
        return Mandobphone;
    }

    public void setMandobphone(String mandobphone) {
        Mandobphone = mandobphone;
    }



    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }
}
