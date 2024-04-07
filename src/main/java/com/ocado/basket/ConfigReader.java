package com.ocado.basket;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for reading basket configuration from JSON file
 */
public class ConfigReader {
    /**
     *
     * @param absolutePathToConfigFile - path to JSON file containing configuration
     * @return map: product -> list of delivery methods
     */
    public static Map<String, List<String>> readConfig(String absolutePathToConfigFile){
        Map<String, List<String>> configuration = new HashMap<>();
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(absolutePathToConfigFile)) {
            configuration = gson.fromJson(reader, new TypeToken<Map<String, List<String>>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(configuration.size() >= 1000)
            throw new RuntimeException("Configuration file is too big: "+configuration.size()+" The maximum size is 1000.");

        return configuration;
    }
}
