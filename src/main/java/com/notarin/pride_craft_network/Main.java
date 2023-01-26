package com.notarin.pride_craft_network;

import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Map<String, Object> config = ConfigHandler.loadConfig();

        com.notarin.pride_craft_network.discord_bot.Main.init(config);
    }

}
