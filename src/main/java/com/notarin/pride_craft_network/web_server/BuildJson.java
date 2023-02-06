package com.notarin.pride_craft_network.web_server;

import com.notarin.pride_craft_network.database.objects.PrideUser;

/**
 * A class that contains methods to build JSON responses.
 */
public class BuildJson {

    /**
     * Builds a JSON response for a unsuccessful request.
     *
     * @param message The message to be sent
     * @return The JSON response
     */
    public static String error(String message) {
        return """
                {
                  "status": "error",
                  "message": "%s"
                }""".formatted(message);
    }


    /**
     * Builds a JSON response for the user object.
     *
     * @param prideUser The user to be sent
     * @return The JSON response
     */
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
