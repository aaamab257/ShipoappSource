package com.middle.east.shipo.data;

public class PeopleData {
    String peopleImg , peoplename , peopleid  ;

    public PeopleData(String peopleImg, String peoplename, String peopleid) {
        this.peopleImg = peopleImg;
        this.peoplename = peoplename;
        this.peopleid = peopleid;
    }

    public String getPeopleImg() {
        return peopleImg;
    }

    public void setPeopleImg(String peopleImg) {
        this.peopleImg = peopleImg;
    }

    public String getPeoplename() {
        return peoplename;
    }

    public void setPeoplename(String peoplename) {
        this.peoplename = peoplename;
    }

    public String getPeopleid() {
        return peopleid;
    }

    public void setPeopleid(String peopleid) {
        this.peopleid = peopleid;
    }
}
