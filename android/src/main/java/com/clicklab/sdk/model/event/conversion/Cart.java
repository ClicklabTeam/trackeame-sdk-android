package com.clicklab.sdk.model.event.conversion;


import android.support.annotation.NonNull;

import org.json.JSONArray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Cart {
    private List<Product> cart = new LinkedList<>();

    public Cart(@NonNull List<Product> cart) {
        this.cart = cart;
    }

    public JSONArray getJsonObject() {
        JSONArray array = new JSONArray();
        for (Product prod : cart) {
            array.put(prod.getJsonObject());
        }
        return array;
    }
}
