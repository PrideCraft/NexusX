package com.notarin.pride_craft_network.web_server;

import com.notarin.pride_craft_network.database.objects.PrideUser;

public class BuildJson {

    public static String accessDenied() {
        return """
                {
                  "status": "error",
                  "message": "Unauthorized"
                }""";
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

    public static String noSuchUser() {
        return """
                {
                  "status": "error",
                  "message": "No such user"
                }""";
    }

}
