package com.middle.east.shipo.data;

public class OnWayTraderOrdersData {
    String OrderID ,MandobName , Mandobphone , orderDetails , orderPrice ;

    public OnWayTraderOrdersData(String orderID, String mandobName, String mandobphone, String orderDetails, String orderPrice ) {
        OrderID = orderID;
        MandobName = mandobName;
        Mandobphone = mandobphone;
        this.orderDetails = orderDetails;
        this.orderPrice = orderPrice;

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
