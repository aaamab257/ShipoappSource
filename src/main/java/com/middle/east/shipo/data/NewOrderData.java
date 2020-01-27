package com.middle.east.shipo.data;

public class NewOrderData {
    String id , orderName, OrderDetails , order_image , price , fee , orderPhone , ordercityf ,orderareaf,orderaddressf,ordercity2,orderarea2,orderaddress2,history , time , ocrete , ostatus  ;

    public NewOrderData(String id, String orderName, String orderDetails, String order_image, String price, String fee, String orderPhone,String ordercityf, String orderareaf, String orderaddressf, String ordercity2, String orderarea2, String orderaddress2, String history, String time, String ocrete, String ostatus) {
        this.id = id;
        this.orderName = orderName;
        OrderDetails = orderDetails;
        this.order_image = order_image;
        this.price = price;
        this.fee = fee;
        this.orderPhone = orderPhone;
        this.ordercityf = ordercityf;
        this.history = history;
        this.time = time;
        this.ocrete = ocrete;
        this.ostatus = ostatus;
        this.ordercityf = ordercityf;
        this.orderareaf = orderareaf;
        this.orderaddressf = orderaddressf;
        this.ordercity2 = ordercity2;
        this.orderarea2 = orderarea2;
        this.orderaddress2 = orderaddress2;
    }


    public String getOrdercityf() {
        return ordercityf;
    }

    public void setOrdercityf(String ordercityf) {
        this.ordercityf = ordercityf;
    }

    public String getOrderareaf() {
        return orderareaf;
    }

    public void setOrderareaf(String orderareaf) {
        this.orderareaf = orderareaf;
    }

    public String getOrderaddressf() {
        return orderaddressf;
    }

    public void setOrderaddressf(String orderaddressf) {
        this.orderaddressf = orderaddressf;
    }

    public String getOrdercity2() {
        return ordercity2;
    }

    public void setOrdercity2(String ordercity2) {
        this.ordercity2 = ordercity2;
    }

    public String getOrderarea2() {
        return orderarea2;
    }

    public void setOrderarea2(String orderarea2) {
        this.orderarea2 = orderarea2;
    }

    public String getOrderaddress2() {
        return orderaddress2;
    }

    public void setOrderaddress2(String orderaddress2) {
        this.orderaddress2 = orderaddress2;
    }

    public String getOcrete() {
        return ocrete;
    }

    public void setOcrete(String ocrete) {
        this.ocrete = ocrete;
    }

    public String getOstatus() {
        return ostatus;
    }

    public void setOstatus(String ostatus) {
        this.ostatus = ostatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderDetails() {
        return OrderDetails;
    }

    public void setOrderDetails(String orderDetails) {
        OrderDetails = orderDetails;
    }

    public String getOrder_image() {
        return order_image;
    }

    public void setOrder_image(String order_image) {
        this.order_image = order_image;
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

    public String getOrderPhone() {
        return orderPhone;
    }

    public void setOrderPhone(String orderPhone) {
        this.orderPhone = orderPhone;
    }


    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
