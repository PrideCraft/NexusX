package com.notarin.pride_craft_network.web_server;

import org.jetbrains.annotations.NotNull;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.notarin.pride_craft_network.ConfigHandler.loadConfig;

public class Main {

    public static void init() {
        Map<String, Object> config = loadConfig();
        configureServer(config);
        Spark.init();
        defineServerPaths();
    }

    private static String[] parseArray(String inputString) {
        String removedOpenBracket =
                inputString.replace("[", "");
        String removedCloseBracket =
                removedOpenBracket.replace("]", "");
        String removedSpaces =
                removedCloseBracket.replace(" ", "");
        return removedSpaces.split(",");
    }

    private static void defineServerPaths() {
        Spark.get("/ping", (req, res) -> "Pong!");
        Spark.post("/users/:username", (req, res) -> {
            if (elevatedTransaction(req)) return "User creation not supported yet!";
            else return denyTransaction(res);
        });
    }

    private static boolean elevatedTransaction(Request req) {
        String token = req.headers("Authorization").replace("Bearer ", "");
        Map<String, Object> config = loadConfig();
        String[] secrets = parseArray(config.get("secrets").toString());
        List<String> secretList = Arrays.asList(secrets);
        return secretList.contains(token);
    }

    @NotNull
    private static String denyTransaction(Response res) {
        res.status(401);
        return "Unauthorized";
    }

    private static void configureServer(Map<String, Object> config) {
        Integer port = getPort(config);
        Spark.port(port);
    }

    private static Integer getPort(Map<String, Object> config) {
        Integer port;
        try {
            port = (Integer) config.get("port");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return port;
    }

}
