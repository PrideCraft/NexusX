package com.notarin.pride_craft_network.web_server;

import com.notarin.pride_craft_network.database.objects.PrideUser;

public class BuildJson {

    public static String error(String message) {
        return """
                {
                  "status": "error",
                  "message": "%s"
                }""".formatted(message);
    }


    public static String user(PrideUser prideUser) {
        String json = """
                {
                  "status": "success",
                  "data": {
                    "id": "%s",
                    "minecraftUuid": "%s"
                  }
                }""";
        return String.format(json, prideUser.id(), prideUser.minecraftUuid());
    }

}
