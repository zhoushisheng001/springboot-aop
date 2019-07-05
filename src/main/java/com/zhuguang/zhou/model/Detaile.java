package com.zhuguang.zhou.model;

import java.io.Serializable;

public class Detaile implements Serializable {

    private String id;

    private String detaName;

    private Long number;

    private String goods;

    private int price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetaName() {
        return detaName;
    }

    public void setDetaName(String detaName) {
        this.detaName = detaName;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Detaile{" +
                "id='" + id + '\'' +
                ", detaName='" + detaName + '\'' +
                ", number=" + number +
                ", goods='" + goods + '\'' +
                ", price=" + price +
                '}';
    }
}
