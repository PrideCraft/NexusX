package com.notarin.pride_craft_network.web_server;

public class BuildJson {

    public static String accessDenied() {
        return """
                {
                  "status": "error",
                  "message": "Unauthorized"
                }""";
    }

}
