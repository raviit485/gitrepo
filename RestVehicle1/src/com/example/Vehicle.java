package com.example;

import java.io.Serializable;

import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("serial")
public class Vehicle implements Serializable {
    //Car attributes
    private int id;
    private String makeModel;
    private int year;
    private double retailPrice;

    //Empty Constructor
    public Vehicle(){}

    //Constructor
    public Vehicle(int id, String makeModel, int year, double retailPrice){
        this.id = id;
        this.makeModel = makeModel;
        this.year = year;
        this.retailPrice = retailPrice;
    }

    //Convernt to attributes to string
    public String toString(){
        return this.getId() + ", " + this.makeModel + ", Year: " + this.year + ", Price: " + this.retailPrice;
    }

    //Convert car attributes to JSON object
    public String toJSON(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return (mapper.writeValueAsString(this));
        } catch (Exception e){
            return null;
        }
    }

    //Getters
    public int getId() {
        return id;
    }
    public String getMakeModel() {
        return makeModel;
    }
    public int getYear() {
        return year;
    }
    public double getRetailPrice() {
        return retailPrice;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }
    public void setMakeModel(String makeModel) {
        this.makeModel = makeModel;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

}

