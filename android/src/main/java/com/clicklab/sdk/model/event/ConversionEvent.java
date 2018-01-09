
package com.clicklab.sdk.model.event;

import android.support.annotation.NonNull;

import com.clicklab.sdk.model.event.conversion.Cart;
import com.clicklab.sdk.model.event.conversion.Product;
import com.clicklab.sdk.utils.JSonUtils;
import com.clicklab.sdk.utils.Preconditions;

import org.json.JSONObject;

import java.util.Currency;
import java.util.List;

public final class ConversionEvent extends BaseEvent {
    private static final String TRANSACTION_ID = "id_tr";
    private static final String PRODUCT = "pr";
    private static final String PRICE = "pri";
    private static final String EXCHANGE_RATE = "exch";
    private static final String CURRENCY = "cur";
    private static final String CART = "cart";

    private Cart cart;

    /**
     * Creates a new Conversion Event
     *
     * @param transactionId identificator of the transaction
     */
    public ConversionEvent(String transactionId) {
        setEventType(EventType.conversion);
        setTransactionId(transactionId);
    }

    /**
     * Creates a new Conversion Event
     *
     * @param transactionId identificator of the transaction
     * @param productName   Name of the product that produced the transaction
     */
    public ConversionEvent(String transactionId, @NonNull String productName) {
        setEventType(EventType.conversion);
        setTransactionId(transactionId);
        setProductName(productName);
    }

    /**
     * Creates a new Conversion Event
     *
     * @param transactionId identificator of the transaction
     * @param productName   Name of the product that produced the transaction
     * @param price         Price of the product expressed in local currency
     * @param exchangeRate  exchange rate between the currency and USD
     * @param currency      The currency in which the transaction has been made
     */
    public ConversionEvent(String transactionId, @NonNull String productName, float price, float exchangeRate, @NonNull Currency currency) {
        setEventType(EventType.conversion);
        setTransactionId(transactionId);
        setProductName(productName);
        setPriceExchangeAndCurrency(price, exchangeRate, currency);
    }

    /**
     * Creates a new Conversion Event
     *
     * @param transactionId identificator of the transaction
     * @param productName   Name of the product that produced the transaction
     * @param price         Price of the product expressed in local currency
     * @param exchangeRate  exchange rate between the currency and USD
     * @param currency      The currency in which the transaction has been made
     * @param productList   Product list containing the cart
     */
    public ConversionEvent(String transactionId, @NonNull String productName, float price, float exchangeRate, @NonNull Currency currency, List<Product> productList) {
        setEventType(EventType.conversion);
        setTransactionId(transactionId);
        setProductName(productName);
        setPriceExchangeAndCurrency(price, exchangeRate, currency);
        setProducts(productList);
    }

    /**
     * Sets the product lists
     *
     * @param productList product list included in the Conversion Event
     * @return an instance of ConversionEvent
     */
    public ConversionEvent setProducts(List<Product> productList) {
        this.cart = new Cart(productList);
        return this;
    }

    /**
     * Sets the extra parameters to the event
     *
     * @param parameters The extra parameters that need to be tracked
     * @return an instance of ConversionEvent
     */
    public ConversionEvent setConversionExtraParameters(Parameters parameters) {
        setExtraParameters(parameters);
        return this;
    }

    @Override
    protected JSONObject getJsonObject() {
        JSONObject json = super.getJsonObject();
        if (cart != null) {
            JSonUtils.addArrayToJsonObject(json, CART, cart.getJsonObject());
        }
        return json;
    }

    private BaseEvent setTransactionId(String transactionId) {
        return set(TRANSACTION_ID, transactionId);
    }

    private void setProductName(@NonNull String productName) {
        set(PRODUCT, productName);
    }

    private void setPriceExchangeAndCurrency(float price, float exchangeRate, @NonNull Currency currency) {
        set(PRICE, price);
        set(EXCHANGE_RATE, exchangeRate);
        set(CURRENCY, currency.getCurrencyCode());
    }

    public static class Builder {

        private final ConversionEvent conversionEvent;

        public Builder(@NonNull String transactionId) {
            Preconditions.checkNotNull(transactionId);
            conversionEvent = new ConversionEvent(transactionId);
        }

        public Builder(long transactionId) {
            conversionEvent = new ConversionEvent(String.valueOf(transactionId));
        }

        public Builder setProductName(@NonNull String productName) {
            conversionEvent.setProductName(productName);
            return this;
        }

        public Builder setPriceExchangeAndCurrency(float price, float exchangeRate, @NonNull Currency currency) {
            conversionEvent.setPriceExchangeAndCurrency(price, exchangeRate, currency);
            return this;
        }

        public Builder setProductList(List<Product> products) {
            conversionEvent.setProducts(products);
            return this;
        }

        public Builder setExtraParameters(Parameters parameters) {
            conversionEvent.setExtraParameters(parameters);
            return this;
        }

        public ConversionEvent build() {
            return conversionEvent;
        }

    }
}
