package com.notarin.pride_craft_network;

import java.lang.reflect.InvocationTargetException;
import static com.notarin.pride_craft_network.LogHandler.logInfo;

/**
 * The main class for the Pride Craft Network.
 */
public class Main {

    /**
     * The main method for initializing the bot.
     *
     * @param args The arguments
     * @throws InvocationTargetException Thrown when registering slash commands
     * fails
     * @throws NoSuchMethodException Should never be thrown, but if it does,
     * the reflector found a method that doesn't exist
     * @throws InstantiationException Thrown when registering slash commands
     * fails
     * @throws IllegalAccessException Thrown when registering slash commands
     * fails
     */
    public static void main(final String[] args) throws InvocationTargetException,
            NoSuchMethodException, InstantiationException,
            IllegalAccessException {
        com.notarin.pride_craft_network.discord_bot.Main.init();
        com.notarin.pride_craft_network.web_server.Main.init();
        logInfo("Main", "Pride Craft Network started!");
    }

}
