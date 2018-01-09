package com.clicklab.sdk.model.event.conversion;


import com.clicklab.sdk.utils.JSonUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Product {
    private Map<String, String> map = new HashMap<>(3);
    private Map<String, String> extraMap = new HashMap<>(0);
    private static final String TRANSACTION_SUB_ID = "id_subtr";
    private static final String PRICE = "pri";
    private static final String PRODUCT = "pr";
    private static final String PRODUCT_EXTRA_PARAMS = "extra_params";

    public Product(long transactionSubId) {
        setTransactionId(transactionSubId);
    }

    public Product(long transactionSubId, String productName) {
        setTransactionId(transactionSubId);
        setProductName(productName);
    }

    public Product(long transactionSubId, String productName, float price) {
        setTransactionId(transactionSubId);
        setProductName(productName);
        setPrice(price);
    }

    private void setPrice(float price) {
        map.put(PRICE, String.valueOf(price));
    }

    JSONObject getJsonObject() {
        JSONObject json = new JSONObject(map);
        JSonUtils.addStringMapToJsonObjectIfIsNotEmpty(json, PRODUCT_EXTRA_PARAMS, extraMap);
        return json;
    }

    private void setProductName(String productName) {
        map.put(PRODUCT, productName);
    }

    private void setTransactionId(long transactionSubId) {
        map.put(TRANSACTION_SUB_ID, String.valueOf(transactionSubId));
    }

    public void setExtraParameter(String parameterName, String parameterValue) {
        extraMap.put(parameterName, parameterValue);
    }
}

