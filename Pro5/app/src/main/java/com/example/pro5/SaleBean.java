package com.example.pro5;

class SaleBean {
    private String saleName;
    private int sales;
    private Float saleAmount;

    public SaleBean(String saleName, int sales, Float saleAmount) {
//        this.saleName = saleName;
//        this.sales = sales;
//        this.saleAmount = saleAmount;
        setSaleName(saleName);
        setSales(sales);
        setSaleAmount(saleAmount);
    }

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public float getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(float saleAmount) {
        this.saleAmount = saleAmount;
    }
}
