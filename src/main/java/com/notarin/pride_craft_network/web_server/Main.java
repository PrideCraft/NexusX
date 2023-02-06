package com.notarin.pride_craft_network.web_server;

import com.notarin.pride_craft_network.database.objects.PrideUser;
import org.jetbrains.annotations.NotNull;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.notarin.pride_craft_network.ConfigHandler.loadConfig;
import static com.notarin.pride_craft_network.LogHandler.logWarn;
import static com.notarin.pride_craft_network.database.Query.createAccount;
import static com.notarin.pride_craft_network.database.Query.getAccount;

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
        Spark.post("/make-user/minecraft-uuid/:uuid", (req, res) -> {
            if (elevatedTransaction(req)) {
                String regex = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";
                if (!req.params(":uuid").matches(regex)) {
                    res.status(400);
                    return BuildJson.error("Invalid UUID");
                }
                PrideUser account = createAccount(req.params(":uuid"));
                return getUser(res, account.id());
            } else return denyTransaction(res);
        });
        Spark.get("/user/:id", (req, res) -> {
            String params = req.params(":id");
            return getUser(res, params);
        });
    }

    private static String getUser(Response res, String id) {
        PrideUser account = getAccount(id);
        if (account == null) {
            res.status(404);
            return BuildJson.error("User not found");
        }
        return BuildJson.user(account);
    }

    private static boolean elevatedTransaction(Request req) {
        String token;
        try {
            token = req.headers("Authorization").replace("Bearer ", "");
        } catch (Exception e) {
            return false;
        }
        Map<String, Object> config = loadConfig();
        String[] secrets = parseArray(config.get("secrets").toString());
        List<String> secretList = Arrays.asList(secrets);
        return secretList.contains(token);
    }

    @NotNull
    private static String denyTransaction(Response res) {
        res.status(401);
        return BuildJson.error("Unauthorized");
    }

    private static void configureServer(Map<String, Object> config) {
        Integer port = getPort(config);
        Spark.port(port);
    }

    private static Integer getPort(Map<String, Object> config) {
        Integer port;
        try {
            port = (Integer) config.get("port");
        } catch (ClassCastException e) {
            logWarn("WebServer", "Invalid port in config.yml, defaulting to 8080");
            return 8080;
        }
        return port;
    }

}
