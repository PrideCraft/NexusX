package com.notarin.pride_craft_network;

import java.lang.reflect.InvocationTargetException;

import static com.notarin.pride_craft_network.LogHandler.logInfo;

public class Main {

    public static void main(String[] args) throws InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        com.notarin.pride_craft_network.discord_bot.Main.init();
        logInfo("Main", "Pride Craft Network started!");
    }

}
