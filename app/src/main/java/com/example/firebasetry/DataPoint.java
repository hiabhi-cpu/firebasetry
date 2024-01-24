package com.example.firebasetry;

public class DataPoint {
    private int xValues,yValues;

    public DataPoint(int xValues, int yValues) {
        this.xValues = xValues;
        this.yValues = yValues;
    }

    public DataPoint() {
    }

    public int getxValues() {
        return xValues;
    }

    public int getyValues() {
        return yValues;
    }
}
