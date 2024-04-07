package com.ocado.basket;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Class made for checking whether subset of delivery methods fits all products
 */
public class SubsetChecker {

    Map<String, List<String>> deliveryToProduct;
    List<String> deliveries;
    List<String> items;

    /**
     *
     * @param items - all products in the basket - unique list
     * @param deliveries - all deliveries
     * @param deliveryToProduct - map: delivery -> list of products
     */
    public SubsetChecker(List<String> items, List<String> deliveries, Map<String, List<String>> deliveryToProduct){
        this.deliveryToProduct = deliveryToProduct;
        this.items = items;
        this.deliveries = deliveries;
    }

    /**
     *
     * @param subset - subset of delivery methods to be checked
     * @return - whether the subset fits all products in the basket
     */
    public boolean checkSubset(List<String> subset){
        HashSet<String> products = new HashSet<>();
        for(String method : subset)
            products.addAll(deliveryToProduct.get(method));
        return products.size() == items.size();
    }
}
