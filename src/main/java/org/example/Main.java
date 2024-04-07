package org.example;

import com.ocado.basket.BasketSplitter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        basket1();
        System.out.println();
        basket2();
        System.out.println();
        basketTest();
    }

    public static void basket1(){
        BasketSplitter splitter = new BasketSplitter("src/main/resources/config.json");
        Gson gson = new Gson();


        List<String> basket = new ArrayList<>();
        try (FileReader reader = new FileReader("src/main/resources/basket-1.json")) {
            basket = gson.fromJson(reader, new TypeToken<List<String>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, List<String>> split = splitter.split(basket);
        System.out.println("BASKET 1");
        for(String delivery: split.keySet()){
            System.out.println(delivery + split.get(delivery));
        }
    }

    public static void basket2(){
        BasketSplitter splitter = new BasketSplitter("src/main/resources/config.json");
        Gson gson = new Gson();


        List<String> basket = new ArrayList<>();
        try (FileReader reader = new FileReader("src/main/resources/basket-2.json")) {
            basket = gson.fromJson(reader, new TypeToken<List<String>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, List<String>> split = splitter.split(basket);
        System.out.println("BASKET 2");
        for(String delivery: split.keySet()){
            System.out.println(delivery + split.get(delivery));
        }

        Set<String> deliveries = new HashSet<>();
    }

    public static void basketTest(){
        BasketSplitter splitter = new BasketSplitter("src/main/resources/test_config.json");
        Gson gson = new Gson();


        List<String> basket = new ArrayList<>();
        try (FileReader reader = new FileReader("src/main/resources/test_basket.json")) {
            basket = gson.fromJson(reader, new TypeToken<List<String>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, List<String>> split = splitter.split(basket);
        System.out.println("TEST BASKET");
        for(String delivery: split.keySet()){
            System.out.println(delivery + split.get(delivery));
        }
    }
}