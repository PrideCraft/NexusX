package com.notarin.pride_craft_network;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import static com.notarin.pride_craft_network.LogHandler.logError;
import static com.notarin.pride_craft_network.LogHandler.logWarn;

/**
 * This class handles the loading of the config file, and checking for any
 * updates to the config file.
 */
public class ConfigHandler {

    static Map<String, Object> config;

    /**
     * Loads the config file, and checks for any updates to the config file.
     *
     * @return The config object
     */
    public static Map<String, Object> loadConfig() {
        if (!(config == null)) {
            return config;
        }

        try {
            //Loading config file
            final InputStream inputStream = new FileInputStream("config.yml");
            final Yaml yaml = new Yaml();
            //set the config object
            config = yaml.load(inputStream);
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        //check for any updates or problems to the bots config
        updateCheck(config);

        return config;
    }

    /**
     * Checks for any updates to the config file.
     *
     * @param config The config object
     */
    public static void updateCheck(final Map<String, Object> config) {
        final Map<String, Object> exampleConfig;

        try {
            //Loading config example file
            final InputStream inputStream =
                    new FileInputStream("config-example.yml");
            final Yaml yaml = new Yaml();
            //set the config object
            exampleConfig = yaml.load(inputStream);
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        //check for unconfigured values in config, or useless values in config
        for (final Map.Entry<String, Object>
                configIteration : config.entrySet()) {
            //Check for keys in config not present in exampleconfig,
            // this isn't really a problem so just warn.
            if (exampleConfig.get(configIteration.getKey()) == null) {
                logWarn("ConfigHandler",
                        "Key found in config not present in example " +
                                "config: " + configIteration.getKey());
            }
            if (configIteration.getValue()
                    .equals(exampleConfig.get(configIteration.getKey()))) {
                logError("ConfigHandler",
                        "Config value unchanged, please configure key \""
                                + configIteration.getKey() + "\"");
            }
        }

        //check for missing keys in config
        for (
                final Map.Entry<String, Object> exampleConfigIteration :
                exampleConfig.entrySet()
        ) {
            if (config.get(exampleConfigIteration.getKey()) == null) {
                logError("ConfigHandler",
                        "Key found in example config not present in config: "
                                + exampleConfigIteration.getKey() +
                                "\nPlease copy all missing values and set " +
                                "them.");
            }
        }
    }
}
