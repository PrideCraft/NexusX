package com.notarin.pride_craft_network;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import static com.notarin.pride_craft_network.LogHandler.logInfo;

public class Main {

    public static void main(String[] args) throws InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        Map<String, Object> config = ConfigHandler.loadConfig();

        com.notarin.pride_craft_network.discord_bot.Main.init(config);
        logInfo("Main", "Pride Craft Network started!");
    }

}
