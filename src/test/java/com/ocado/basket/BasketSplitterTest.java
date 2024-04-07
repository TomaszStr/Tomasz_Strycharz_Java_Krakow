package com.ocado.basket;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BasketSplitterTest {

    @Test
    void TestConfigurationReading() {
        BasketSplitter splitterConfig = new BasketSplitter("src/main/resources/config.json");
        assertEquals(100,splitterConfig.configuration.size());
        assertTrue(splitterConfig.configuration.containsKey("Cookies Oatmeal Raisin"));
        assertTrue(splitterConfig.configuration.containsKey("Beans - Green"));

        BasketSplitter splitterTestConfig = new BasketSplitter("src/main/resources/test_config.json");
        assertEquals(6,splitterTestConfig.configuration.size());
        assertTrue(splitterTestConfig.configuration.containsKey("Carrots (1kg)"));
        assertTrue(splitterTestConfig.configuration.containsKey("Garden Chair"));
    }
    @Test
    void TestEmptyBasket() {
        BasketSplitter splitterConfig = new BasketSplitter("src/main/resources/config.json");
        assertTrue(splitterConfig.split(new ArrayList<String>()).isEmpty());
    }
    @Test
    void TestNullBasket() {
        BasketSplitter splitterConfig = new BasketSplitter("src/main/resources/config.json");
        assertThrows(NullPointerException.class,
                () -> splitterConfig.split(null).isEmpty());
    }
    @Test
    void TestOneProduct() {
        BasketSplitter splitterConfig = new BasketSplitter("src/main/resources/config.json");
        ArrayList<String> basket = new ArrayList<>(List.of(new String[]{"Sole - Dover, Whole, Fresh"}));
        Map<String, List<String>> split = splitterConfig.split(basket);
        assertEquals(1,split.size());
        assertArrayEquals(new String[]{"In-store pick-up"},split.keySet().toArray());
        assertArrayEquals(new String[]{"Sole - Dover, Whole, Fresh"},split.get("In-store pick-up").toArray());
    }
    @Test
    void TestOneDeliveryGroup() {
        BasketSplitter splitterConfig = new BasketSplitter("src/main/resources/config.json");
        ArrayList<String> basket = new ArrayList<>(List.of(new String[]{
                "Cheese Cloth",
                "English Muffin",
                "Otomegusa Dashi Konbu",
                "Beans - Green",
                "Cake Circle, Paprus"}));
        Map<String, List<String>> split = splitterConfig.split(basket);
        assertEquals(1,split.size());
        assertArrayEquals(new String[]{"Courier"},split.keySet().toArray());
        assertArrayEquals(basket.toArray(),split.get("Courier").toArray());
    }
    @Test
    void TestAllDifferentDeliveryGroups() {
        BasketSplitter splitterConfig = new BasketSplitter("src/main/resources/config.json");
        ArrayList<String> basket = new ArrayList<>(List.of(new String[]{
                "Sole - Dover, Whole, Fresh",
                "Juice - Ocean Spray Cranberry",
                "Garlic - Peeled",
                "Spinach - Frozen",
                "Cake - Miini Cheesecake Cherry"
        }));
        String[] expectedDeliveries = new String[]{
                "In-store pick-up",
                "Pick-up point",
                "Same day delivery",
                "Mailbox delivery",
                "Courier"
        };
        Map<String, List<String>> split = splitterConfig.split(basket);
        assertEquals(5,split.size());
        for(int i=0; i<5; i++)
            assertArrayEquals(new String[]{basket.get(i)},split.get(expectedDeliveries[i]).toArray());
    }
    @Test
    void TestGivenSolution() {
        BasketSplitter splitter = new BasketSplitter("src/main/resources/test_config.json");

        Gson gson = new Gson();
        List<String> basket = new ArrayList<>();
        try (FileReader reader = new FileReader("src/main/resources/test_basket.json")) {
            basket = gson.fromJson(reader, new TypeToken<List<String>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, List<String>> split = splitter.split(basket);
        Map<String, List<String>> realSolution = new HashMap<>();

        realSolution.put("Courier",new ArrayList<>(List.of(new String[]{
                "Espresso Machine",
                "Garden Chair"
        })));
        realSolution.put("Express Delivery",new ArrayList<>(List.of(new String[]{
                "Carrots (1kg)",
                "Cold Beer (330ml)",
                "Steak (300g)",
                "AA Battery (4 Pcs.)"
        })));

        assertEquals(2,split.size());
        assertTrue(realSolution.get("Courier").size() == split.get("Courier").size() && realSolution.get("Courier").containsAll(split.get("Courier")));
        assertTrue(realSolution.get("Express Delivery").size() == split.get("Express Delivery").size() && realSolution.get("Express Delivery").containsAll(split.get("Express Delivery")));
    }

}