package com.rifat.qrscanner.activity.model;

public class Bar {
    private Integer id;
    private String barcode;
    private String datetime;

    public Bar(Integer id, String barcode, String datetime) {
        this.id = id;
        this.barcode = barcode;
        this.datetime = datetime;
    }

    public Bar(String barcode) {
        this.barcode = barcode;
    }

    public Integer getId() {
        return id;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getDatetime() {
        return datetime;
    }
}

