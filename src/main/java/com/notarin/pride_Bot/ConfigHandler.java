package com.notarin.pride_Bot;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

public class ConfigHandler {
    public static Map<String, Object> loadConfig() {
        Map<String, Object> config;

        try {
            //Loading config file
            InputStream inputStream = new FileInputStream("config.yml");
            Yaml yaml = new Yaml();
            //set the config object
            config = yaml.load(inputStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return config;
    }
}
