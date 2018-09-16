package com.example.model;
import java.util.*;
/**
 * @Class: untitled
 * @Author: Song
 * @Description:
 * @Create: 2018-09-13 15:22
 */
public class BeerExpert {
    public List getBrands(String color){
        List brands = new ArrayList();
        if(color.equals("amber")){
            brands.add("Jack Amber");
            brands.add("Red Moose");
        }
        else {
            brands.add("Jail Pale Ale");
            brands.add("Gout Stout");
        }
        return (brands);
    }
}
