package com.example.gypc.petsday.model;

/**
 * Created by gypc on 2017/12/30.
 */

public class Good {
    private String good_name;
    private int good_price;
    private int good_id;
    private int good_count;
    private String good_info;

    public Good(String good_name,int good_price,int good_id,int good_count,String good_info){
        this.good_name = good_name;
        this.good_price = good_price;
        this.good_id = good_id;
        this.good_count = good_count;
        this.good_info = good_info;
    }

    public String getGood_name(){return good_name;}
    public int getGood_price(){return good_price;}
    public int getGood_id(){return  good_id;}
    public int getGood_count(){return good_count;}
    public String getGood_info(){return good_info;}
}
