package com.middle.east.shipo.data;

public class CitiesData {
    private String CiteId;
    private String CiteName;

    public CitiesData(String citeId, String citeName) {
        CiteId = citeId;
        CiteName = citeName;
    }

    public String getCiteId() {
        return CiteId;
    }

    public void setCiteId(String citeId) {
        CiteId = citeId;
    }

    public String getCiteName() {
        return CiteName;
    }

    public void setCiteName(String citeName) {
        CiteName = citeName;
    }
}
