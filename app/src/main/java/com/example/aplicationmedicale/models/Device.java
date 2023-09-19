package com.example.aplicationmedicale.models;

public class Device {
    private String name,value;

    public Device(String name, String value){
        this.name = name;
        this.name = value;
    }

    public Device() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

